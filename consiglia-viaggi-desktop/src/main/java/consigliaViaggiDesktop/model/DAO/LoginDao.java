package consigliaViaggiDesktop.model.DAO;

import java.io.IOException;

public interface LoginDao {

    boolean authenticate(String username, String pwd) throws DaoException;
    String getAuthenticatedUserToken();
}
