package woo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import woo.exceptions.DuplicateProductKeyException;
import woo.exceptions.DuplicateClientKeyException;
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
  private Map<String, Client> _clients = new HashMap<String, Client>();
  // suppliers
  // private Map<String,suppliers> _suppliers=new HashMap<String,suppliers>();
  // product
  private Map<String, Product> _products = new HashMap<String, Product>();

  // FIXME define contructor(s)
  public Store(String name) {
    _name = name;
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

  }
  public void addProductBox(String name,String key,String address) throws  DuplicateProductKeyException{

  }
  public void addProductContainer(String name,String key,String address) throws  DuplicateProductKeyException{

  }
  public void addProductBook(String name,String key,String address) throws  DuplicateProductKeyException{

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
    //FIXME implement method

  }

}
