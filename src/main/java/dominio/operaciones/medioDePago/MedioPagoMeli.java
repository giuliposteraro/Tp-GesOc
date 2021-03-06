package dominio.operaciones.medioDePago;

public class MedioPagoMeli {
    private String id;
    private String name;
    private String thumbnail;
    private String payment_type_id;


    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getPayment_type_id() { return payment_type_id; }
    public void getPayment_type_id(String value) { this.payment_type_id = value; }

    public boolean getEsTarjetaDeCredito(){
        return this.payment_type_id.equals("credit_card");
    }

    public boolean getEsTarjetaDeDebito(){
        return this.payment_type_id.equals("debit_card");
    }

    public boolean getEsImagenGrande(){
        return this.id.equals("mercadopago_cc");
    }

    public boolean getEsEfectivo(){
        return this.payment_type_id.equals("ticket");
    }

    public boolean getEsDineroEnCuenta(){
        return this.payment_type_id.equals("account_money");
    }


    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String value) { this.thumbnail = value; }
}