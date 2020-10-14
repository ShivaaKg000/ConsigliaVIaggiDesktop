package consigliaViaggiDesktop.controller.manageReview;

import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.model.DAO.DaoException;
import consigliaViaggiDesktop.model.DAO.ReviewDao;
import consigliaViaggiDesktop.model.DAO.ReviewDaoJSON;
import consigliaViaggiDesktop.model.Review;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

public class ModerateReviewController {

    private final ReviewDao reviewDao;
    private final ReviewController reviewController;

    public ModerateReviewController(ReviewController reviewController){
        reviewDao=new ReviewDaoJSON();
        this.reviewController=reviewController;
    }

    public ObjectProperty<Review> approveReview(int reviewId) {
        ObjectProperty<Review> updatedReview= new SimpleObjectProperty<>();
        Task<Void> task = new Task<>() {
            @Override
            public Void call() throws DaoException {

                Review review=  reviewDao.approveReview(reviewId);
                updatedReview.setValue(review);
                NavigationController.getInstance().buildInfoBox("Valutazione recensione","Recensione approvata con successo!");
                reviewController.refreshList();
                return null;
            }
        };

        Thread approveReviewThread = new Thread(task);
        approveReviewThread.start();

        return updatedReview;
    }
    public ObjectProperty<Review> rejectReview(int reviewId) {
        ObjectProperty<Review> updatedReview= new SimpleObjectProperty<>();
        Task<Void> task = new Task<>() {
            @Override
            public Void call() throws DaoException {

                Review review=  reviewDao.rejectReview(reviewId);
                updatedReview.setValue(review);
                NavigationController.getInstance().buildInfoBox("Valutazione recensione","Recensione rigettata!");
                reviewController.refreshList();
                return null;
            }
        };

        Thread rejectReviewThread = new Thread(task);
        rejectReviewThread.start();

        return updatedReview;

    }
}
