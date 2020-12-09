package woo;

import java.io.Serializable;



public abstract class ClientClassification implements Serializable {
    
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String _status;
    private Client _client;
    public ClientClassification (Client client,String status) {
        _client=client;
        _status=status;
    }
    
    public Client getClient(){
        return _client;
    }
    public String get_status(){
        return _status;
    }
    public abstract void delayed_payment(int days);
    
    public abstract void up();
}