package bci.core.exception;


public class NotEnoughInventoryExceptionCore extends Exception {

    private static final long serialVersionUID = 20251009L;

    public NotEnoughInventoryExceptionCore() {
        super();
    }

    public NotEnoughInventoryExceptionCore(String message) {
        super(message);
    }
}
