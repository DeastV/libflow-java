package bci.core.exception;

public class NoSuchCreatorExceptionCore extends Exception{
    @java.io.Serial
    private static final long serialVersionUID = 202507171003L;

    public NoSuchCreatorExceptionCore(String id) {
        super("O criador '" + id + "' não existe.");
    }
}
