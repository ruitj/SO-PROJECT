package woo;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.Serializable;
import java.util.TreeMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;
import woo.*;
import woo.exceptions.*;

//FIXME import classes (cannot import from pt.tecnico or woo.app)

/**
 * Class Store implements a store.
 */
public class Store implements Serializable {
   
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202009192006L;

  // FIXME define attributes
  private String _name;
  private int _day;
  private int _transaction_id=0;
  private int orders=0;
  private int _available_balance;
  private int _accountingBalance;
  private ArrayList<Product> _store_products= new ArrayList<Product>();
  private ArrayList<Transaction> _transactions = new ArrayList<Transaction>();
  private SortedMap<String,Client> _clients = new TreeMap<String,Client>(String.CASE_INSENSITIVE_ORDER);
  private SortedMap<String,Supplier>  _suppliers = new  TreeMap<String, Supplier>(String.CASE_INSENSITIVE_ORDER);
  private SortedMap<String,Product>  _products = new TreeMap<String, Product>(String.CASE_INSENSITIVE_ORDER);
  
  
 

  public Store(TreeMap<String,Product> products) {
      _products = products;
      _day=0;
  }
  
  public Store() {
      _day=0;
  }
  public void set_days(int days){
      _day=days;
  }
  public int get_days(){
      return _day;
  }
  // FIXME define methods

  public Map<String, Product> get_products() {
    return _products;
  }

  public boolean ON_OFF_notf(String client_key,String prod_key){
    Client client;
    for(Map.Entry<String, Client> client_entry : _clients.entrySet()){
      if(client_entry.getValue().getkey().equals(client_key)){
        for(Map.Entry<String, Product> entry : _products.entrySet()){
          if(entry.getValue().get_product_key().equals(prod_key))
            if(entry.getValue().Is_Observer(client_entry.getValue())) {
              entry.getValue().removeObserver(client_entry.getValue());
              return entry.getValue().Is_Observer(client_entry.getValue());
              
            } 
            else{
              entry.getValue().registerObserver(client_entry.getValue());
              return entry.getValue().Is_Observer(client_entry.getValue());
            }
          }
      }
    }
    return false;
  }

  public boolean prod_exists(String prod_key) throws UnknownProdException{
    for(Map.Entry<String,Product> p_entry : _products.entrySet()){
      if(p_entry.getValue().get_product_key().equals(prod_key))
        return true;
    }
    throw new UnknownProdException(prod_key);
  }

  public boolean supplier_exists(String supp_key) throws UnknownSupplierException{
    for(Map.Entry<String,Supplier> supp_entry : _suppliers.entrySet()){
      if(supp_entry.getValue().getkey().equals(supp_key))
        return true;
    }
    throw new UnknownSupplierException(supp_key);
  }
  public String get_products_under(int price){
    String str= "";
    for(Map.Entry<String, Product> product : _products.entrySet())
      if(product.getValue().get_price()<price)
        str+=product.getValue().toString();
    return str;
  }
  public boolean ON_OFF_trns(String supplier_key){
    for(Map.Entry<String,Supplier> supp_entry : _suppliers.entrySet()){
      if(supp_entry.getValue().getkey().equals(supplier_key)){
          if(supp_entry.getValue().has_trns()) {
              supp_entry.getValue().change_trns(false);
              return supp_entry.getValue().has_trns();
              
            } 
            else{
              supp_entry.getValue().change_trns(true);
              return supp_entry.getValue().has_trns();  
            }
          }
      }
      return false;
  }

  public boolean search_transactions(int id) throws TransactionInvalid{
    for (int counter = 0; counter < _transactions.size(); counter++){
      if(counter==id)
        return true;
    }
    throw new TransactionInvalid(id);
  }
  public void delete_order(){
    String supp_key;
    for (int counter = 0; counter < _transactions.size(); counter++){
      if(counter==_transactions.size()-1){
        supp_key=_transactions.get(counter).get_supp_key();
        for(Map.Entry<String,Supplier> supp_entry : _suppliers.entrySet()){
          if(supp_entry.getValue().getkey().equals(supp_key)){
            supp_entry.getValue().remove_last_order();
          }
        }
      }
    }
    for (int counter = 0; counter < _transactions.size(); counter++){
      if(counter==_transactions.size()-1)
        _transactions.remove(counter);
    }
    orders--;
    _transaction_id--;
  }
  public boolean client_exists(String client_key) throws UnknownClientException{
    for(Map.Entry<String,Client> c_entry : _clients.entrySet()){
      if(c_entry.getValue().getkey().equals(client_key))
        return true;
    }
    throw new UnknownClientException(client_key);
  }


  public String show_orders(String supp_key)throws UnknownSupplierException{
    if(supplier_exists(supp_key)==false)
      throw new UnknownSupplierException(supp_key);
    else{
      String str="";
    for(Map.Entry<String,Supplier> supp_entry : _suppliers.entrySet())
    if(supp_entry.getValue().getkey().equals(supp_key))
      str = str +supp_entry.getValue().show_transactions();
    return str;
    }
  }
  public String show_transaction(int id){
    String str="";
    for (int counter = 0; counter < _transactions.size(); counter++){
      if(_transactions.get(counter).get_id()==id){
        calculate_sale(counter);
        str+= _transactions.get(counter).toString()+"\n";
        break;
        }
    }
    return str;
  }

  public int get_balance(){
    return _available_balance;
  }
  public void Change_Price(String key,int price){
    for(Map.Entry<String, Product> entry : _products.entrySet())
    if(entry.getValue().get_product_key().equals(key))
        entry.getValue().change_price(price);
  }

  public void set_products(TreeMap<String, Product> _products) {
    this._products = _products;
  }

  public String showAllProducts() {
    String str = "";
    for(Map.Entry<String, Product> entry : _products.entrySet()) 
        str = str + entry.getValue().toString() + "\n";
    return str;
}
  public String show_suppliers() {
    String str = "";
    Map<String,Supplier> treeMap = new TreeMap<String, Supplier>(_suppliers);
    for(Map.Entry<String, Supplier> entry : treeMap.entrySet()) 
        str = str + entry.getValue().toString() + "\n";
    return str;
}

  public void addClient(boolean notf,String name, String key, String address) throws DuplicateClientException {
    if(_clients.containsKey(key))
      throw new DuplicateClientException(key);
    Client client = new Client(name,key,address);
    _clients.put(key,client);
    if(notf){
      for(Map.Entry<String,Product> entry : _products.entrySet())
        entry.getValue().registerObserver(client);
    } 
  }

  public void createTransactionOrder(boolean more,String supp_id , String prod_key,int amount){
    Product product,order_product;
    Transaction order;
    Supplier supp;
    int id,cont=0;
    if(more){
      //System.out.println(orders);
      for (int counter = 0; counter < _transactions.size(); counter++){
        if(_transactions.get(counter) instanceof Order){
          cont++;
        }
        if(cont ==orders){
          order=_transactions.get(counter);
          product= _products.get(prod_key);
          product.add_quantity(amount);
        if(product instanceof Book){
          order_product = new Book(product.get_price(),0,amount,supp_id,"","","",prod_key); 
          order.add_order(prod_key,order_product);
          _store_products.add(order_product);
        }
        if(product instanceof Container){
          order_product = new Container(product.get_product_key(),"","",supp_id,product.get_price(),0,amount); 
          order.add_order(prod_key,order_product);
          _store_products.add(order_product);
        }
        if(product instanceof Box){
          order_product = new  Box(product.get_product_key(),"",supp_id,product.get_price(),0,amount); 
          order.add_order(prod_key,order_product);
          _store_products.add(order_product);
        }
          break;
    }
  }
}
    //_transactions.add(order);
    else{
      ++orders;
      System.out.println(_transaction_id);
      supp=_suppliers.get(supp_id);
      product=_products.get(prod_key);
      product.add_quantity(amount);
      for (int counter = 0; counter < _store_products.size(); counter++){
        if(_store_products.get(counter).get_product_key()==prod_key)
            _store_products.get(counter).add_quantity(amount);
      }

      if(product instanceof Book){
        order_product = new Book(product.get_price(),0,amount,supp_id,"","","",prod_key); 
        order=new Order(_transaction_id,product.get_price(),supp);
        order.add_order(prod_key,order_product);
        _transactions.add(order);
        _store_products.add(order_product);
        supp.add_supp_order(order);
        }
      if(product instanceof Container){
        order_product = new Container(product.get_product_key(),"","",supp_id,product.get_price(),0,amount); 
        order=new Order(_transaction_id,product.get_price(),supp);
        order.add_order(prod_key,order_product);
        _transactions.add(order);
        _store_products.add(order_product);
        supp.add_supp_order(order);
        }
      if(product instanceof Box){
        order_product = new Box(product.get_product_key(),"",supp_id,product.get_price(),0,amount); 
        order=new Order(_transaction_id,product.get_price(),supp);
        order.add_order(prod_key,order_product);
        _transactions.add(order);
        _store_products.add(order_product);
        supp.add_supp_order(order);
        }
        _transaction_id+=1;
      }
    }
    
    public void createSaleTransaction(String client_id,int limit_day,String prod_id,int amount)throws NotEnoughAmount{
      Product product= _products.get(prod_id),sale_product;
      Transaction sale;
      Client client=_clients.get(client_id);
      /*for (int counter = 0; counter < _store_products.size(); counter++){
        if(_store_products.get(counter).get_product_key()==prod_id){
            if(_store_products.get(counter).get_quantity()<amount)
              System.out.println(amount);
              throw new NotEnoughAmount(_store_products.get(counter).get_quantity(),amount);
            }
          }*/
          System.out.println(amount);
      for(Map.Entry<String, Product> entry : _products.entrySet()){
        if(entry.getValue().get_product_key().equals(prod_id)){
          if(entry.getValue().get_quantity()<amount){
              throw new NotEnoughAmount(entry.getValue().get_quantity(),amount);
          }
        }
      }
        for(Map.Entry<String, Product> entry : _products.entrySet()){
          if(entry.getValue().get_product_key().equals(prod_id))
              entry.getValue().remove_stock(amount);
          }      
        for (int counter = 0; counter < _store_products.size(); counter++){
          if(_store_products.get(counter).get_product_key()==prod_id)
              _store_products.get(counter).remove_stock(amount);
        }
        //System.out.println(_transaction_id); 
        if(product instanceof Book){
          sale_product = new Book(product.get_price(),0,amount,"","","","",prod_id);
          sale= new Sale(client,_transaction_id,amount,sale_product,limit_day);
          _transactions.add(sale);
          client.add_Sale(sale_product);
        }
        if(product instanceof Container){
          sale_product = new Container(product.get_product_key(),"","","",product.get_price(),0,amount);
          sale= new Sale(client,_transaction_id,amount,sale_product,limit_day);
          _transactions.add(sale);
          client.add_Sale(sale_product);
        }
        if(product instanceof Box){
          sale_product = new Box(product.get_product_key(),"","",product.get_price(),0,amount); 
          sale=new Sale(client,_transaction_id,amount,sale_product,limit_day);
          _transactions.add(sale);
          client.add_Sale(sale_product);
          }
          _transaction_id+=1;
    }
  public void calculate_sale(int id){
    String Period="";
    Client client;
    int N=0;
    int deadline;
    int identifier;
    double value;
    for (int counter = 0; counter < _transactions.size(); counter++){
      if(counter==id){
        if(_transactions.get(counter) instanceof Order)
          return;
        if(_transactions.get(counter).get_product() instanceof Box)
          N=5;
        if(_transactions.get(counter).get_product() instanceof Container)
          N=8;
        if(_transactions.get(counter).get_product() instanceof Book)
          N=3;
      client=_transactions.get(counter).get_buyer();
      identifier=counter;
      deadline=_transactions.get(counter).get_deadline();
    if(deadline-_day>=N)
      Period="P1";
    if((deadline-_day>=0)&&(deadline-_day<N))
      Period="P2";
    if((_day-deadline>0)&&(_day-deadline<=N)){
      Period="P3";
      System.out.println("ok");
    }
    if(_day-deadline>N)
      Period="P4";
    _transactions.get(identifier).calculate(client,Period,deadline-_day);
    value=_transactions.get(identifier).get_value();
    }
  }
}

  public void  pay_sale(int id){
    Client client;
    int deadline;
    double value;
    calculate_sale(id);
    for (int counter = 0; counter < _transactions.size(); counter++){
      if(counter==id){
        if(_transactions.get(counter) instanceof Order)
          return;
        client=_transactions.get(counter).get_buyer();
        deadline=_transactions.get(counter).get_deadline();
        _transactions.get(counter).pay(_day);
        value=_transactions.get(counter).get_value();
        client.add_sale_value(value);
        if(deadline>_day)
          client.add_sale_points(value);
        else
          client.remove_sale_points(_day-deadline);
      }
  }
}


  public void addSupplier(String key,String name,String address) throws DuplicateSupplierException{
    if (_suppliers.containsKey(key)){
      throw new DuplicateSupplierException();
    }
    else{
      Supplier supplier =new Supplier(name,key,address);
      _suppliers.put(key,supplier);
    }
  }

  public void addProductBox(String key,String service_type,String supp_key,int price,int critical_value,int stock) throws  DuplicateProductException{
    if(_products.containsKey(key)){
      //throw exception
    }
    else{
      Product product= new Box(key,service_type,supp_key,price,critical_value,stock);
      //if(notf){
        for(Map.Entry<String, Client> entry : _clients.entrySet()){
            product.registerObserver(entry.getValue());
            }
          //}
      _products.put(key,product);
    }
  }

  public void addProductContainer(String key,String service_type,String service_level,String supp_key,int price,int critic_value,int stock) throws  DuplicateProductException{
    if(_products.containsKey(key)){
      //throw exception
    }
    else{
      Product container= new Container(key,service_type,service_level,supp_key,price,critic_value,stock);
      //if(notf){
        for(Map.Entry<String, Client> entry : _clients.entrySet()){
          container.registerObserver(entry.getValue());
            }
          //}
      _products.put(key,container);
    }
  }
  public void addProductBook(int price,int critic_value,int stock,String supp_key,String Title,String Author,String ISBN,String key) throws  DuplicateProductException{
    if(_products.containsKey(key)){
      //throw exception
    }
    else{
      Product book= new Book(price,critic_value,stock,supp_key,Title,Author,ISBN,key);
      //if(notf){
      for(Map.Entry<String, Client> entry : _clients.entrySet()){
          book.registerObserver(entry.getValue());
          }
        //}
      _products.put(key,book);
    }
  }
    

  public String showAllClients() {
    String str = "";
    for(Map.Entry<String, Client> entry : _clients.entrySet()){
        entry.getValue().show_notf(false);
        str = str + entry.getValue().toString();
    }
    return str;
}
  public String show_client_transactions(String client_id){
    String str="";
    for (int counter = 0; counter < _transactions.size(); counter++){
      if((_transactions.get(counter) instanceof Sale)&&(_transactions.get(counter).get_client_id().equals(client_id))){
        str+=_transactions.get(counter).toString()+"\n";
      }
    }
    return str;
  }



  public boolean search_client(String id) throws UnknownClientException {
    if (_clients.containsKey(id))
      return true;
    throw new UnknownClientException(id);
  }
  public boolean service_type_valid(String type) throws UnknownTypeException {
    if(type.equals("NORMAL"))
      return true;
    if(type.equals("AIR"))
      return true;
    if(type.equals("EXPRESS"))
      return true;
    if(type.equals("PERSONAL"))
      return true;
    throw new UnknownTypeException();
  }
  public boolean service_level_valid(String type) throws UnknownLevelException {
    if(type.equals("B4"))
      return true;
    if(type.equals("C4"))
      return true;
    if(type.equals("C5"))
      return true;
    if(type.equals("DL"))
      return true;
    throw new UnknownLevelException();
  }
  public String showClient(String id) throws UnknownClientException {
    if (_clients.containsKey(id)){
      _clients.get(id).show_notf(true);
      return _clients.get(id).toString();
    }
    throw new UnknownClientException(id);
  }
  
  public final String getName(){
    return _name;
  }
  public boolean transaction_real(int id)throws UnknownTrnsException{
    if(id<_transaction_id && id>=0)
      return true;
    throw new UnknownTrnsException();
  }
  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  void importfile(String txtfile) throws ImportFileException {
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
          Pattern patBook = Pattern.compile("^(BOOK)");
          if (patClient.matcher(fields[0]).matches()){
            addClient(false,fields[2],fields[1],fields[3]);
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
        System.out.println(txtfile);
      }
      catch (DuplicateClientException e){
        System.out.println("Duplicate Client Error");
    }
      catch(DuplicateProductException e){
        System.out.println("Duplicate Product Error");
      }
      catch(DuplicateSupplierException e ){

      }

}

}
