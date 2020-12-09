package woo;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

//import woo.exceptions.BoxDuplicateException;

public class Container extends Product {
    /**
     *
     */
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String _service_level;
    private String _service_type;
    private List<Box> _boxes;
    public Container(String product_key, String service_type, String service_level, String supp_key, int price,
            int critic_value, int stock) {
        super(product_key, price,critic_value, stock, supp_key);
        _stock=stock;
        
        set_service_type(service_type);
        set_service_level(service_level);
        _boxes= new ArrayList<Box>();
    }
    public String get_product_key(){
        return _product_key;
    }
    public void add_quantity(int quantity){
        if((_stock==0)&&(quantity>0)){
            _stock+=quantity;
            changes_applied(this,"quantity");
        }
        _stock+=quantity;
    }
    public void change_price(int price){
        if(_price>price){
            _price=price;
            changes_applied(this,"preço");
        }
        _price=price;
    }
    public String get_service_type() {
        return _service_type;
    }
    public void set_service_type(String _service_type) {
        this._service_type = _service_type;
    }

    public String get_service_level() {
        return _service_level;
    }
    @Override
    public void remove_stock(int amount){
        _stock-=amount;
    }
    public void set_service_level(String _service_level) {
        this._service_level = _service_level;
    }
    @Override
    public int get_quantity(){
        return _stock;
    }
    public int get_price(){
        return _price;
    }
    public String toString() {
        String str = String.format("CONTAINER|%s|%s|%d|%s|%s|%s|%s", _product_key, _supp_key, _price, _critical_value, _stock,_service_type,_service_level);
        return str; 
    }

}
