package pl.kacper;

public class NotEnoughtProductsException extends RuntimeException {
    public NotEnoughtProductsException(String message) {
        super(message);
    }
}
