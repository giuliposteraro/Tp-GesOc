package mock;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import datos.*;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dominio.categorizacion.CriterioDeCategorizacion;
import dominio.categorizacion.exceptions.CategorizacionException;
import dominio.cuentasUsuarios.Roles.Privilegio;
import dominio.cuentasUsuarios.Roles.Rol;
import dominio.entidades.ETipoEmpresa;
import dominio.entidades.Empresa;
import dominio.entidades.Organizacion;
import dominio.entidades.calculadorFiscal.ETipoActividad;
import dominio.licitacion.Licitacion;
import dominio.licitacion.Presupuesto;
import dominio.licitacion.criterioSeleccion.CriterioMenorPrecio;
import dominio.notificador_suscriptores.NotificadorSuscriptores;
import dominio.operaciones.*;
import dominio.operaciones.medioDePago.Efectivo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Date;

public class ServerDataMock {
    private static EntityManagerFactory entityManagerFactory;
    static MongoClient mongoClient;
    static Datastore datastore;

    public static void main(String[] args) throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("db");

        cargarMock();
    }

    public static void cargarMock() throws Exception {
        initAudit();
        cargarOrganizaciones();
        cargarRoles();
        cargarUsuarios();
        cargarCategorias();
        cargarIngresos();
        cargarEgregos();
    }

    private static void initAudit() {
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://mongodb:0AnE83904LltBfkF@cluster0.8pqel.mongodb.net/operations_audit?retryWrites=true&w=majority");

        mongoClient = new MongoClient(uri);

        Morphia morphia = new Morphia();
        morphia.mapPackage("auditoria");

        datastore = morphia.createDatastore(mongoClient, "operations_audit");
        datastore.ensureIndexes();
    }

    public ServerDataMock() {
        entityManagerFactory = Persistence.createEntityManagerFactory("db");
    }

    private static void cargarOrganizaciones() {

        ArrayList<Empresa> entidades1 = new ArrayList<>();
        ArrayList<Empresa> entidades2 = new ArrayList<>();

        Empresa entidad1 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa 1", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidad2 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa 2", "Empresa 2", "20-40678950-4", "203", "Av.Libertador 200", false);
        Empresa entidad3 = new Empresa(ETipoEmpresa.MEDIANA_T2, 5, ETipoActividad.COMERCIO, 3000.54, "Empresa 3", "Empresa 3", "20-57678950-4", "205", "Av.Maipu 200", false);

        Empresa entidadO1 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Origen 1", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadO2 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Origen 2", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadO3 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Origen 3", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadO4 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Origen 4", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadO5 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Origen 5", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadO6 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Origen 6", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadO7 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Origen 7", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);

        Empresa entidadD1 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Destino 1", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadD2 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Destino 2", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadD3 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Destino 3", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadD4 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Destino 4", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadD5 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Destino 5", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadD6 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Destino 6", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);
        Empresa entidadD7 = new Empresa(ETipoEmpresa.MEDIANA_T1, 3, ETipoActividad.COMERCIO, 2000.54, "Empresa Destino 7", "Empresa 1", "20-40678950-3", "200", "Av.Libertador 801", false);

        entidades1.add(entidadO1);
        entidades1.add(entidadO2);
        entidades1.add(entidadO3);
        entidades1.add(entidadO4);
        entidades1.add(entidadO5);
        entidades1.add(entidadO6);
        entidades1.add(entidadO7);

        entidades2.add(entidadD1);
        entidades2.add(entidadD2);
        entidades2.add(entidadD3);
        entidades2.add(entidadD4);
        entidades2.add(entidadD5);
        entidades2.add(entidadD6);
        entidades2.add(entidadD7);

        entidades1.add(entidad1);
        entidades2.add(entidad2);

        EntityManager em = getEntityManager();
        RepoOrganizaciones repoOrganizaciones = new RepoOrganizaciones(em);

        em.getTransaction().begin();
        repoOrganizaciones.agregarOrganizacion("Organizacion1", entidades1);
        repoOrganizaciones.agregarOrganizacion("Organizacion2", entidades2);
        em.getTransaction().commit();

    }

    private static void cargarRoles() {
        Privilegio privilegioABOrganizacion = new Privilegio("PRIVILEGIO_AB_ORGANIZACIONES");
        Privilegio privilegioABMUsuarios = new Privilegio("PRIVILEGIO_ABM_USUARIOS");
        Privilegio privilegioABMEntidadesJuridicas = new Privilegio("PRIVILEGIO_ABM_ENTIDADES_JURIDICAS");
        Privilegio privilegioABMEntidadesBase = new Privilegio("PRIVILEGIO_ABM_ENTIDADES_BASE");
        Privilegio privilegioABOperacion = new Privilegio("PRIVILEGIO_AB_OPERACIONES");
        Privilegio privilegioABLicitaciones = new Privilegio("PRIVILEGIO_AB_LICITACIONES");
        Privilegio privilegioRevisor = new Privilegio("PRIVILEGIO_REVISOR");
        Privilegio privilegioRecategorizador = new Privilegio("PRIVILEGIO_RECATEGORIZADOR");
        Privilegio privilegioVinculador = new Privilegio("PRIVILEGIO_VINCULADOR");

        ArrayList<Privilegio> privilegiosRolAdministradorSistema = new ArrayList<Privilegio>();
        ArrayList<Privilegio> privilegiosRolAdministradorOrganizacion = new ArrayList<Privilegio>();
        ArrayList<Privilegio> privilegiosRolEstandar = new ArrayList<Privilegio>();
        ArrayList<Privilegio> privilegiosRolRevisor = new ArrayList<Privilegio>();

        privilegiosRolAdministradorSistema.add(privilegioABOrganizacion);
        privilegiosRolAdministradorSistema.add(privilegioABMUsuarios);
        privilegiosRolAdministradorOrganizacion.add(privilegioABMEntidadesJuridicas);
        privilegiosRolAdministradorOrganizacion.add(privilegioABMEntidadesBase);
        privilegiosRolEstandar.add(privilegioABOperacion);
        privilegiosRolEstandar.add(privilegioABLicitaciones);
        privilegiosRolEstandar.add(privilegioRecategorizador);
        privilegiosRolEstandar.add(privilegioVinculador);

        privilegiosRolRevisor.add(privilegioRevisor);

        Rol rolAdministradorSistema = new Rol("ROL_ADMINISTRADOR_SISTEMA", privilegiosRolAdministradorSistema);
        Rol rolAdministradorOrganizacion = new Rol("ROL_ADMINISTRADOR_ORGANIZACION", privilegiosRolAdministradorOrganizacion);
        Rol rolEstandar = new Rol("ROL_ESTANDAR", privilegiosRolEstandar);
        Rol rolRevisor = new Rol("ROL_REVISOR", privilegiosRolRevisor);

        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        em.persist(rolAdministradorSistema);
        em.persist(rolAdministradorOrganizacion);
        em.persist(rolEstandar);
        em.persist(rolRevisor);

        em.getTransaction().commit();
    }

    private static void cargarUsuarios() {
        ArrayList<String> listaDeRolesCliente = new ArrayList<String>();
        listaDeRolesCliente.add("ROL_ESTANDAR");
        ArrayList<String> listaDeRolesClienteMaestro = new ArrayList<String>();
        listaDeRolesClienteMaestro.add("ROL_ADMINISTRADOR_ORGANIZACION");
        listaDeRolesClienteMaestro.add("ROL_ESTANDAR");
        listaDeRolesClienteMaestro.add("ROL_REVISOR");

        EntityManager em = getEntityManager();
        // Usuarios Administradores
        em.getTransaction().begin();
        RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios(em);
        repositorioUsuarios.agregarUsuarioAdministrador("admin1", "1234");
        repositorioUsuarios.agregarUsuarioAdministrador("admin2", "1234");
        repositorioUsuarios.agregarUsuarioAdministrador("admin3", "1234");

        // Usuarios Estandar
        RepoOrganizaciones repoOrganizaciones = new RepoOrganizaciones(em);

        Organizacion organizacion1 = repoOrganizaciones.buscarOrganizacion("Organizacion1");
        Organizacion organizacion2 = repoOrganizaciones.buscarOrganizacion("Organizacion2");

        repositorioUsuarios.agregarUsuarioEstandar("UsuarioWeb1", organizacion1, listaDeRolesCliente, "1234");
        repositorioUsuarios.agregarUsuarioEstandar("UsuarioWeb2", organizacion2, listaDeRolesClienteMaestro, "1234");
        em.getTransaction().commit();
    }

    private static void cargarCategorias() throws CategorizacionException {
        EntityManager em = getEntityManager();
        // Usuarios Administradores
        em.getTransaction().begin();
        RepositorioCategorizacion repositorioCategorizacion = new RepositorioCategorizacion(em);

        CriterioDeCategorizacion criterioDePrueba1 = new CriterioDeCategorizacion("CriterioDePrueba-1");
        repositorioCategorizacion.agregarCriterioDeCategorizacion(criterioDePrueba1);
        criterioDePrueba1.agregarCategoria("Categoria-1");
        criterioDePrueba1.agregarCategoria("Categoria-1.1", "Categoria-1");
        criterioDePrueba1.agregarCategoria("Categoria-2");

        CriterioDeCategorizacion criterioDePrueba2 = new CriterioDeCategorizacion("CriterioDePrueba-2");
        repositorioCategorizacion.agregarCriterioDeCategorizacion(criterioDePrueba2);
        criterioDePrueba2.agregarCategoria("Categoria-1");
        criterioDePrueba2.agregarCategoria("Categoria-1.1", "Categoria-1");
        criterioDePrueba2.agregarCategoria("Categoria-2");
        criterioDePrueba2.agregarCategoria("Categoria-3");
        criterioDePrueba2.agregarCategoria("Categoria-3.1", "Categoria-3");
        em.getTransaction().commit();
    }

    private static void cargarIngresos() throws Exception {
        OperacionIngreso ingreso1 = new OperacionIngreso();
        ingreso1.setFecha(new Date());
        ingreso1.setMontoTotal(1000);
        ingreso1.setDescripcion("ingreso1");

        OperacionIngreso ingreso2 = new OperacionIngreso();
        ingreso2.setFecha(new Date());
        ingreso2.setMontoTotal(2000);
        ingreso2.setDescripcion("ingreso2");

        OperacionIngreso ingreso3 = new OperacionIngreso();
        ingreso3.setFecha(new Date());
        ingreso3.setMontoTotal(3434.6);
        ingreso3.setDescripcion("ingreso3");

        OperacionIngreso ingreso4 = new OperacionIngreso();
        ingreso4.setFecha(new Date());
        ingreso4.setMontoTotal(565.3);
        ingreso4.setDescripcion("ingreso4");

        OperacionIngreso ingreso5 = new OperacionIngreso();
        ingreso5.setFecha(new Date());
        ingreso5.setMontoTotal(1100);
        ingreso5.setDescripcion("ingreso5");

        OperacionIngreso ingreso6 = new OperacionIngreso();
        ingreso6.setFecha(new Date());
        ingreso6.setMontoTotal(1200);
        ingreso6.setDescripcion("ingreso6");

        EntityManager em = getEntityManager();
        RepoEntidadesJuridicas repoEntidadesJuridicas = new RepoEntidadesJuridicas(em);

        EntidadOperacion origen = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Origen 1").getEntidadOperacion();
        EntidadOperacion destino = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Destino 1").getEntidadOperacion();

        ingreso1.setEntidadOrigen(origen);
        ingreso1.setEntidadDestino(destino);

        ingreso2.setEntidadOrigen(origen);
        ingreso2.setEntidadDestino(destino);

        ingreso3.setEntidadOrigen(origen);
        ingreso3.setEntidadDestino(destino);

        ingreso4.setEntidadOrigen(origen);
        ingreso4.setEntidadDestino(destino);

        ingreso5.setEntidadOrigen(destino);
        ingreso5.setEntidadDestino(origen);

        ingreso6.setEntidadOrigen(destino);
        ingreso6.setEntidadDestino(origen);

        RepoOperacionesIngreso repoIngreso = new RepoOperacionesIngreso(em);
        em.getTransaction().begin();
        repoIngreso.agregarIngreso(ingreso1);
        em.getTransaction().commit();

        em.getTransaction().begin();
        repoIngreso.agregarIngreso(ingreso2);
        repoIngreso.agregarIngreso(ingreso3);
        repoIngreso.agregarIngreso(ingreso4);
        repoIngreso.agregarIngreso(ingreso5);
        repoIngreso.agregarIngreso(ingreso6);
        em.getTransaction().commit();
    }

    private static void cargarEgregos() throws Exception {
        OperacionEgresoBuilder builderEgreso = new OperacionEgresoBuilder();
        ArrayList<Item> items1 = new ArrayList<>();
        ArrayList<Item> items2 = new ArrayList<>();
        ArrayList<Item> items3 = new ArrayList<>();
        ArrayList<Item> items4 = new ArrayList<>();
        ArrayList<Item> items5 = new ArrayList<>();
        ArrayList<Item> items6 = new ArrayList<>();
        ArrayList<Item> items7 = new ArrayList<>();


        Item resma = new Item(300, ETipoItem.ARTICULO, "Resma de hojas");
        Item flete = new Item(350, ETipoItem.SERVICIO, "Servicio de transporte de productos");
        Item tinta = new Item(500, ETipoItem.ARTICULO, "Cartucho de tinta");
        Item pc1 = new Item(50000, ETipoItem.ARTICULO, "computadora");
        Item pc2 = new Item(50050, ETipoItem.ARTICULO, "computadora");
        Item escritorio = new Item(456454, ETipoItem.ARTICULO, "Escritorio");
        Item impresora = new Item(490, ETipoItem.ARTICULO, "Impresora");
        Item router = new Item(550, ETipoItem.ARTICULO, "Router");

        items1.add(resma);
        items2.add(flete);
        items3.add(tinta);
        items4.add(pc1);
        items5.add(escritorio);
        items5.add(pc2);
        items6.add(impresora);
        items7.add(router);

        //Efectivo pesos = new Efectivo(200000, "Rapipago", "Efectivo");
        Efectivo pesos1 = new Efectivo(200000,"Rapipago", "Efectivo", "ticket");
        Efectivo pesos2 = new Efectivo(200000,"Rapipago", "Efectivo", "ticket");
        Efectivo pesos3 = new Efectivo(200000,"Rapipago", "Efectivo", "ticket");
        Efectivo pesos4 = new Efectivo(200000,"Rapipago", "Efectivo", "ticket");
        Efectivo pesos5 = new Efectivo(200000,"Rapipago", "Efectivo", "ticket");
        Efectivo pesos6 = new Efectivo(200000,"Rapipago", "Efectivo", "ticket");
        Efectivo pesos7 = new Efectivo(200000,"Rapipago", "Efectivo", "ticket");
        DocumentoComercial documento1 = new DocumentoComercial(ETipoDoc.FACTURA, 2000);
        DocumentoComercial documento2 = new DocumentoComercial(ETipoDoc.FACTURA, 2000);
        DocumentoComercial documento3 = new DocumentoComercial(ETipoDoc.FACTURA, 2000);
        DocumentoComercial documento4 = new DocumentoComercial(ETipoDoc.FACTURA, 2000);
        DocumentoComercial documento5 = new DocumentoComercial(ETipoDoc.FACTURA, 2000);
        DocumentoComercial documento6 = new DocumentoComercial(ETipoDoc.FACTURA, 2000);
        DocumentoComercial documento7 = new DocumentoComercial(ETipoDoc.FACTURA, 2000);

        Date fecha = new Date();

        EntityManager em = getEntityManager();
        RepoEntidadesJuridicas repoEntidadesJuridicas = new RepoEntidadesJuridicas(em);

        EntidadOperacion origen1 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Origen 1").getEntidadOperacion();
        EntidadOperacion origen2 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Origen 2").getEntidadOperacion();
        EntidadOperacion origen3 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Origen 3").getEntidadOperacion();
        EntidadOperacion origen4 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Origen 4").getEntidadOperacion();
        EntidadOperacion origen5 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Origen 5").getEntidadOperacion();
        EntidadOperacion origen6 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Origen 6").getEntidadOperacion();
        EntidadOperacion origen7 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Origen 7").getEntidadOperacion();


        EntidadOperacion destino1 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Destino 1").getEntidadOperacion();
        EntidadOperacion destino2 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Destino 2").getEntidadOperacion();
        EntidadOperacion destino3 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Destino 3").getEntidadOperacion();
        EntidadOperacion destino4 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Destino 4").getEntidadOperacion();
        EntidadOperacion destino5 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Destino 5").getEntidadOperacion();
        EntidadOperacion destino6 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Destino 6").getEntidadOperacion();
        EntidadOperacion destino7 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Destino 7").getEntidadOperacion();

        int presupuestosNecesarios = 2;

        OperacionEgreso egreso1 = builderEgreso.agregarItems(items1)
                .agregarMedioDePago(pesos1)
                .agregarDocComercial(documento1)
                .agregarFecha(fecha)
                .agregarEntidadOrigen(origen1)
                .agregarEntidadDestino(destino1)
                .agregarPresupuestosNecesarios(presupuestosNecesarios).build();

        OperacionEgreso egreso2 = builderEgreso.agregarItems(items2)
                .agregarMedioDePago(pesos2)
                .agregarDocComercial(documento2)
                .agregarFecha(fecha)
                .agregarEntidadOrigen(origen2)
                .agregarEntidadDestino(destino2)
                .agregarPresupuestosNecesarios(presupuestosNecesarios).build();

        OperacionEgreso egreso3 = builderEgreso.agregarItems(items3)
                .agregarMedioDePago(pesos3)
                .agregarDocComercial(documento3)
                .agregarFecha(fecha)
                .agregarEntidadOrigen(origen3)
                .agregarEntidadDestino(destino3)
                .agregarPresupuestosNecesarios(presupuestosNecesarios).build();

        OperacionEgreso egreso4 = builderEgreso.agregarItems(items4)
                .agregarMedioDePago(pesos4)
                .agregarDocComercial(documento4)
                .agregarFecha(fecha)
                .agregarEntidadOrigen(origen4)
                .agregarEntidadDestino(destino4)
                .agregarPresupuestosNecesarios(presupuestosNecesarios).build();

        OperacionEgreso egreso5 = builderEgreso.agregarItems(items5)
                .agregarMedioDePago(pesos5)
                .agregarDocComercial(documento5)
                .agregarFecha(fecha)
                .agregarEntidadOrigen(origen5)
                .agregarEntidadDestino(destino5)
                .agregarPresupuestosNecesarios(presupuestosNecesarios).build();

        OperacionEgreso egreso6 = builderEgreso.agregarItems(items6)
                .agregarMedioDePago(pesos6)
                .agregarDocComercial(documento6)
                .agregarFecha(fecha)
                .agregarEntidadOrigen(destino6)
                .agregarEntidadDestino(origen6)
                .agregarPresupuestosNecesarios(presupuestosNecesarios).build();

        OperacionEgreso egreso7 = builderEgreso.agregarItems(items7)
                .agregarMedioDePago(pesos7)
                .agregarDocComercial(documento7)
                .agregarFecha(fecha)
                .agregarEntidadOrigen(destino7)
                .agregarEntidadDestino(origen7)
                .agregarPresupuestosNecesarios(presupuestosNecesarios).build();


        RepoOperacionesEgreso repoEgresos = new RepoOperacionesEgreso(em);
        em.getTransaction().begin();
        repoEgresos.agregarOperacionEgreso(egreso1, "ServerDataMock", getDatastore());
        repoEgresos.agregarOperacionEgreso(egreso2, "ServerDataMock", getDatastore());
        repoEgresos.agregarOperacionEgreso(egreso3, "ServerDataMock", getDatastore());
        repoEgresos.agregarOperacionEgreso(egreso4, "ServerDataMock", getDatastore());
        repoEgresos.agregarOperacionEgreso(egreso5, "ServerDataMock", getDatastore());
        repoEgresos.agregarOperacionEgreso(egreso6, "ServerDataMock", getDatastore());
        repoEgresos.agregarOperacionEgreso(egreso7, "ServerDataMock", getDatastore());
        em.getTransaction().commit();

        cargarPresupuestos(egreso1, egreso5);
    }

    private static void cargarPresupuestos(OperacionEgreso egreso1, OperacionEgreso egreso5) {

        Licitacion licitacion1;
        Licitacion licitacion2;
        Presupuesto presup1;
        Presupuesto presup2;
        Presupuesto presup3;
        EntidadOperacion proveedor1;

        EntityManager entityManager = getEntityManager();
        RepoLicitaciones repoLicitaciones = new RepoLicitaciones(entityManager);

        ArrayList<Item> listaItems = new ArrayList<>();
        listaItems.add(new Item(50, ETipoItem.ARTICULO, "Item1"));
        listaItems.add(new Item(100, ETipoItem.ARTICULO, "Item2"));

        licitacion1 = new Licitacion(egreso1, NotificadorSuscriptores.getInstance());
        licitacion2 = new Licitacion(egreso5, NotificadorSuscriptores.getInstance());

        licitacion1.agregarCriterioSeleccionDeProveedor(new CriterioMenorPrecio());
        licitacion2.agregarCriterioSeleccionDeProveedor(new CriterioMenorPrecio());

        ArrayList<Item> listaItems1 = new ArrayList<>();
        listaItems1.add(new Item(350, ETipoItem.ARTICULO, "Resma de hojas"));

        ArrayList<Item> listaItems2 = new ArrayList<>();
        listaItems2.add(new Item(45032, ETipoItem.ARTICULO, "Escritorio"));
        listaItems2.add(new Item(65000, ETipoItem.ARTICULO, "computadora"));

        ArrayList<Item> listaItems3 = new ArrayList<>();
        listaItems3.add(new Item(456454, ETipoItem.ARTICULO, "Escritorio"));
        listaItems3.add(new Item(50000, ETipoItem.ARTICULO, "computadora"));

        RepoEntidadesJuridicas repoEntidadesJuridicas = new RepoEntidadesJuridicas(entityManager);

        proveedor1 = repoEntidadesJuridicas.buscarEntidadJuridica("Empresa Origen 1").getEntidadOperacion();


        presup1 = new Presupuesto(proveedor1, listaItems1);
        presup2 = new Presupuesto(proveedor1, listaItems2);
        presup3 = new Presupuesto(proveedor1, listaItems3);

        entityManager.getTransaction().begin();
        licitacion1.agregarPresupuesto(presup1, "ServerDataMock", getDatastore());
        licitacion2.agregarPresupuesto(presup2, "ServerDataMock", getDatastore());
        licitacion2.agregarPresupuesto(presup3, "ServerDataMock", getDatastore());

        repoLicitaciones.agregarLicitacion(licitacion1);
        repoLicitaciones.agregarLicitacion(licitacion2);
        entityManager.getTransaction().commit();
    }

    private static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private static Datastore getDatastore(){
        return datastore;
    }
}