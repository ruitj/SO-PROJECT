package woo.app.clients;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import woo.Storefront;                                                                                                                        
//FIXME import other classes

/**
 * Register new client.
 */
public class DoRegisterClient extends Command<Storefront> {

  //FIXME add input fields
 
  // transactions
  public DoRegisterClient(Storefront storefront) {
    super(Label.REGISTER_CLIENT, storefront);
  }

  public void execute() throws DialogException {
    //FIXME implement command
  }

}
