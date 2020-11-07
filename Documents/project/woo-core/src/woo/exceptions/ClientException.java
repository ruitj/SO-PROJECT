package woo.exceptions;

/**
 * Exception for unknown import file entries.
 */
public class ClientException extends Exception {

    private static final long serialVersionUID = 1L;
    private final String _id;
    private String _name;

    public ClientException(String id, String name) {
        _id = id;
        set_name(name);
    }

    public String get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }
}