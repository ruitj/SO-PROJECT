package woo;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

//import woo.exceptions.BoxDuplicateException;

public class Box extends Product {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String _service_type;
    private List<Book> _books;
    
    public Box(String product_key,String service_type,String supp_key,int price,int critical_value,int stock) {
        super(product_key,price,critical_value,stock,supp_key);
        _books = new ArrayList<Book>();
        _service_type=service_type;
    }
    public int get_quantity(){
        return _stock;
    }
    public void set_price(int price){
        _price=price;
    }
    @Override
    public String get_product_key(){
        return _product_key;
    }
    public int get_price(){
        return _price;
    }
    @Override
    public void remove_stock(int amount){
        _stock-=amount;
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
    public String toString() {
        
        String str = String.format("BOX|%s|%s|%d|%d|%d|%s", _product_key, _supp_key, _price, _critical_value, _stock,_service_type);
        return str;

    }
}
