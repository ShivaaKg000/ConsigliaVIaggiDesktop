package consigliaViaggiDesktop.model.DAO;

public class ReviewDaoFactory {
    public static ReviewDao getReviewDao(){
        /*Logica per scegliere il dao*/
        return new ReviewDaoJSON();

    }
}