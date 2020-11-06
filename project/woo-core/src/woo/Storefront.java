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
  private Store _store = new Store("STORE");

  //FIXME define other attributess
  //FIXME define constructor(s)

  //FIXME define other methods
@SuppressWarnings("nls")
public static void addFromInputFile(Store store,String _filename){
  int line_number=0;
  try{
    BufferedReader  in = new BufferedReader(new FileReader(_filename));
    String s;

    while (( s = in.readLine() ) != null) {
      String line=new String(s.getBytes(),"UTF-8");
      line_number++;
      String[] split=line.split("|");
      if (split[0].equals("SUPPLIERS"))
        try{
          store.addSupplier(split[2],split[1],split[3]);
          }
        catch(DuplicateProductKeyException he){
        System.err.println(he);
        }
      if (split[0].equals("CLIENT"))
        try{
        store.addClient(split[2],split[1], split[3]);
        }
        catch (DuplicateClientKeyException DC){
          System.err.println(DC);
        }
      if (split[0].equals("BOX"))
        try{
          store.addProductBox(split[2],split[1],split[3]);
        }
        catch(DuplicateProductKeyException DP){
          System.err.println(DP);
        }
      if (split[0].equals("CONTAINER"))
        try{
          store.addProductContainer(split[2],split[1],split[3]);
        }
        catch(DuplicateProductKeyException DP){
          System.err.println(DP);
        }
      else if (split[0].equals("BOOK"))
        try{
          store.addProductBook(split[2],split[1],split[3]);
        }
        catch(DuplicateProductKeyException DP){
          System.err.println(DP);
        }
      }
      in.close();
    }
    catch (FileNotFoundException e) {
      System.out.println("File not found: " + _filename + ": " + e);  
}
    catch (IOException e) {
      System.out.println("IO error: " + _filename + ": " + line_number + ": line " + e);
}
}
  /**
   * @throws IOException
   * @throws FileNotFoundException
   * @throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException {
    //FIXME implement serialization method
    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(_filename));
    out.writeObject(_store);
    out.close();
  }

  /**
   * @param filename
   * @throws MissingFileAssociationException
   * @throws IOException
   * @throws FileNotFoundException
   */
  public void saveAs(String filename) throws  FileNotFoundException, IOException{
    _filename = filename;
    save();
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
