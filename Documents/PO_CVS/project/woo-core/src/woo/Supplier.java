package woo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import woo.exceptions.DuplicateClientKeyException;

public class Supplier implements Serializable {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String _key;
    private String _name;
    private String _address;
    private String _is_active;

    public Supplier(String name, String key, String address) throws DuplicateClientKeyException {
        _key = key;
        _name = name;
        _address = address;
    }

    public String get_address() {
        return _address;
    }
    
    public final String getkey(){
        return _key;
    }
    public String getName(){
        return _name;
    }

    public final void setName(String name){
        _name = name;
    }

    @Override
    public String toString(){
        String str = String.format("%s|%s|%s|SIM", _name,_key,_address);
        return str;
    } 
}
