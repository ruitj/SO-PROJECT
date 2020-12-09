package woo;

import java.util.TreeMap;
import java.io.Serializable;
import java.util.Map;
//import woo.exceptions.DuplicateProductException;

public class Sale extends Transaction {
    private static final long serialVersionUID = 1L;
    private int _id;
    private int _payment_date;
    private int _quantity;
    private double _value=0;
    private int _limit;
    private Product _product;
    private Boolean _payed=false;
    private Client _client;
    private boolean calculated=false;

    public Sale(Client client,int id, int quantity, Product product,int limit) {
        super(id, quantity);
        _id=id;
        _product = product;
        _client=client;
        _limit=limit;
        _quantity=quantity;
        set_value(0);
    }
    public double get_price(){
        return _value;
    }
    @Override
    public Product get_product(){
        return _product;
    }
    public Client get_buyer(){
        return _client;
    }
    public void set_value(int value) {
        _value = value;
    }
    public void pay(int day){
        _payment_date=day;
        _payed=true;
    }
    public int get_id(){
        return _id;
    }
    public String get_client_id(){
        return _client.getkey();
    }
    public boolean payed() {
        return _payed;
    }
    public String get_supp_key(){
        return "";
    }
    @Override
    public double get_value(){
        return _value;
    }
    public int get_deadline(){
        return _limit;
    }
    public void add_order(String key,Product product){}
    
    @Override
    public void calculate(Client client, String Period, int days_dif) {
        if (Period.equals("P1")) {
            System.out.println("a");
            _value=(_product.get_price() * _quantity) - ((_product.get_price() * _quantity) * 0.1);
        }
        if (Period.equals("P2")) {
            if (client.getStatus().equals("SELECTION")) {
                if (days_dif <= -2)
                    _value=(_product.get_price() * _quantity) - (_product.get_price() * _quantity) * 0.05;
                else
                    _value= _product.get_price() * _quantity;
            }
            if (client.getStatus().equals("NORMAL"))
                _value = (_product.get_price() * _quantity);
            if (client.getStatus().equals("ELITE"))
                _value=(_product.get_price() * _quantity) - (_product.get_price() * _quantity) * 0.1;
        }
        if (Period.equals("P3")) {
            if (client.getStatus().equals("NORMAL")){
                _value=(_product.get_price() * _quantity) + ((_product.get_price() * _quantity) * 0.05 * days_dif*(-1));
                System.out.println("okiii");
                System.out.println(_value);
                System.out.println(_product.get_price());
                System.out.println(_quantity);
                System.out.println(days_dif);
            }
            if (client.getStatus().equals("SELECTION")) {
                if (days_dif == 1)
                    _value = _product.get_price() * _quantity;
                else
                    _value=_product.get_price() * _quantity + (_product.get_price() * _quantity) * 0.02;
            }
            if (client.getStatus().equals("ELITE"))
                _value=(_product.get_price() * _quantity) - (_product.get_price() * _quantity) * 0.05;
        }
        if (Period.equals("P4")) {
            if (client.getStatus().equals("NORMAL"))
                _value=(_product.get_price() * _quantity) + (_product.get_price() * _quantity) * 0.1;
            if (client.getStatus().equals("SELECTION"))
                _value=(_product.get_price() * _quantity) + (_product.get_price() * _quantity) * 0.05;
            if (client.getStatus().equals("ELITE"))
                _value= (_product.get_price() * _quantity);
            }
            calculated=true;
        }

    @Override
    public String toString() {
        if (_payed) {
            String str = String.format("%d|%s|%s|%d|%d|%d|%d|%d", _id,_client.getkey(), _product.get_product_key(),_product.get_quantity(),_product.get_quantity()*_product.get_price(),(long)_value,_limit,_payment_date);
            return str;
        }
        if(calculated==true){
            String str = String.format("%d|%s|%s|%d|%d|%d|%d", _id,_client.getkey(), _product.get_product_key(),_product.get_quantity(),_product.get_quantity()*_product.get_price(),(long)_value,_limit);
            return str;
        }
        else{
            _value=_product.get_quantity()*_product.get_price();
            String str = String.format("%d|%s|%s|%d|%d|%d|%d", _id,_client.getkey(),_product.get_product_key(), _product.get_quantity(),_product.get_price()*_product.get_quantity(),(long)_value,_limit);
            return str;
        }

    }
}