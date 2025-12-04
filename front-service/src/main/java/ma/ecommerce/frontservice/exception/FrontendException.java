package ma.ecommerce.frontservice.exception;

public class FrontendException extends RuntimeException {

    public FrontendException(String message) {
        super(message);
    }

    public FrontendException(String message, Throwable cause) {
        super(message, cause);
    }
}
