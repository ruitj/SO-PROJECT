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
import woo.Store;
import java.util.Collection;
import java.util.Collections;
import woo.exceptions.DuplicateProductException;
import woo.exceptions.ImportFileException;

import woo.exceptions.*;
//FIXME import classes (cannot import from pt.tecnico or woo.app)

/**
 * Storefront: façade for the core classes.
 */
public class Storefront{

  /** Current filename. */
  private String _filename="";
  private int _final;
  private boolean saved=true;
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
  public void save(String filename) throws IOException {
    //FIXME implement serialization method
    ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
    out.writeObject(_store);
    out.close();
    saved=true;
  }
  public void save() throws IOException, FileNotFoundException{
    ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)));
    out.writeObject(_store);
    out.close();
    saved=true;
  }
  public void advance_Date(int days){
    _final=_store.get_days()+days;
    _store.set_days(_final);
    saved=false;
  }
  public int store_days(){
    _final=_store.get_days();
    return _final;
}
public int get_balance(){
  return _store.get_balance();
}

public boolean search_transactions(int id) throws TransactionInvalid{
  return _store.search_transactions(id);
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
  public boolean save_state(){
    return saved;
  } 
 
  public String show_allproducts(){
    return _store.showAllProducts();
  }
  public String show_allsuppliers(){
    return _store.show_suppliers();
  }
  /**
   * @param filename
   * @throws UnavailableFileException
   */
  public Store load(String filename) throws IOException,UnavailableFileException,ClassNotFoundException{
    ObjectInputStream in= new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
    _store =(Store) in.readObject();
    _filename=filename;
    in.close();
    return _store;
  }

  public String getFileName() {
    return _filename;
  }
  public boolean ON_OFF_notf(String client_id,String prod_key){
    return _store.ON_OFF_notf(client_id,prod_key);
  }
  public boolean ON_OFF_trns(String supp_key){
    return _store.ON_OFF_trns(supp_key);
  }
  public void add_sale(String client_id,int limit_day,String prod_id,int amount) throws NotEnoughAmount{
    _store.createSaleTransaction(client_id,limit_day,prod_id,amount);
    saved=false;
  }
  public void setFileName(String name) {
    _filename = name;
    saved=false;
  }
  public void delete_order(){
    _store.delete_order();
  }
  public boolean supplier_exists(String supp_key) throws UnknownSupplierException{
    return _store.supplier_exists(supp_key);
  }
  public boolean client_exists(String client_key) throws UnknownClientException{
    return _store.client_exists(client_key);
  }
  public String show_supp_orders(String supp_key)throws UnknownSupplierException{
    return _store.show_orders(supp_key);
  }
  public String show_client_transactions(String client_id){
    return _store.show_client_transactions(client_id);
  }

  public String show_transaction(int id){
    return _store.show_transaction(id);
  }
  public boolean prod_exists(String prod_id) throws UnknownProdException{
    return _store.prod_exists(prod_id);
  }
  public String get_products_under(int price){
    return _store.get_products_under(price);
  }

  public void pay_sale(int id){
    _store.pay_sale(id);
  }
  public boolean search_client(String id) throws UnknownClientException{
    return _store.search_client(id);
  }
  public String showClient(String key) throws UnknownClientException{
    return _store.showClient(key);
  }
  public void createOrder(boolean more,String supp_id , String prod_key,int amount){
    _store.createTransactionOrder(more,supp_id, prod_key, amount);
    saved=false;
  }
  public String showAllClients() {
    return _store.showAllClients();
  }
  public boolean transaction_real(int id) throws UnknownTrnsException{
    return _store.transaction_real(id);
  }
  public boolean service_type_valid(String type) throws UnknownTypeException{
    return _store.service_type_valid(type);
  }
  public boolean service_level_valid(String type) throws UnknownLevelException{
    return _store.service_level_valid(type);
  }

  public void  addClient(boolean notf,String name,String key,String address) throws  DuplicateClientException{
      _store.addClient(notf,name,key,address);
      saved=false;
        }
  public void ChangePrice(String key,int price){
    _store.Change_Price(key,price);
    saved=false;
  }
  public void addProductBook(int price,int critic_value,int stock,String supp_key,String Title,String Author,String ISBN,String key) throws  DuplicateProductException{
    _store.addProductBook(price,critic_value,stock,supp_key,Title,Author,ISBN,key);
    saved=false;
  }
  public void addSupplier(String name,String key,String address)throws DuplicateSupplierException{
    _store.addSupplier(key,name,address);
    saved=false;
  }
  public void addProductBox(String key,String service_type,String supp_key,int price,int critical_value,int stock) throws  DuplicateProductException{
    _store.addProductBox(key, service_type, supp_key, price, critical_value, stock);
    saved=false;
  }
  public void addProductContainer(String key,String service_type,String service_level,String supp_key,int price,int critic_value,int stock) throws  DuplicateProductException{
    _store.addProductContainer(key,service_type,service_level,supp_key,price,critic_value,stock);
    saved=false;
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException{
      _store.importfile(textfile);
      saved=true;
  }
}
