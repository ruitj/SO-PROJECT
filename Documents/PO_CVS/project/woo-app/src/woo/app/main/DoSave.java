package woo.app.main;
import java.io.*;
import woo.Storefront;
import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes
import pt.tecnico.po.ui.Input;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * Save current state to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> filename;

  public DoSave(Storefront receiver) {
    super(Label.SAVE, receiver);
    //FIXME init input fields
    if (_receiver.getFileName() == "")
        filename = _form.addStringInput(Message.newSaveAs());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    //FIXME implement command
    try {
      if(_receiver.getFileName() == ""){
      _form.parse();  
      _receiver.setFileName(filename.value());  
      _receiver.save(filename.value());
        }
      _receiver.save(_receiver.getFileName());
    }
  catch (IOException e) {
      e.printStackTrace();
    }
}
}
