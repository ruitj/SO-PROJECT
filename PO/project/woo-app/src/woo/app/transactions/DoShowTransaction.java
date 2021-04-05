package woo.app.transactions;
import woo.Storefront;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.app.exceptions.UnknownTransactionKeyException;
import woo.exceptions.*;
/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<Storefront> {

  //FIXME add input fields
  private Input<Integer> id;
  public DoShowTransaction(Storefront receiver) {
    super(Label.SHOW_TRANSACTION, receiver);
    //FIXME init input fields
    id=_form.addIntegerInput(Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws DialogException {
    //FIXME implememt command
    _form.parse();
    try {
      _receiver.transaction_real(id.value());
      _display.addLine(_receiver.show_transaction(id.value()));
      _display.display();
    }
    catch(UnknownTrnsException e){
      throw new UnknownTransactionKeyException(id.value());
    }
  }

}
