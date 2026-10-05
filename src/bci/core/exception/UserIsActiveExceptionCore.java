package bci.core.exception;

public class UserIsActiveExceptionCore extends Exception{
    @java.io.Serial
  private static final long serialVersionUID = 202507171003L;

 /**
   * @param id the id of concerned user
   */
  public UserIsActiveExceptionCore(int id) {
    super("Não ativo" + id);
  }
}
