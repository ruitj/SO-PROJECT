package woo;
import java.util.TreeMap;
import java.io.Serializable;
import java.util.Map;
//import woo.exceptions.DuplicateProductException;

public abstract class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;
    public int  _id;
    public int _quantity;

    public Transaction(int id,int quantity) {
        _id=id;
        _quantity=quantity;
    }
    
    public  abstract double get_price();
    public abstract void  add_order(String key,Product product);
    public abstract void calculate(Client client, String Period, int days_dif);
    public abstract void pay(int day);
    //public abstract int get_deadline();
    public  abstract boolean payed();
    public abstract String toString();
    public abstract Product get_product();
    public abstract Client get_buyer();
    public abstract int get_deadline();
    public abstract double get_value();
    public abstract int get_id();
    public abstract String get_client_id();
    public abstract String get_supp_key();
    //public abstract String get_type();
}