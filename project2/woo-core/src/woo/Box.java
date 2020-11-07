package woo;

import java.io.Serializable;

//import woo.exceptions.BoxDuplicateException;

public class Box extends Product {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int _quantity = 0;
    public Box(int price, int critic_value, int existence_value, String supplier) {
        super(price,critic_value,existence_value,supplier);
        _quantity++;
    }

}
