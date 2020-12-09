package woo.app.clients;
import woo.Storefront;
import woo.app.clients.Message;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.exceptions.BadEntryException;
import woo.app.exceptions.UnknownClientKeyException;
import woo.exceptions.*;
import pt.tecnico.po.ui.DialogException;                                                                                                             import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes

/**
 * Show client.
 */
public class DoShowClient extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> id;
  private Input<String> name;
  private Input<String> address;
  public DoShowClient(Storefront storefront) {
    super(Label.SHOW_CLIENT, storefront);
    //FIXME init input fields
    id=_form.addStringInput(Message.requestClientKey());
  }

  @Override
  public void execute() throws DialogException {
    //FIXME implement command
        _form.parse();
        try
        {
          _display.addLine(_receiver.showClient(id.value()));
          _display.display();
        }
        catch(UnknownClientException e){
          throw new UnknownClientKeyException(id.value());
        }
    }
}
