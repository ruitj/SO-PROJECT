package woo.exceptions;

/**
 * Exception for unknown import file entries.
 */
public class DuplicateSupplierException extends Exception {

	private static final long serialVersionUID = 1L;
/** Bad bad entry specification. */

  public DuplicateSupplierException(String id) {
    super(id);
  }
  public DuplicateSupplierException() {
  }
}