package woo;

import java.io.Serializable;
//import woo.exceptions.DuplicateProductException;
import java.util.ArrayList;
public class  Product extends Observable  implements Serializable{

    private static final long serialVersionUID = 1L;
    private ArrayList<Observer> _observers = new ArrayList<Observer>();
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
    public void registerObserver(Observer o) { 
        _observers.add(o); }
    public boolean Is_Observer(Observer o){
        int i = _observers.indexOf(o);
        if (i >= 0)
            return true;
        else
            return false;
    }
    public void removeObserver(Observer o) {
      int i = _observers.indexOf(o);
      if (i >= 0) { _observers.remove(i); }
    }
          
    public void notifyObservers(Product product,String notf) {
      for (Observer observer: _observers) {
            observer.update(product,notf);
      }
    }
    public void changes_applied(Product product,String notf){
        notifyObservers(product,notf);
    }

    public String get_product_key(){
        return _product_key;
    }
    public int get_price(){
        return _price;
    }
    public void remove_stock(int amount){
    }
    public void change_price(int price){}
    public void add_quantity(int amount){}
    public int get_quantity(){
        return _stock;
    }
    }
