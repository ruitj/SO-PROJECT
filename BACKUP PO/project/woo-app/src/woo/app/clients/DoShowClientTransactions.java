package woo.app.clients;
import woo.app.clients.Message;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.Storefront;
import woo.app.exceptions.UnknownClientKeyException;
import woo.exceptions.*;
/**
 * Show all transactions for a specific client.
 */
public class DoShowClientTransactions extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> client;
  public DoShowClientTransactions(Storefront storefront) {
    super(Label.SHOW_CLIENT_TRANSACTIONS, storefront);
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
        _display.addLine(_receiver.show_client_transactions(client.value()));
        _display.display();
      }
        catch(UnknownClientException e){
          throw new UnknownClientKeyException(client.value());
        }
  }
  }


