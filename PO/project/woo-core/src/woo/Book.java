package woo;

import java.io.Serializable;

//import woo.exceptions.BoxDuplicateException;

public class Book extends Product {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String _ISBN;
    private String _Title;
    private String _Author;
    
    public Book(int price, int critical_value, int stock, String supp_key,String Title,String Author,String ISBN,String product_key) {
        super(product_key,price,critical_value,stock,supp_key);
        _ISBN = ISBN;
        _Title = Title;
        _Author = Author;
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

    public int get_price(int price) {
        return _price;
    }
    public String get_product_key(){
        return _product_key;
    }
    public int get_quantity(){
        return _stock;
    } 
    
    public int get_price(){
        return _price;
    }
    public int get_critical_value(){
        return _critical_value;
    }
    public void set_price(int price){
        _price=price;
    }
    public String get_Title() {
        return _Title;
    }

    public String get_Author(){
        return _Author;
    }
    public String get_ISBN(){
        return _ISBN;
    }

    @Override
    public void remove_stock(int amount){
        _stock-=amount;
    }

    public String toString() {
        String str = String.format("BOOK|%s|%s|%d|%s|%s|%s|%s|%s", _product_key, _supp_key, _price, _critical_value, _stock,_Title,_Author,_ISBN);
        return str; 
    }

}
