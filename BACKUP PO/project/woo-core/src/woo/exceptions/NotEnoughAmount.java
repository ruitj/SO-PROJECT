package woo.exceptions;

/** Exception thrown when the requested service does not exist. */
public class NotEnoughAmount extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201709021324L;

  /** Service id. */
    private int _amount;
    private int _required;
  /**
   * @param id
   */
  public NotEnoughAmount(int amount,int required) {
      _amount=amount;
      _required=required;
  }
  public int get_amount(){
      return _amount;
  }
  public int get_required(){
      return _required;
  }

  /** @return id */

}