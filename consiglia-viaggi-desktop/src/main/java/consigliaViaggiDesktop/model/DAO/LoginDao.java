package consigliaViaggiDesktop.model.DAO;

public interface LoginDao {

    boolean authenticate(String username, String pwd) throws DaoException;
    String getAuthenticatedUserToken();
}
