package woo.app.transactions;
import woo.app.exceptions.*;
import woo.exceptions.*;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes

/**
 * Register order.
 */
public class DoRegisterOrderTransaction extends Command<Storefront> {

  //FIXME add input fields
  public DoRegisterOrderTransaction(Storefront receiver) {
    super(Label.REGISTER_ORDER_TRANSACTION, receiver);
    //FIXME init input fields
  }


  @Override
  public final void execute() throws DialogException {
  Input<String> supp_id;
  Input<Integer> amount; 
  Input<String> prod_id;
  Input<Boolean> more;
  try{
    //FIXME implement command
    _form.clear();
    supp_id=_form.addStringInput(Message.requestSupplierKey());
    prod_id=_form.addStringInput(Message.requestProductKey());
    amount=_form.addIntegerInput(Message.requestAmount());
    more=_form.addBooleanInput(Message.requestMore());
    _form.parse();   // maybe false for not adding   more products to the order by default
    _receiver.supplier_exists(supp_id.value());
    _receiver.prod_exists(prod_id.value());
    _receiver.createOrder(false,supp_id.value(),prod_id.value(),amount.value());
    while(more.value()==true){
      _form.clear();
      prod_id=_form.addStringInput(Message.requestProductKey());
      amount=_form.addIntegerInput(Message.requestAmount());
      more=_form.addBooleanInput(Message.requestMore());
      _form.parse(); // maybe true for the opposite
      _receiver.prod_exists(prod_id.value());
      _receiver.createOrder(true,supp_id.value(),prod_id.value(),amount.value());
    }
  }
  catch(UnknownProdException e){
    _receiver.delete_order();
    throw new UnknownProductKeyException(e.getId());
  }
  catch (UnknownSupplierException e){
    _receiver.delete_order();
    throw new UnknownSupplierKeyException(e.getId());
    } 
  }

  }

