package woo.app.main;

import java.io.IOException;

import pt.tecnico.po.ui.Command;                                                                                                              import pt.tecnico.po.ui.DialogException;                                                                                                      import pt.tecnico.po.ui.Input;                                                                                                                import woo.Storefront;                                                                                                                        //FIXME import other classes

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<Storefront> {

  //FIXME add input fields
  private Input<String> filename;
  /** @param receiver */
  public DoSave(Storefront receiver) {
    super(Label.SAVE, receiver);
    //FIXME init input fields
    if (_receiver.getFileName() == null)
        filename = _form.addStringInput(Message.newSaveAs());
  }


  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    //FIXME implement command
    try{
      if (_receiver.getfilename()==null){
        _form.parse();
        _receiver.setfilename(filename.value());
        _receiver.save(filename.value());
      }
      _receiver.save(_receiver.getfilename());
    }
    catch(IOException e){
      e.printStackTrace();
    }
  }
}
