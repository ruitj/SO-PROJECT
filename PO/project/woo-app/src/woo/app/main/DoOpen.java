package woo.app.main;

import woo.Storefront;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.Input;
import woo.app.exceptions.FileNotFound;
import pt.tecnico.po.ui.DialogException;
import java.io.FileNotFoundException;
import java.io.IOException;
import woo.app.exceptions.FileOpenFailedException;
import woo.exceptions.UnavailableFileException;
//FIXME import other classes

/**
 * Open existing saved state.
 */
public class DoOpen extends Command<Storefront> {

  //FIXME add input fields

  private Input<String> filename;
  /** @param receiver */
  public DoOpen(Storefront receiver) {
    super(Label.OPEN, receiver);
    filename = _form.addStringInput(Message.openFile());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.load(filename.value());
  }
  catch(UnavailableFileException |ClassNotFoundException| IOException e){
    throw new FileOpenFailedException(filename.value());
  }
  }

}
