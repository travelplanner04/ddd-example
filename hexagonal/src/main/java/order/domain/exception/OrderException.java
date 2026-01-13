package order.domain.exception;

/**
 * Base Exception für Order Domain.
 */
public abstract class OrderException extends RuntimeException {

    protected OrderException(String message) {
        super(message);
    }
}
