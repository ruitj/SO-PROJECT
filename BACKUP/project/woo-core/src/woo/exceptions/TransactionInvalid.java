package woo.exceptions;

/** Exception thrown when the requested service does not exist. */
public class TransactionInvalid extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201709021324L;

  /** Service id. */
  private int _id;

  /**
   * @param id
   */
  public TransactionInvalid(int id) {
    _id = id;
  }

  /** @return id */
  public int getId() {
    return _id;
  }

}