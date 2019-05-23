package cli;

public class FactoryException extends Exception {
    public FactoryException(String errorMessage) {
        super(errorMessage);
    }
    public FactoryException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
