package woo.app.suppliers;
import woo.exceptions.*;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.Storefront;
import  woo.app.*;
import woo.app.exceptions.*;
/**
 * Enable/disable supplier transactions.
 */
public class DoToggleTransactions extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> supp_key;
  public DoToggleTransactions(Storefront receiver) {
    super(Label.TOGGLE_TRANSACTIONS, receiver);
    //FIXME init input fields
    supp_key=_form.addStringInput(Message.requestSupplierKey());
  }

  @Override
  public void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    try{
    _receiver.supplier_exists(supp_key.value()) ;
    if(_receiver.ON_OFF_trns(supp_key.value())) {
        _display.addLine(Message.transactionsOn(supp_key.value()));
        _display.display();
    }
    else{
        _display.addLine(Message.transactionsOff(supp_key.value()));
        _display.display();
        }
    }
    catch(UnknownSupplierException e){
            throw new UnknownSupplierKeyException(supp_key.value());
        }
  }
}
