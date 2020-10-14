package consigliaViaggiDesktop.model.DAO;

public class DaoException extends Exception {

    public final static int ERROR = 3;
    public final static int FORBIDDEN_ACCESS = 4;
    public static final int NOT_FOUND = 5 ;

    private final String errorMessage;
    private final int errorCode;

    public DaoException(int errorCode,String message) {
        this.errorCode = errorCode;
        this.errorMessage=message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
