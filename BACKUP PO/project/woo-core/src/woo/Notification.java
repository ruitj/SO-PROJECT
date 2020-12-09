package woo;

import java.io.Serializable;

public interface  Notification {
    public void registerInterested(Observer o);
    public void removeInterested(Observer o);
    public void notifyInterested();
}