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
        _ISBN=ISBN;
        _Title=Title;
        _Author=Author;
    }

}
