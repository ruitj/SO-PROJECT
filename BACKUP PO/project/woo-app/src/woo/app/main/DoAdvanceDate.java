package woo.app.main;
import woo.Store;
import pt.tecnico.po.ui.*;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import woo.app.exceptions.InvalidDateException;
/**
 * Advance current date.
 */
public class DoAdvanceDate extends Command<Storefront> {
  
  //FIXME add input fields
  Input<Integer> _days;
  public DoAdvanceDate(Storefront receiver) {
    super(Label.ADVANCE_DATE, receiver);
    //FIXME init input fields
    _days  = _form.addIntegerInput(Message.requestDaysToAdvance());
  }

  @Override
  public final void execute() throws DialogException{
    //FIXME implement command
    _form.parse();
    if(_days.value()<0){
      throw new InvalidDateException(_days.value());
    }
    else
    _receiver.advance_Date(_days.value());
    }
  }
