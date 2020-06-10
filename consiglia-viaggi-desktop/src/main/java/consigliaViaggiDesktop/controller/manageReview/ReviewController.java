package consigliaViaggiDesktop.controller.manageReview;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.model.DAO.DaoException;
import consigliaViaggiDesktop.model.DAO.ReviewDao;
import consigliaViaggiDesktop.model.DAO.ReviewDaoJSON;
import consigliaViaggiDesktop.model.DTO.JsonPageResponse;
import consigliaViaggiDesktop.model.Review;
//import consigliaViaggiDesktop.model.ReviewDao;
import consigliaViaggiDesktop.model.SearchParamsAccommodation;
import consigliaViaggiDesktop.model.SearchParamsReview;
import consigliaViaggiDesktop.view.ReviewDetailView;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
public class ReviewController {

	
    private ReviewDao reviewDao;
    private ObservableList<Review> observableReviewList;
    private SearchParamsReview currentSearchParamsReview;
    private ExecutorService executor;
    private int currentAccommodationId;
    private IntegerProperty pageNumber;
    private IntegerProperty totalPageNumber;
    private IntegerProperty totalElementNumber;

    public IntegerProperty getPageNumber() {
        return pageNumber;
    }
    public IntegerProperty getTotalPageNumber() {
        return totalPageNumber;
    }
    public IntegerProperty getTotalElementNumber() {
        return  totalElementNumber;
    }


    public ReviewController() {

        totalPageNumber= new SimpleIntegerProperty();
        pageNumber= new SimpleIntegerProperty(-1);
        totalElementNumber= new SimpleIntegerProperty();

        executor= new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));
        reviewDao= new ReviewDaoJSON();
        observableReviewList= FXCollections.observableArrayList();
    }

    public ObservableList<Review> loadReviewListAsync(SearchParamsReview params) throws DaoException {

        currentSearchParamsReview =params;

        Task task = new Task() {
            @Override
            public Void call() throws DaoException {
                JsonPageResponse<Review> page=reviewDao.getReviewList(currentSearchParamsReview);
                List<Review> reviewList= page.getContent();
                pageNumber.setValue(page.getPage());
                totalPageNumber.setValue(page.getTotalPages());
                totalElementNumber.setValue(page.getTotalElements());
                observableReviewList.clear();
                observableReviewList.addAll(reviewList);
                return null;
            }
        };
        executor= new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));

        Thread testThread = new Thread(task);
        executor.execute(testThread);

        return observableReviewList;
    }
    public ObjectProperty<Review> getReviewAsync(int reviewId) {
        ObjectProperty<Review>  observableReview = new SimpleObjectProperty<Review>();
        Task task = new Task() {
            @Override
            public Void call() throws InterruptedException {

                try {
                    Review review = reviewDao.getReviewById(reviewId);
                    observableReview.set(review);
                } catch (DaoException e) {
                    NavigationController.getInstance().buildInfoBox("Errore",e.getErrorMessage());
                }

                return null;
            }
        };
        Thread testThread = new Thread(task);
        testThread.start();
        return observableReview;

    }

    public void refreshList() throws DaoException {
        loadReviewListAsync(currentSearchParamsReview);
    }

	public void reviewSelected(int reviewId) {

        ReviewDetailView reviewDetailView=new ReviewDetailView();
        reviewDetailView.setId(reviewId);
        reviewDetailView.setReviewController(this);
        NavigationController.getInstance().navigateToView(Constants.REVIEW_DETAIL_VIEW,reviewDetailView);
    }

	public void goBack() {
    	NavigationController.getInstance().navigateBack();
		
	}
	public void goBackToMenu() {
        //per tornare al menu bisogna interrompere l'executor altrimenti resta attivo in bg
        executor.shutdownNow();
        NavigationController.getInstance().navigateBack();

    }

    public void nextPage() throws DaoException {if (pageNumber.getValue()+1<totalPageNumber.getValue()) {
        currentSearchParamsReview.setCurrentpage(pageNumber.getValue()+1);
        loadReviewListAsync(currentSearchParamsReview);
    }
    }

    public void previousPage() throws DaoException {
        if (pageNumber.getValue()>0) {
            currentSearchParamsReview.setCurrentpage(pageNumber.getValue()-1);
            loadReviewListAsync(currentSearchParamsReview);
        }
    }
}
