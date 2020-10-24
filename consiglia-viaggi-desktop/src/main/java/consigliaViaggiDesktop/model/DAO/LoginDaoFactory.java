package consigliaViaggiDesktop.model.DAO;

public class LoginDaoFactory {

    public static LoginDao getLoginDao(){
        /*Logica per scegliere il dao*/
        return new LoginDaoSpring();

    }
}
