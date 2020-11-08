package woo;

import java.io.Serializable;

//import woo.exceptions.DuplicateProductException;

public abstract class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    public  int _price;
    public String _product_key;
    public int _critical_value;
    public String _supp_key;
    public int _stock;

    public Product(String product_key,int price, int critical_value, int stock, String supp_key) {
        _price = price;
        _critical_value = critical_value;
        _stock = stock;
        _supp_key = supp_key;
        _product_key = product_key;
    }
    
    public int get_price() {
        return _price;
    }

    public void set_price(int _price) {
        this._price = _price;
    }
    
    public abstract String toString();
}