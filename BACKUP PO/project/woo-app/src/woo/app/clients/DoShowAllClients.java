package woo.app.clients;

import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.Store;
import woo.Storefront;
/**
 * Show all clients.
 */
public class DoShowAllClients extends Command<Storefront> {

  //FIXME add input fields

  public DoShowAllClients(Storefront storefront) {
    super(Label.SHOW_ALL_CLIENTS, storefront);
                //FIXME init input fields
  }

  @Override
  public void execute() throws DialogException {
    //FIXME implement command
    _display.addLine(_receiver.showAllClients());
    _display.display();
  }
}
