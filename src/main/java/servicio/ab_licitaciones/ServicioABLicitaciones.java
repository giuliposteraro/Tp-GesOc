package servicio.ab_licitaciones;

import dominio.cuentasUsuarios.CuentaUsuario;
import dominio.entidades.Organizacion;
import dominio.licitacion.Licitacion;
import dominio.licitacion.Presupuesto;
import datos.RepoLicitaciones;
import dominio.notificador_suscriptores.NotificadorSuscriptores;
import dominio.operaciones.OperacionEgreso;
import dominio.operaciones.OperacionIngreso;
import seguridad.sesion.exceptions.PermisoDenegadoException;
import datos.RepositorioUsuarios;
import servidor.Router;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ServicioABLicitaciones {

	RepositorioUsuarios repositorioUsuarios;
	RepoLicitaciones repoLicitaciones;

	public ServicioABLicitaciones(EntityManager entityManager){
		repositorioUsuarios = new RepositorioUsuarios(entityManager);
		repoLicitaciones = new RepoLicitaciones(entityManager);
	}

	public Licitacion altaLicitacion(OperacionEgreso operacion, NotificadorSuscriptores notificadorSuscriptores) {
		Licitacion licitacion = new Licitacion(operacion,notificadorSuscriptores);
		return licitacion;
	}

	public void altaPresupuesto(Licitacion licitacion,Presupuesto presupuesto, String nombreUsuario){
		licitacion.agregarPresupuesto(presupuesto, nombreUsuario, Router.getDatastore());
	}

	public void bajaPresupuesto(Licitacion licitacion,Presupuesto presupuesto) {
		licitacion.sacarPresupuesto(presupuesto);
	}

	public void suscribir(Licitacion licitacion,CuentaUsuario cuentaUsuario) throws Exception {
		if(cuentaUsuario.tieneElPrivilegio("PRIVILEGIO_REVISOR")) {
			licitacion.suscribir(cuentaUsuario);
		}
		throw new PermisoDenegadoException("Este Usuario no tiene el permiso de ser revisor");
	}

	public void licitar(Licitacion licitacion,String nombreUsuario) throws Exception {
		CuentaUsuario cuentaUsuario = repositorioUsuarios.buscarUsuario(nombreUsuario);
		if (cuentaUsuario.puedeLicitar()) {
			licitacion.licitar();
		} else {
			throw new Exception("el usuario no tiene permisos para licitar");
		}

	}

	public ArrayList<Licitacion> listarLicitacionesOrg(Organizacion organizacion){
		return new ArrayList<>(this.listarLicitaciones().stream().filter(licitacion -> licitacion.getOperacionEgreso().esDeLaOrganizacion(organizacion)).collect(Collectors.toList()));
	}

	public ArrayList<Licitacion> listarLicitaciones(){
		return repoLicitaciones.getLicitaciones();
	}
}
