package woo.exceptions;

/**
 * Exception for unknown import file entries.
 */
public class ProductException extends Exception {

    private static final long serialVersionUID = 1L;
    private int _price;
    private int _critic_value;
    private int _existence_value;
    private String _supplier;
    private String _name;

    public ProductException(int price, int critic_value, int existence_value, String supplier, String name) {
        set_price(price);
        set_critic_value(critic_value);
        set_existence_value(existence_value);
        set_supplier(supplier);
        set_name(name);
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
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

    public int get_critic_value() {
        return _critic_value;
    }

    public void set_critic_value(int _critic_value) {
        this._critic_value = _critic_value;
    }

    public int get_price() {
        return _price;
    }

    public void set_price(int _price) {
        this._price = _price;
    }
}