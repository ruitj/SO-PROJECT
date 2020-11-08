package woo.exceptions;

/**
 * Exception for unknown import file entries.
 */
public class NoSuchClientIdException extends Exception {

    private static final long serialVersionUID = 1L;
    private final String _id;

    public NoSuchClientIdException(String id) {
        _id=id;
    }

    public String get_id() {
        return _id;
    }
  }