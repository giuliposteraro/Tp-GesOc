package servicio.abOperaciones;

import dominio.entidades.Organizacion;
import dominio.operaciones.*;
import dominio.operaciones.medioDePago.MedioDePago;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class ServicioABOperaciones {

    public void altaOperacion(ArrayList<Item> items, MedioDePago medioDePago, DocumentoComercial documento, Date fecha, double valorOperacion, EntidadOperacion entidadOrigen, EntidadOperacion entidadDestino) throws Exception{
        OperacionEgresoBuilder operacionEgresoBuilder = new OperacionEgresoBuilder();
        operacionEgresoBuilder.agregarItems(items)
                .agregarMedioDePago(medioDePago)
                .agregarDocComercial(documento)
                .agregarFecha(fecha)
                .agregarEntidadOrigen(entidadOrigen)
                .agregarEntidadDestino(entidadDestino);

        RepoOperacionesEgreso.getInstance().agregarOperacionEgreso(operacionEgresoBuilder.build());
    }


    public ArrayList<OperacionIngreso> listarIngresosPorOrg(Organizacion organizacion){
        return new ArrayList<OperacionIngreso>(this.listarIngresos().stream().
                filter(ingreso -> ingreso.esDeLaOrganizacion(organizacion)).collect(Collectors.toList()));    }

    public void bajaOperacion(OperacionEgreso operacionEgreso){
        RepoOperacionesEgreso.getInstance().eliminarOperacionEgreso(operacionEgreso);
    }

    public ArrayList<OperacionEgreso> listarOperaciones(){
        return RepoOperacionesEgreso.getInstance().getOperacionesEgreso();
    }

    public ArrayList<OperacionIngreso> listarIngresos(){
        return RepoOperacionesIngreso.getInstance().getIngresos();
    }

    public ArrayList<OperacionEgreso> listarOperaciones(Organizacion unaOrganizacion){
        return new ArrayList<OperacionEgreso>(this.listarOperaciones().stream().
                filter(egreso -> egreso.esDeLaOrganizacion(unaOrganizacion)).collect(Collectors.toList()));
    }

    public OperacionEgreso buscarEgreso(String identificadorEgreso){
        return RepoOperacionesEgreso.getInstance().buscarOperacionEgresoPorIdentificador(identificadorEgreso);
    }
}
