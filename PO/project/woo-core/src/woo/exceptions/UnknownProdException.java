package woo.exceptions;

/** Exception thrown when the requested service does not exist. */
public class UnknownProdException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201709021324L;

  /** Service id. */
  private String _id;

  /**
   * @param id
   */
  public UnknownProdException(String id) {
    _id = id;
  }

  /** @return id */
  public String getId() {
    return _id;
  }

}