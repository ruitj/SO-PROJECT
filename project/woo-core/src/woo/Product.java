package woo;

import java.io.Serializable;

//import woo.exceptions.DuplicateProductException;

public abstract class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    private int _price;
    private int _critic_value;
    private int _existence_value;
    private String _supplier;
    private String _name;

    public Product(int price, int critic_value, int existence_value, String supplier, String name) {
        _price = price;
        _critic_value = critic_value;
        _existence_value = existence_value;
        _supplier = supplier;
        set_name(name);
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_price() {
        return _price;
    }

    public String get_supplier() {
        return _supplier;
    }

    public void set_supplier(String _supplier) {
        this._supplier = _supplier;
    }

    public int get_existence_value() {
        return _existence_value;
    }

    public void set_existence_value(int _existence_value) {
        this._existence_value = _existence_value;
    }

    public Product(int price,int critic_value,int existence_value,String supplier){
      
    }

    public int get_critic_value() {
        return _critic_value;
    }

    public void set_critic_value(int _critic_value) {
        this._critic_value = _critic_value;
    }

    public void set_price(int _price) {
        this._price = _price;
    }
    
}