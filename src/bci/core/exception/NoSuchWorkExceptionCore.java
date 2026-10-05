package bci.core.exception;

public class NoSuchWorkExceptionCore extends Exception{
    
    /**
     * Class encoding reference to an invalid work id.
     */
    @java.io.Serial
    private static final long serialVersionUID = 202507171003L;
    
      /**
    * @param id unknown work id
       */
    public NoSuchWorkExceptionCore(int idWork) {
        super("A obra " + idWork + " não existe.");
    }
}
    