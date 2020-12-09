package woo.app.lookups;
import woo.exceptions.*;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.app.exceptions.UnknownClientKeyException;
/**
 * Lookup payments by given client.
 */
public class DoLookupPaymentsByClient extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> client;
  public DoLookupPaymentsByClient(Storefront storefront) {
    super(Label.PAID_BY_CLIENT, storefront);
    //FIXME init input fields
    client=_form.addStringInput(Message.requestClientKey());
  }

  @Override
  public void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    try
        {
        _receiver.search_client(client.value());
        }
        catch(UnknownClientException e){
          throw new UnknownClientKeyException(client.value());
        }
  }

}
