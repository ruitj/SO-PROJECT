package woo.exceptions;

/**
 * Exception for unknown import file entries.
 */
public class DuplicateClientKeyException extends ClientException {

	private static final long serialVersionUID = 1L;
/** Bad bad entry specification. */

  public DuplicateClientKeyException(String id, String name) {
    super(id, name);
  }
}