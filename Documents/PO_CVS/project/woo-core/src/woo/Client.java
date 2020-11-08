package woo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import woo.exceptions.DuplicateClientKeyException;

public class Client implements Serializable {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String _key;
    private String _name;
    private String _address;

    public Client(String name, String key, String address) throws DuplicateClientKeyException{
        _key = key;
        _name = name;
        _address = address;
    }

    public String get_address() {
        return _address;
    }
    /*public viewtransaction(){

    }
    public viewClient(){
    
    }

    public notifiicationAllow(product Product){

    }*/
    /* public Client(String[] init) throws DuplicateHolderException {
        this(Integer.parseInt(init[1]), init[2]);
      }*/
    
    public final String getkey(){
        return _key;
    }
    public String getName(){
        return _name;
    }

    public final void setName(String name){
        _name=name;
    }

    @Override
    public String toString(){
        String str = String.format("%s|%s|%s|NORMAL|0|0", _key,_name,_address);
        return str;
    } 
}


