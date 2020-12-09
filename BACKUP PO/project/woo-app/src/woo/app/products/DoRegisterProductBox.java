package woo.app.products;
import woo.Storefront;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.exceptions.*;
import woo.app.exceptions.*;
/**
 * Register box.
 */
public class DoRegisterProductBox extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> key;
  private Input<String> service_type;
  private Input<String> supp_key;
  private Input<Integer> price;
  private Input<Integer> critic_value;
  private Input<Integer> stock;
  public DoRegisterProductBox(Storefront receiver) {
    super(Label.REGISTER_BOX, receiver);
    //FIXME init input fields
    key = _form.addStringInput(Message.requestProductKey());
    price=_form.addIntegerInput(Message.requestPrice());
    critic_value=_form.addIntegerInput(Message.requestStockCriticalValue());
    supp_key=_form.addStringInput(Message.requestSupplierKey());
    service_type = _form.addStringInput(Message.requestServiceType());
    
  }

  @Override
  public final void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    try{
    _receiver.supplier_exists(supp_key.value());
    _receiver.service_type_valid(service_type.value()); 
    _receiver.addProductBox(key.value(),service_type.value(),supp_key.value(),price.value(),critic_value.value(),0);
    }
    catch(DuplicateProductException e){

    }
    catch(UnknownTypeException e){
      throw new UnknownServiceTypeException(service_type.value());
    }
    catch(UnknownSupplierException e){
      throw new UnknownSupplierKeyException(supp_key.value());
    } 
  }
}