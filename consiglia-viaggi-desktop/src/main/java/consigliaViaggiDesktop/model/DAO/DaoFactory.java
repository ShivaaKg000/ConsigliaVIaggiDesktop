package consigliaViaggiDesktop.model.DAO;

public class DaoFactory {

    public static AccommodationDao getAccommodationDao(){

        /*Logica per scegliere il dao*/
        return new AccommodationDaoJSON();

    }
    public static ReviewDao getReviewDao(){
        /*Logica per scegliere il dao*/
        return new ReviewDaoJSON();

    }
    public static LoginDao getLoginDao(){
        /*Logica per scegliere il dao*/
        return new LoginDaoSpring();

    }
}
