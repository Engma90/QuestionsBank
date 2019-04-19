package exceptions;

public class DBConnectionFailedException extends RuntimeException {
    public DBConnectionFailedException(final String message){
        super(message);
    }
}
