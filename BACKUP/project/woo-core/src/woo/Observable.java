package woo;
  
  
public abstract class Observable {
    public abstract void registerObserver(Observer o);
    public abstract void removeObserver(Observer o);
    public abstract void notifyObservers(Product product,String notf);
  }