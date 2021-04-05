package woo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;


public class NormalClient extends ClientClassification {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    public NormalClient (Client client) {
        super(client,"NORMAL");
    }

    public void up(){
        getClient().setClassification(new SelectionClient(getClient()));
    }


    public void delayed_payment(int delayed){}
}

