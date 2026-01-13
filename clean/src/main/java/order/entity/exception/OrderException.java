package order.entity.exception;

/**
 * Base exception for all Order-related domain exceptions.
 */
public abstract class OrderException extends RuntimeException {

    protected OrderException(String message) {
        super(message);
    }
}
