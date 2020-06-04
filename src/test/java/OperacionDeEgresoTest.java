import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import dominio.operaciones.DocumentoComercial;
import dominio.operaciones.ETipoDoc;
import dominio.operaciones.ETipoItem;
import dominio.operaciones.Efectivo;
import dominio.operaciones.EntidadOperacion;
import dominio.operaciones.Item;
import dominio.operaciones.OperacionEgresoBuilder;

public class OperacionDeEgresoTest {

	Item computadora;
	Item flete;
	Efectivo pesos;
	DocumentoComercial documento;
	Date fecha;
	double valorOp;
	EntidadOperacion origen;
	EntidadOperacion destino;
	ArrayList<Item> items;
	OperacionEgresoBuilder builder;
	
	@Before
	public void init() {
		items = new ArrayList<Item>();
		computadora = new Item(100000,ETipoItem.ARTICULO,"Notebook marca ASUS");
		flete = new Item(500, ETipoItem.SERVICIO, "Servicio de transporte de productos");
		items.add(flete);
		items.add(computadora);
		pesos = new Efectivo(200000,"Rapipago");
		documento = new DocumentoComercial(ETipoDoc.FACTURA, 2000);
		fecha = new Date();
		valorOp = 100500;
		origen = new EntidadOperacion("Operacion compra 1","20-40678950-4","Av.Libertador 800");
		destino = new EntidadOperacion("Operacion compra 1", "20-40678950-4", "Av.Corrientes 550");
		builder = new OperacionEgresoBuilder();
	}
	
	@Test
	
	public void crearOperacionEgreso() {
		builder.setItems(items);
		builder.setDocumento(documento);
		builder.setEntidadDestino(destino);
		builder.setEntidadOrigen(origen);
		builder.setFecha(fecha);
		builder.setMedioDePago(pesos);
		builder.setValorOperacion(valorOp);
		
		assertNotNull(builder.build());
	}
	
}
