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
    @Override
    public String toString() {
        
        String str = String.format("BOX|%s|%s|%d|%s|%s|%s", _product_key, _supp_key, _price, _critical_value, _stock,_service_type);
        return str;

    }
}
