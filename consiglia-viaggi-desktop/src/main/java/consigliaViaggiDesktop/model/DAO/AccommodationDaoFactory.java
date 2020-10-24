package consigliaViaggiDesktop.model.DAO;

public class AccommodationDaoFactory{

    public static AccommodationDao getAccommodationDao(){

        /*Logica per scegliere il dao*/
        return new AccommodationDaoJSON();

    }
}
