package woo;
import java.util.TreeMap;
import java.io.Serializable;
import java.util.Map;
//import woo.exceptions.DuplicateProductException;

public class  Order extends Transaction {
    private static final long serialVersionUID = 1L;
    private int  _id;
    private int _payment_date;
    private Map<String,Product>  _products ;
    private Supplier _supp;
    private int a;
    private Product prod;
    private Client client;
    public Order(int id,int price,Supplier supp) {
        super(id,price);
        _id=id;
        _supp=supp;
        _products= new TreeMap<String,Product>();
    }
    public void pay(int day){
        _payment_date=day;
    }
    public int get_id(){
        return _id;
    }
    public String get_client_id(){
        return "";
    }
    public String get_supp_key(){
        return _supp.getkey();
    }
    
    @Override
    public double get_value(){
        return a;
    }
    @Override
    public int get_deadline(){
        return _payment_date;
    }
    @Override
    public Product get_product(){
        return prod;
    }
    @Override
    public Client get_buyer(){
        return client;
    }
    public void  add_order(String key,Product product){
        _products.put(key,product);
    }
    public void calculate(Client client, String Period, int days_dif){
    }
    public boolean payed(){
        return true;
    }
    public double get_price(){
        return 1;
    }
    public String toString(){
        String str = "";
        int base=0;
        for(Map.Entry<String, Product> entry : _products.entrySet()){
            base+=entry.getValue().get_price()*entry.getValue().get_quantity();
        }    
        str=String.format("%d|%s|%d|%d\n", _id,_supp.getkey(),base, _payment_date);
        for(Map.Entry<String, Product> entry : _products.entrySet()) 
            str = str + String.format("%s|%d",entry.getValue().get_product_key(),entry.getValue().get_quantity())+"\n";
        return str;
    }
}