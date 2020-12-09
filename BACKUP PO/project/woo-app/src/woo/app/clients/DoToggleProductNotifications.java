package woo.app.clients;
import woo.Storefront;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.app.clients.Message;
/**
 * Toggle product-related notifications.
 */
public class DoToggleProductNotifications extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> prod_id;
  private Input<String> client_id;
  private boolean notf;
  public DoToggleProductNotifications(Storefront storefront) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, storefront);
    //FIXME init input fields
    prod_id=_form.addStringInput(Message.requestProductKey());
    client_id=_form.addStringInput(Message.requestClientName());
  }

  @Override
  public void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    if(_receiver.ON_OFF_notf(client_id.value(),prod_id.value())){
        _display.addLine(Message.notificationsOn(client_id.value(),prod_id.value()));
        _display.display();
    }
    else{
        _display.addLine(Message.notificationsOff(client_id.value(),prod_id.value()));
        _display.display();
        }
      }

}
