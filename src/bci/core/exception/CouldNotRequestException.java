package bci.core.exception;

public class CouldNotRequestException extends Exception{
    @java.io.Serial
    private static final long serialVersionUID = 202507171003L;
    private int error;

    public CouldNotRequestException(int errorId){
         error = errorId;
    }

    public int getError(){
        return error;
    }
}
