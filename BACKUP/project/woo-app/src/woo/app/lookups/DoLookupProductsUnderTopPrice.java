package woo.app.lookups;
import woo.Storefront;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes

/**
 * Lookup products cheaper than a given price.
 */
public class DoLookupProductsUnderTopPrice extends Command<Storefront> {

  //FIXME add input fields
  private Input<Integer> price_limit;
  public DoLookupProductsUnderTopPrice(Storefront storefront) {
    super(Label.PRODUCTS_UNDER_PRICE, storefront);
    //FIXME init input fields
    price_limit=_form.addIntegerInput(Message.requestPriceLimit());
  }

  @Override
  public void execute() throws DialogException {
    //FIXME implement command
    _form.parse();
    _display.addLine(_receiver.get_products_under(price_limit.value()));
    _display.display();
  }
}
