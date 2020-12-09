package woo;
import java.util.Comparator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Supplier  implements  Serializable {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String _key;
    private String _name;
    private String _address;
    private boolean trns_activ=true;
    private String transaction="SIM";
    private boolean changed=false;
    private boolean printed=true;
    private  ArrayList<Transaction> _transactions = new ArrayList<Transaction>();
    public Supplier(String name, String key, String address) {
        _key = key;
        _name = name;
        _address = address;
    }

    public String get_address() {
        return _address;
    }
    public void add_supp_order(Transaction transaction){
        _transactions.add(transaction);
    }
    public String show_transactions(){
        String str="";
        for (int counter = 0; counter < _transactions.size(); counter++)
            str=str+_transactions.get(counter).toString();
        return str;    
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

    public void remove_last_order(){
        for (int counter = 0; counter < _transactions.size(); counter++){
            if(counter==_transactions.size()-1)
                _transactions.remove(counter);
        }
    }


    public boolean has_trns(){
        return trns_activ;
    }
    public void change_trns(boolean transac){
        trns_activ=transac;
        if(trns_activ){
            transaction="SIM";
            changed=true;
            printed=false;
            }
        else {
            transaction="NÃO";
            changed=true;
            printed=false;
            }
        }
    @Override
    public String toString(){
        String str = String.format("%s|%s|%s|%s", _key,_name,_address,transaction);
        return str;
    } 
}
