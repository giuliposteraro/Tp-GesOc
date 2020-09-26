package servidor.controladores;

import dominio.cuentasUsuarios.CuentaUsuario;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import seguridad.sesion.exceptions.CredencialesNoValidasException;
import seguridad.sesion.exceptions.CuentaBloqueadaException;
import servicio.Sesiones.ServicioSesiones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import temporal.dominio.repositorioSesiones.RepositorioSesiones;
import temporal.seguridad.repositorioUsuarios.RepositorioUsuarios;

import java.util.HashMap;
import java.util.Map;

public class LoginController extends Controller{
	
	ServicioSesiones loginService;
	
	public ModelAndView login(Request request, Response response) {
		if (usuarioAutenticado(request))
			response.redirect("/home");

		Map<String, String> parameters = new HashMap<>();

		parameters.put("username", request.cookie("username"));
		parameters.put("password", request.cookie("password"));
		parameters.put("remember-credentials-checked", request.cookie("remember-credentials-checked"));
		parameters.put("CredencialesNoValidas", request.cookie("CredencialesNoValidas"));
		parameters.put("CuentaBloqueada", request.cookie("CuentaBloqueada"));


		return new ModelAndView(parameters, "login.hbs");
	}

	public Response loguear(Request request, Response response) throws CredencialesNoValidasException, CuentaBloqueadaException {
		String password = request.queryParams("password");
		String username = request.queryParams("username");

		if(request.queryParams("remember-credentials") != null){
			response.cookie("password", password);
			response.cookie("username", username);
			response.cookie("remember-credentials-checked", "true");
		}
		else{
			response.removeCookie("password");
			response.removeCookie("username");
			response.removeCookie("remember-credentials-checked");
		}


		if(RepositorioUsuarios.getInstance().existeElUsuario(username)){
			CuentaUsuario usuario = RepositorioUsuarios.getInstance().buscarUsuario(username);

			if(!usuario.estaBloqueada()) {
				if(usuario.verificarContrasenia(password)) {
					//identificadorSesion = RepositorioSesiones.getInstance().logInSesion(cuentaDeUsuario);
					request.session(true);
					request.session().attribute("user", usuario);
					response.removeCookie("CredencialesNoValidas");
					response.removeCookie("CuentaBloqueada");
				}
				else {
					//throw new CredencialesNoValidasException();
					response.cookie("CredencialesNoValidas", "true");
				}
			}
			else {
				//throw new CuentaBloqueadaException();
				response.cookie("CuentaBloqueada", "true");
			}
		}
		else{
			response.cookie("CredencialesNoValidas", "true");
		}

		response.redirect("/home", 302);

		return response;
	}

	private boolean usuarioAutenticado(Request request) {
		return request.session().attribute("user") != null;
	}
	
	public Response logout(Request request, Response response) {
		request.session().removeAttribute("user");
		response.redirect("/login");
		return response;
	}
}
