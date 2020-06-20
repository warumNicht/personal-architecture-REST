package architecture.error;

public abstract class BaseControllerException extends RuntimeException {
    public BaseControllerException(String message) {
        super(message);
    }
}
