package woo.app.exceptions;

import pt.tecnico.po.ui.DialogException;

/** Exception for date-related problems. */
public class FileNotFound extends DialogException {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202009192335L;

  /** Bad date. */
  private String _string;

  /** @param date bad date to report. */
  public FileNotFound(String filename) {
    _string=filename;
  }

  /** @see pt.tecnico.po.ui.DialogException#getMessage() */
  @Override
  public String getMessage() {
    return Message.fileNotFound(_string);
  }

}