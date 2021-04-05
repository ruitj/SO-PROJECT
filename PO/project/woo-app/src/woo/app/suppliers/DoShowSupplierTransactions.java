package woo.app.suppliers;
import woo.exceptions.*;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.app.exceptions.*;
import woo.Storefront;
/**
 * Show all transactions for specific supplier.
 */
public class DoShowSupplierTransactions extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> supp_key;
  public DoShowSupplierTransactions(Storefront receiver) {
    super(Label.SHOW_SUPPLIER_TRANSACTIONS, receiver);
    //FIXME init input fields
    supp_key=_form.addStringInput(Message.requestSupplierKey());
  }

  @Override
  public void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    try{

    _display.addLine(_receiver.show_supp_orders(supp_key.value()));
    _display.display();
    }
    catch(UnknownSupplierException e){
        throw new UnknownSupplierKeyException(supp_key.value());
        }
    }

}
