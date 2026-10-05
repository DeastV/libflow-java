package bci.core.exception;

public class NoIdMatchException extends Exception{
  @java.io.Serial
  private static final long serialVersionUID = 202507171003L;

  private static final String ERROR_MESSAGE = "ID inexistente";

  public NoIdMatchException(){
    super(ERROR_MESSAGE);
  }
}
