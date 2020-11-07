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
    
    public Box(String key,String service_type,String supp_key,int price,int critical_value,int stock) {
        super(key,price,critical_value,stock,supp_key);
        
        _service_type = service_type;
        _books = new ArrayList<Book>();
    }

}
