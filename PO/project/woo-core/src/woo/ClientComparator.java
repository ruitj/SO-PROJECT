package woo;
import java.util.Comparator;


public class ClientComparator implements Comparator<Client> {
    
    public ClientComparator(){

    }
    @Override
    public int compare(Client o1, Client o2) {
        if (o1.getkey().compareTo(o2.getkey())>0 ) {
            return 1;
        } else if (o1.getkey().compareTo(o2.getkey())<0) {
            return -1;
        } else {
            return 0;
        }
    }
 
} 