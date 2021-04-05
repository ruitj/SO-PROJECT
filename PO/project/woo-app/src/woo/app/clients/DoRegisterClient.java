package woo.app.clients;
import woo.Store;
import pt.tecnico.po.ui.Command;
import woo.exceptions.*;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.app.exceptions.DuplicateClientKeyException;
/**
 * Register new client.
 */
public class DoRegisterClient extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> name;
  private Input<String> key;
  private Input<String> address;
  public DoRegisterClient(Storefront storefront) {
    super(Label.REGISTER_CLIENT, storefront);
    //FIXME init input fields
    key=_form.addStringInput(Message.requestClientKey());
    name=_form.addStringInput(Message.requestClientName());
    address=_form.addStringInput(Message.requestClientAddress());
  }

  @Override
  public void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    try{
    _receiver.addClient(true,name.value(),key.value(),address.value());
    }
    catch(DuplicateClientException e){
      throw new DuplicateClientKeyException(key.value());
    }
  }

}