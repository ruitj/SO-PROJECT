package woo.app.transactions;
import woo.app.exceptions.UnknownClientKeyException;
import woo.app.exceptions.UnknownProductKeyException;
import woo.app.exceptions.UnavailableProductException;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.exceptions.*;
/**
 * Register sale.
 */
public class DoRegisterSaleTransaction extends Command<Storefront> {

  //FIXME add input fields
  private Input<Integer> deadline;
  private Input<String> client_id;
  private Input<String> prod_id;
  private Input<Integer> amount;
  public DoRegisterSaleTransaction(Storefront receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    //FIXME init input fields
    client_id = _form.addStringInput(Message.requestClientKey());
    deadline = _form.addIntegerInput(Message.requestPaymentDeadline());
    prod_id = _form.addStringInput(Message.requestProductKey());
    amount = _form.addIntegerInput(Message.requestAmount());
  }

  @Override
  public final void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    try{
      _receiver.client_exists(client_id.value());
      _receiver.prod_exists(prod_id.value());
      _receiver.add_sale(client_id.value(),deadline.value(),prod_id.value(),amount.value());
    }
    catch(UnknownClientException e){
      throw new UnknownClientKeyException(client_id.value());
    }
    catch(UnknownProdException e){
      throw new UnknownProductKeyException(prod_id.value());
    }
    catch(NotEnoughAmount e){
      throw new UnavailableProductException(prod_id.value(),e.get_required(),e.get_amount());
    }
  }

}
