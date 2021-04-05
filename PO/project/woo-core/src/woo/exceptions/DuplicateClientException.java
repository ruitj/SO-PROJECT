package woo.exceptions;

/**
 * Exception for unknown import file entries.
 */
public class DuplicateClientException extends Exception {

	private static final long serialVersionUID = 1L;
/** Bad bad entry specification. */

  public DuplicateClientException(String id) {
    super(id);
  }
  public DuplicateClientException() {
  }
}