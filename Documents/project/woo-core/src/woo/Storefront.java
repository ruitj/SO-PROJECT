package woo;
import java.io.*;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import woo.exceptions.DuplicateClientKeyException;
import woo.exceptions.DuplicateProductKeyException;
import woo.exceptions.ImportFileException;
//FIXME import classes (cannot import from pt.tecnico or woo.app)

/**
 * Storefront: façade for the core classes.
 */
public class Storefront {

  /** Current filename. */
  private String _filename = "";

  /** The actual store. */
  private Store _store = new Store();

  //FIXME define other attributess
  //FIXME define constructor(s)

  //FIXME define other methods

  /**
   * @throws IOException
   * @throws FileNotFoundException
   * @throws MissingFileAssociationException
   */
  public void save(String filename) throws IOException, FileNotFoundException {
    //FIXME implement serialization method
    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
    out.writeObject(_store);
    out.close();

  }

  /**
   * @param filename
   * @throws MissingFileAssociationException
   * @throws IOException
   * @throws FileNotFoundException
   */
  /*public void saveAs(String filename) throws  FileNotFoundException, IOException{
    _filename = filename;
    save();
  }*/
  public String getfilename(){
    return _filename;
  }
  public String setfilename(String filename){
    _filename=filename;
  }
  /**
   * @param filename
   * @throws UnavailableFileException
   */
  public Store load(String filename) throws IOException, ClassNotFoundException {
    ObjectInputStream in= new ObjectInputStream(new FileInputStream(filename));
    _store =(Store) in.readObject();
    in.close();
    return _store;
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException{
    try {
      _store.importFile(textfile);
    } 
    catch (IOException e) {
      throw new ImportFileException(textfile);
    }
  }

}
