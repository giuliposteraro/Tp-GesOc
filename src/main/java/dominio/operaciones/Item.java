package dominio.operaciones;

import javax.persistence.*;

@Entity
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int valor;

	@Enumerated(EnumType.STRING)
	private ETipoItem tipo;

	private String descripcion;

	public Item(int i, ETipoItem articulo, String string) {
		this.valor = i;
		this.tipo = articulo;
		this.descripcion = string;
	}

    public Item() {

    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ETipoItem getTipo() {
		return tipo;
	}

	public void setTipo(ETipoItem tipo) {
		this.tipo = tipo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public String getDescripcion() {
		return descripcion;
	}
	public boolean esValido(OperacionEgreso operacion) {
		return operacion.getItems().stream().anyMatch(i-> i.getDescripcion().equals(this.getDescripcion()));
	}
	public boolean esCorrespondiente(OperacionEgreso operacion) {
		return operacion.getItems().stream().anyMatch(i-> i.getDescripcion().equals(this.getDescripcion()) && i.getValor() == this.getValor());
	}
}