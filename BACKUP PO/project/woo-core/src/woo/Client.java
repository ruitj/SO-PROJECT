package woo;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class Client extends Observer implements Serializable{
    

    /**
     *
     */
    private static final long serialVersionUID = 2L;
    private String _key;
    private String _name;
    private String _address;
    private double _points=0;
    private double payed=0;
    private String _notification="";
    private boolean printed;
    private boolean _notf;
    private TreeMap<String,Product> _products=new TreeMap<String, Product>();
    private ClientClassification _classification= new NormalClient(this);
    private ArrayList<Product> _sale=new ArrayList<Product>(); 
    public Client(String name, String key, String address){
        _key = key;
        _name = name;
        _address = address;
    }
    public void setClassification(ClientClassification classification){
        _classification=classification;
    }
    public void add_points(double points){
        _points+=points;
    }
    public void set_points(double points){
        _points=points;
    }
    public void add_sale_points(double sale_value){
        _points+=sale_value*10;
        System.out.println(_points);
        if((_points>2000)&&((_classification.get_status()=="NORMAL")))
            _classification.up();
        if(_points>25000) {
            _classification.up();
            }
        }
    public void remove_sale_points(int delayed_days){
        _classification.delayed_payment(delayed_days);
    }
    public void addObserver(Product product){
        product.registerObserver(this);
        _products.put(product.get_product_key(), product);
    }
    public void add_sale_value(double value){
        payed+=value;
    }


	public void update(Product product, String type) {
        Product  new_product = (Product) product;
        String notf=String.valueOf(type);
        if(notf.equals("preço")){
            this.notf_price_change(new_product.get_product_key(),new_product.get_price());  
        }   
        if(notf.equals("quantity")){
            this.notf_amount_change(new_product.get_product_key(),new_product.get_quantity());
        }
    }
    public void notf_price_change(String id,int price){
        _notification += String.format("BARGAIN|%s|%d\n",id,price);
        printed=false;
    }
    public void notf_amount_change(String id,int amount){
        _notification += String.format("NEW|%s|%d\n",id,amount);
        printed=false;
    }
    public double get_points(){
        return _points;
    }
    public String get_address() {
        return _address;
    }
    public void show_notf(boolean show){
        _notf=show;
    }
    public void add_Sale(Product product){
        _sale.add(product);
    }
    
    public final String getkey(){
        return _key;
    }
    public String getName(){
        return _name;
    }
    public String getStatus(){
        return _classification.get_status();
    }
    public final void setName(String name){
        _name=name;
    }

    @Override
    public String toString(){
        String str="";
        int base=0;
        for (int counter = 0; counter < _sale.size(); counter++){
            base+=_sale.get(counter).get_price()*_sale.get(counter).get_quantity();
        }    
        str = str+String.format("%s|%s|%s|%s|%d|%d\n", _key,_name,_address,_classification.get_status(),base,(long)payed);
        if((_notification.length()>0)&&(printed==false)&&(_notf)){
            printed=true;
            str+=_notification;
            _notification="";
        }
        return str;
    }
}



