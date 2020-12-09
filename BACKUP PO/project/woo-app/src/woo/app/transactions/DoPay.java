package woo.app.transactions;
import woo.Storefront;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.app.exceptions.*;
import woo.exceptions.*;
/**
 * Pay transaction (sale).
 */
public class DoPay extends Command<Storefront> {

  //FIXME add input fields
  private Input<Integer> id;
  public DoPay(Storefront storefront) {
    super(Label.PAY, storefront);
    //FIXME init input fields
    id=_form.addIntegerInput(Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    try{
    _receiver.search_transactions(id.value());
    _receiver.pay_sale(id.value());
    }
    catch(TransactionInvalid e){
      throw new UnknownTransactionKeyException(e.getId());
    }
  }

}
