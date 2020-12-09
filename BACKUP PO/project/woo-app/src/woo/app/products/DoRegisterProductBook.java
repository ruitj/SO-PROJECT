package woo.app.products;
import woo.exceptions.*;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.app.exceptions.*;
/**
 * Register book.
 */
public class DoRegisterProductBook extends Command<Storefront> {

  //FIXME add input fields
  private Input<Integer> price;
  private Input<Integer> critic_value;
  private Integer stock;
  private Input<String> supp_key;
  private Input<String> Title;
  private Input<String> Author;
  private Input<String> ISBN;
  private Input<String> key;
  public DoRegisterProductBook(Storefront receiver) {
    super(Label.REGISTER_BOOK, receiver);
    //FIXME init input fields
    key = _form.addStringInput(Message.requestProductKey());
    Title = _form.addStringInput(Message.requestBookTitle());
    Author = _form.addStringInput(Message.requestBookAuthor());
    ISBN = _form.addStringInput(Message.requestISBN());
    price = _form.addIntegerInput(Message.requestPrice());
    critic_value = _form.addIntegerInput(Message.requestStockCriticalValue());
    supp_key = _form.addStringInput(Message.requestSupplierKey());
    stock = 0;
  }

  @Override
  public final void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    try{
      _receiver.supplier_exists(supp_key.value());
    _receiver.addProductBook(price.value(),critic_value.value(),0,supp_key.value(),Title.value(),Author.value(),ISBN.value(),key.value());
    }
    catch(DuplicateProductException e){
      
    }
    catch(UnknownSupplierException e){
      throw new UnknownSupplierKeyException(supp_key.value());
    }
  }
}
