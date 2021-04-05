package woo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;


public class SelectionClient extends ClientClassification {
    
    private static final long serialVersionUID = 1L;
    
    public SelectionClient (Client client) {
		super(client,"SELECTION");
    }


    public void delayed_payment(int delayed_days){
        if(delayed_days>2){
        getClient().setClassification(new NormalClient(getClient()));
        getClient().set_points(getClient().get_points()*0.1) ;
          }
      }
    public void up(){
        getClient().setClassification(new EliteClient(getClient()));
  }
}
