package woo.app.products;
import pt.tecnico.po.ui.Command;                                                                                                              
import pt.tecnico.po.ui.DialogException;                                                                                                      
import pt.tecnico.po.ui.Input;                                                                                                                
import woo.Storefront;                                                                                                                        
//FIXME import other classes

/**
 * Change product price.
 */
public class DoChangePrice extends Command<Storefront> {
  private Input<Integer> price;
  private Input<String> key;
  //FIXME add input fields
  
  public DoChangePrice(Storefront receiver) {
    super(Label.CHANGE_PRICE, receiver);
    //FIXME init input fields
    key = _form.addStringInput(Message.requestProductKey());
    price = _form.addIntegerInput(Message.requestPrice());
  }

  @Override
  public final void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    if(price.value()>0)
      _receiver.ChangePrice(key.value(),price.value());
  }
}
