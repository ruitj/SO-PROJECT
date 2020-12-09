package woo.exceptions;


/**
 * This exception represents a duplicate holder problem.
 */
public class DuplicateProductException extends ProductException{

	/**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param id
     * @param name
     */
	public DuplicateProductException(int price,int critic_value,int existence_value,String supplier,String name) {
		super(price,critic_value,existence_value,supplier,name);
	}
}
