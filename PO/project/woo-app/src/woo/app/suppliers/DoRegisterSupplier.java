package woo.app.suppliers;
import woo.Storefront;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.Store;
import woo.exceptions.*;
import woo.app.exceptions.DuplicateSupplierKeyException;
/**
 * Register supplier.
 */
public class DoRegisterSupplier extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> name;
  private Input<String> key;
  private Input<String> address;
  public DoRegisterSupplier(Storefront receiver) {
    super(Label.REGISTER_SUPPLIER, receiver);
    //FIXME init input fields
    key = _form.addStringInput(Message.requestSupplierKey());
    name=_form.addStringInput(Message.requestSupplierName());
    address = _form.addStringInput(Message.requestSupplierAddress());
  }

  @Override
  public void execute() throws DialogException{
    //FIXME implement command
    _form.parse();
    try {
    _receiver.addSupplier(name.value(),key.value(),address.value());
    }
    catch (DuplicateSupplierException e){
      throw new DuplicateSupplierKeyException(key.value());
    }
}

}
