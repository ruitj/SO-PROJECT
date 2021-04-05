package woo;

import java.io.Serializable;


public class EliteClient extends ClientClassification {
    
    private static final long serialVersionUID = 1L;
    
    public EliteClient (Client client) {
		super(client,"ELITE");
    }
    public void delayed_payment(int delayed_days){
      double points;
      if(delayed_days>15){
        getClient().setClassification(new SelectionClient(getClient()));
        getClient().set_points(getClient().get_points()*0.25);
        }
      }
    public void up(){}
}
