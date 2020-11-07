package woo;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import woo.exceptions.DuplicateProductKeyException;
import woo.exceptions.DuplicateClientKeyException;
import woo.exceptions.ImportFileException;

//FIXME import classes (cannot import from pt.tecnico or woo.app)

/**
 * Class Store implements a store.
 */
public class Store implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202009192006L;

  // FIXME define attributes
  private String _name;
  // private int _availableBalance;
  // private int accountingBalance;
  // client
  private Map<String, Client> _clients;
  
  // suppliers
  private Map<String, Supplier> _suppliers;
  // product
  private Map<String, Product> _products;
  
  // FIXME define contructor(s)
  public Store() {
    _clients = new HashMap<String, Client>();
    _suppliers = new HashMap<String, Supplier>();
    _products = new HashMap<String, Product>();
  }

  // FIXME define methods

  public Map<String, Product> get_products() {
    return _products;
  }

  public void set_products(Map<String, Product> _products) {
    this._products = _products;
  }

  public void addClient(String name, String key, String address) throws DuplicateClientKeyException {
    if(_clients.containsKey(key)){}
      //throw new DuplicateClientException(key,name);
    else{
      Client client = new Client(name,key,address);
      _clients.put(key,client); 
    }
  }

  public void addSupplier(String name,String key,String address) throws  DuplicateProductKeyException{
    if (_suppliers.containsKey(key)){

    }
    else{
      Supplier supplier=new Supplier(name,key,address);
      _suppliers.put(key,supplier);
    }
  }

  public void addProductBox(String key,String service_type,String supp_key,int price,int critical_value,int stock) throws  DuplicateProductKeyException{
    if(_products.containsKey(key)){
      //throw exception
    }
    else{
      Product product= new Box(key,service_type,supp_key,price,critical_value,stock);
      _products.put(key,product);
    }
  }

  public void addProductContainer(String key,String service_type,String service_level,String supp_key,int price,int critic_value,int stock) throws  DuplicateProductKeyException{
    if(_products.containsKey(key)){
      //throw exception
    }
    else{
      Container container= new Container(key,service_type,service_level,supp_key,price,critic_value,stock);
      _products.put(key,container);
    }
  }
  public void addProductBook(int price,int critic_value,int stock,String supp_key,String Title,String Author,String ISBN,String key) throws  DuplicateProductKeyException{
    if(_products.containsKey(key)){
      //throw exception
    }
    else{
      Book book= new Book(price,critic_value,stock,supp_key,Title,Author,ISBN,key);
      _products.put(key,book);
    }
  }

  public final String getName(){
    return _name;
  }
  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  void importFile(String txtfile) throws IOException /* FIXME maybe other exceptions */ {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(txtfile));
      String line;

      // Reads all lines from file "filename"
      while ((line = reader.readLine()) != null) {
          String[] fields = line.split("\\|");
          Pattern patClient = Pattern.compile("^(CLIENT)");
          Pattern patSupplier = Pattern.compile("^(SUPPLIER)");
          Pattern patBox = Pattern.compile("^(BOX)");
          Pattern patContainer = Pattern.compile("^(CONTAINER)");
          Pattern patBook = Pattern.compile("^(BOOK");
          if (patClient.matcher(fields[0]).matches()){
            addClient(fields[2],fields[1],fields[3]);
          }
          else if(patBox.matcher(fields[0]).matches()){
            
            addProductBox(fields[1],fields[2],fields[3],Integer.parseInt(fields[4]),Integer.parseInt(fields[5]),Integer.parseInt(fields[6]));
          }
          else if (patSupplier.matcher(fields[0]).matches()) {
            addSupplier(fields[1],fields[2],fields[3]);
          }
          else if(patContainer.matcher(fields[0]).matches()) {
            addProductContainer(fields[1],fields[2],fields[3],fields[4],Integer.parseInt(fields[5]),Integer.parseInt(fields[6]),Integer.parseInt(fields[7]));
          }
          else if(patBook.matcher(fields[0]).matches()){
            addProductBook(Integer.parseInt(fields[6]),Integer.parseInt(fields[7]),Integer.parseInt(fields[8]),fields[5],fields[2],fields[3],fields[4],fields[1]);
          }
        }
      }
      catch (IOException e){
        System.out.println("File not Found");
      }
      catch (DuplicateClientKeyException e){
        System.out.println("Duplicate Client Error");
    }
      catch(DuplicateProductKeyException e){
        System.out.println("Duplicate Product Error");
      }

}

}
