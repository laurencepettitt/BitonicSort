package cli;

public class BadParamatersException extends Exception {
    public BadParamatersException(String errorMessage) {
        super(errorMessage);
    }
    public BadParamatersException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
