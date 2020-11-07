package woo.app.main;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import java.io.FileNotFoundException;
import java.io.IOException;
//FIXME import other classes

/**
 * Open existing saved state.
 */
public class DoOpen extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> filename;
  public DoOpen(Storefront receiver) {
    super(Label.OPEN, receiver);
    filename = _form.addStringInput(Message.openFile());
  }

      @Override
      public final void execute() {
        try {
            _form.parse();
            _receiver.load(filename.value());
        }
        catch (FileNotFoundException e) {
            _display.popup(Message.fileNotFound());
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
      }
  }

