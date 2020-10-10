package servidor.controladores;

import dominio.cuentasUsuarios.CuentaUsuario;
import dominio.entidades.ETipoEmpresa;
import dominio.entidades.Empresa;
import dominio.entidades.EntidadJuridica;
import dominio.entidades.Organizacion;
import dominio.entidades.calculadorFiscal.ETipoActividad;
import dominio.licitacion.Licitacion;
import dominio.licitacion.Presupuesto;
import dominio.licitacion.RepoLicitaciones;
import dominio.operaciones.*;
import dominio.operaciones.medioDePago.*;
import org.json.JSONArray;
import servicio.abOperaciones.ServicioABOperaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.text.SimpleDateFormat;

import temporal.seguridad.repositorioUsuarios.RepositorioUsuarios;

import java.util.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;


public class EgresoController extends Controller{

    ServicioABOperaciones servicioOperaciones = new ServicioABOperaciones();

    private String mensajeError;

    public ModelAndView mostrarEgresos(Request req, Response res) {

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("user", req.session().attribute("user"));

        CuentaUsuario usuario = req.session().attribute("user");

        Organizacion org = usuario.getOrganizacion();

        //parameters.put("egresos", servicioOperaciones.metodoQueHaceFedeParaFiltrarEgresoPorOrganizacion(org));

        ArrayList<OperacionEgreso> egresosPaginados = new ArrayList<>();

        ArrayList<OperacionEgreso> egresos = servicioOperaciones.listarOperaciones();


        int egresosPorPagina = 3;

        String pagina = req.queryParams("pagina");

        if(pagina == null){
            if(egresos.size() > egresosPorPagina){ // 3 egresos por pagina
                res.redirect("/egresos?pagina=1"); // redirecciona a la pagina 1
                return null;
            }

            //OUTPUT
            Map<String, Object> map = new HashMap<>();
            map.put("egresos",egresos);
            map.put("user", req.session().attribute("user"));
            String mensajeE = Objects.toString(req.queryParams("error"),"");
            if(mensajeE.equals("licitacionFinalizada"))
                parameters.put("error","No puede agregarse el presupuesto debido a que la licitacion a finalizado");


            return new ModelAndView(map,"egresos2.hbs");
        }
        else{
            int numeroPagina = Integer.parseInt(pagina);
            int indiceInicial = Math.min((numeroPagina - 1) * egresosPorPagina,egresos.size());
            int indiceFinal = Math.min(numeroPagina * egresosPorPagina,egresos.size());
            List<OperacionEgreso> egresosSubLista = egresos.subList(indiceInicial,indiceFinal);

            //OUTPUT
            Map<String, Object> map = new HashMap<>();
            map.put("egresos",egresosSubLista);

            int cantidadPaginas = (int) Math.ceil((double)egresos.size()/egresosPorPagina);
            ArrayList<Integer> listaCantidadPaginas = new ArrayList<>();
            for(int i = 1;i<=cantidadPaginas; i++){
                listaCantidadPaginas.add(i);
            }
            map.put("cantidad_paginas",listaCantidadPaginas);
            if(numeroPagina > 1)
                map.put("pagina_anterior",numeroPagina - 1);
            if(numeroPagina * egresosPorPagina < egresos.size())
                map.put("pagina_siguiente",numeroPagina + 1);

            map.put("user", req.session().attribute("user"));

            String mensajeE = Objects.toString(req.queryParams("error"),"");
            if(mensajeE.equals("licitacionFinalizada"))
                parameters.put("error","No puede agregarse el presupuesto debido a que la licitacion a finalizado");


            return new ModelAndView(map,"egresos2.hbs");
        }

    }


    public ModelAndView showEgreso(Request request, Response response){
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("user", request.session().attribute("user"));



        return new ModelAndView(parameters, "egreso.hbs");
    }


    public ModelAndView deleteEgreso(Request request, Response response){
        Map<String, Object> parameters = new HashMap<>();

        String identificador = request.params("identificador");


        RepoOperacionesEgreso.getInstance().eliminarOperacionEgresoPorIdentificador(identificador);

        response.redirect("/egresos");

        return null;
    }


    public ModelAndView crearEgreso(Request req, Response res) throws Exception {

        try {
            MedioDePago medioDePagoFinal;
            String monto;
            String nombre;
            String numero;

            if(!req.queryParams("tarjeta-credito-num").isEmpty()){
                nombre = req.queryParams("tarjeta-credito-nombre-apellido");
                String cuotas = req.queryParams("tarjeta-credito-cantidad");
                numero = req.queryParams("tarjeta-credito-num");
                medioDePagoFinal = new TarjetaDeCredito(Integer.valueOf(cuotas), nombre, numero);
            }
            else{
                if(!req.queryParams("tarjeta-debito-num").isEmpty()){
                    nombre = req.queryParams("tarjeta-debito-nombre-apellido");
                    numero = req.queryParams("tarjeta-debito-num");
                    medioDePagoFinal = new TarjetaDeDebito(nombre, numero);
                }
                else {
                    if(!req.queryParams("efectivo-monto").isEmpty()){
                        monto = req.queryParams("efectivo-monto");
                        String puntoDePago = req.queryParams("efectivo-punto-de-pago");
                        medioDePagoFinal = new Efectivo(Double.valueOf(monto), puntoDePago, "Efectivo");
                    }
                    else{
                        if(!req.queryParams("dinero-cuenta-monto").isEmpty()){
                            monto = req.queryParams("dinero-cuenta-monto");
                            nombre = req.queryParams("dinero-cuenta-nombre-apellido");
                            medioDePagoFinal = new DineroEnCuenta(Double.valueOf(monto), nombre);
                        }
                        else{
                            throw new Exception("no hay medio de pago válido");
                        }
                    }
                }
            }

            String documentoComercialNumero = req.queryParams("documento-num");

            ETipoDoc Etipo =  tipoDeDocumento.get(req.queryParams("tipo-documento"));


            DocumentoComercial documento = new DocumentoComercial(Etipo, Integer.valueOf(documentoComercialNumero));


            String fecha = req.queryParams("operacion-egreso-date");

            String cantItems = req.queryParams("cantidad-items");

            ArrayList<Item> items = new ArrayList<>();
            ETipoItem itemTipoEnum;
            String itemNum;

            for (int i = 0; i < Integer.valueOf(cantItems); i++) {

                itemNum = String.valueOf(i);

                String itemValor = req.queryParams("item_monto" + itemNum);
                String itemDescripcion = req.queryParams("item_descripcion" + itemNum);
                itemTipoEnum =  tipoDeItem.get(req.queryParams("item_tipo" + itemNum));


                items.add(new Item(Integer.valueOf(itemValor), itemTipoEnum, itemDescripcion));

            }

            CuentaUsuario usuario = req.session().attribute("user");

            Organizacion org = usuario.getOrganizacion();


            String EONombre = req.queryParams("query_EO_nombre");

            EntidadJuridica entidadJuridica = org.buscarEntidad(EONombre);

            String EOCuil = entidadJuridica.getCuit();
            String EODireccion = entidadJuridica.getDireccionPostal();

            EntidadOperacion entidadOrigen = new EntidadOperacion(EONombre, EOCuil, EODireccion);


            String EDNombre = req.queryParams("query_ED_nombre");
            String EDCuil = req.queryParams("query_ED_cuil");
            String EDDireccion = req.queryParams("query_ED_direccion");

            EntidadOperacion entidadDestino = new EntidadOperacion(EDNombre, EDCuil, EDDireccion);

            String presupuestosNecesarios = req.queryParams("presupuestos-necesarios-num");

            Date parsed=new SimpleDateFormat("yyyy-MM-dd").parse(fecha);

            OperacionEgreso egreso = new OperacionEgreso(items,medioDePagoFinal , documento, parsed, entidadOrigen, entidadDestino, Integer.valueOf(presupuestosNecesarios));

            RepoOperacionesEgreso.getInstance().agregarOperacionEgreso(egreso);


        }
        catch(NullPointerException e){
            mensajeError = "Null error: " + e.getMessage();
            return new ModelAndView(this, "fallaCreacionEgreso.hbs"); }
        catch (Exception e) {
            mensajeError = "Error desconocido: " + e.getMessage();
            return new ModelAndView(this, "fallaCreacionEgreso.hbs");
        }

        /* Si no se pone el redirect, igual va a ir a esa uri por que esta en la action de la form. Pero el metodo va a ser post, entonces cada vez que se recargue la pagina se vuelve a agregar la prenda. El redirect es un get de la uri.*/
        res.redirect("/egresos");

        return null;


    }

    public ModelAndView showModificarEgreso(Request req, Response res){
        Map<String, Object> parameters = new HashMap<>();
        OperacionEgreso egreso = servicioOperaciones.buscarEgreso(req.params("id"));
        Date fecha = egreso.getFecha();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
        String fechaFormateada = formateador.format(fecha);

        parameters.put("user", req.session().attribute("user"));
        parameters.put("egreso", egreso);
        parameters.put("fecha", fechaFormateada);

        return new ModelAndView(parameters, "egreso.hbs");
    }

    public ModelAndView modificarEgreso(Request req, Response res){

        try {

            MedioDePago medioDePagoFinal;
            String monto;
            String nombre;
            String numero;

            if(!req.queryParams("tarjeta-credito-num").isEmpty()){
                nombre = req.queryParams("tarjeta-credito-nombre-apellido");
                String cuotas = req.queryParams("tarjeta-credito-cantidad");
                numero = req.queryParams("tarjeta-credito-num");
                medioDePagoFinal = new TarjetaDeCredito(Integer.valueOf(cuotas), nombre, numero);
            }
            else{
                if(!req.queryParams("tarjeta-debito-num").isEmpty()){
                    nombre = req.queryParams("tarjeta-debito-nombre-apellido");
                    numero = req.queryParams("tarjeta-debito-num");
                    medioDePagoFinal = new TarjetaDeDebito(nombre, numero);
                }
                else {
                    if(!req.queryParams("efectivo-monto").isEmpty()){
                        monto = req.queryParams("efectivo-monto");
                        String puntoDePago = req.queryParams("efectivo-punto-de-pago");
                        medioDePagoFinal = new Efectivo(Double.valueOf(monto), puntoDePago, "Efectivo");
                    }
                    else{
                        if(!req.queryParams("dinero-cuenta-monto").isEmpty()){
                            monto = req.queryParams("dinero-cuenta-monto");
                            nombre = req.queryParams("dinero-cuenta-nombre-apellido");
                            medioDePagoFinal = new DineroEnCuenta(Double.valueOf(monto), nombre);
                        }
                        else{
                            throw new Exception("no hay medio de pago válido");
                        }
                    }
                }
            }

            String documentoComercialNumero = req.queryParams("documento-num");

            String tipoDocumento = req.queryParams("tipo-documento");

            ETipoDoc Etipo =  tipoDeDocumento.get(req.queryParams("tipo-documento"));


            DocumentoComercial documento = new DocumentoComercial(Etipo, Integer.valueOf(documentoComercialNumero));


            String fecha = req.queryParams("operacion-egreso-date");

            String EONombre = req.queryParams("query_EO_nombre");
            String EOCuil = req.queryParams("query_EO_cuil");
            String EODireccion = req.queryParams("query_EO_direccion");

            EntidadOperacion entidadOrigen = new EntidadOperacion(EONombre, EOCuil, EODireccion);

            // TODO, debe verificarse que la entidad origen sea perteneciente a la org del usuario

            String EDNombre = req.queryParams("query_ED_nombre");
            String EDCuil = req.queryParams("query_ED_cuil");
            String EDDireccion = req.queryParams("query_ED_direccion");

            EntidadOperacion entidadDestino = new EntidadOperacion(EDNombre, EDCuil, EDDireccion);

            OperacionEgreso egreso = servicioOperaciones.buscarEgreso(req.params("id"));

            Date parsed=new SimpleDateFormat("yyyy-MM-dd").parse(fecha);

            // Modificar Egreso
            egreso.modificarse(medioDePagoFinal , documento, parsed, entidadOrigen, entidadDestino);

        }
        catch(NullPointerException e){
            mensajeError = "Null error: " + e.getMessage();
            return new ModelAndView(this, "fallaCreacionEgreso.hbs"); }
        catch (Exception e) {
            mensajeError = "Error desconocido: " + e.getMessage() + req.queryMap();
            return new ModelAndView(this, "fallaCreacionEgreso.hbs");
        }

        /* Si no se pone el redirect, igual va a ir a esa uri por que esta en la action de la form. Pero el metodo va a ser post, entonces cada vez que se recargue la pagina se vuelve a agregar la prenda. El redirect es un get de la uri.*/
        res.redirect("/egresos");
        return null;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    private HashMap<String, ETipoItem> tipoDeItem = new HashMap<String, ETipoItem>() {{
        put("articulo", ETipoItem.ARTICULO);
        put("servicio", ETipoItem.SERVICIO);
    }};

    private HashMap<String, ETipoDoc> tipoDeDocumento = new HashMap<String, ETipoDoc>() {{
        put("ticket", ETipoDoc.TICKET);
        put("factura", ETipoDoc.FACTURA);
        put("TICKET", ETipoDoc.TICKET);
        put("FACTURA", ETipoDoc.FACTURA);
    }};

}
