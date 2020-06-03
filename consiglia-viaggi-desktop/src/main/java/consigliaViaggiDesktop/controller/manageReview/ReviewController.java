package consigliaViaggiDesktop.controller.manageReview;


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
import consigliaViaggiDesktop.model.SearchParamsReview;
import consigliaViaggiDesktop.view.ReviewDetailView;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
public class ReviewController {

	
    private ReviewDao reviewDao;
    private ObservableList<Review> observableReviewList;
    private SearchParamsReview currentSearchParamsReview;
    private ExecutorService executor;
    private int currentAccommodationId;
    private LongProperty pageNumber;
    private LongProperty totalPageNumber;
    private LongProperty totalElementNumber;

    public LongProperty getPageNumber() {
        return pageNumber;
    }
    public LongProperty getTotalPageNumber() {
        return totalPageNumber;
    }
    public LongProperty getTotalElementNumber() {
        return  totalElementNumber;
    }


    public ReviewController() {
        totalPageNumber= new SimpleLongProperty();
        pageNumber= new SimpleLongProperty(-1);
        totalElementNumber= new SimpleLongProperty();
        executor= new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));
        reviewDao= new ReviewDaoJSON();
        observableReviewList= FXCollections.observableArrayList();
    }

    public ObservableList<Review> loadReviewListAsync(SearchParamsReview params) throws DaoException {

        currentSearchParamsReview =params;
        observableReviewList.clear();
        Task task = new Task() {
            @Override
            public Void call() throws DaoException {
                JsonPageResponse<Review> page=reviewDao.getReviewList(currentSearchParamsReview);
                List<Review> reviewList= page.getContent();
                pageNumber.setValue(page.getPage());
                totalPageNumber.setValue(page.getTotalPages());
                totalElementNumber.setValue(page.getTotalElements());
                observableReviewList.addAll(reviewList);
                observableReviewList.notifyAll();
                System.out.println("\nCIAO"+reviewList);
                return null;
            }
        };
        executor= new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));

        Thread testThread = new Thread(task);
        executor.execute(testThread);

        return observableReviewList;
    }

    public void refreshList() throws DaoException {
        loadReviewListAsync(currentSearchParamsReview);
    }

    public ObjectProperty<Review> getReviewAsync(int reviewId) {
    	ObjectProperty<Review>  observableReview = new SimpleObjectProperty<Review>();
    	Task task = new Task() {
    		@Override
            public Void call() throws InterruptedException {
    			
    			Review review= reviewDao.getReviewById(reviewId);
    			observableReview.set(review);
				return null;
            }
        };
        Thread testThread = new Thread(task);
        testThread.start();
		return observableReview;
     
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

}
