package consigliaViaggiDesktop.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.DAO.AccommodationDaoJSON;
import consigliaViaggiDesktop.model.DAO.DaoException;
import consigliaViaggiDesktop.model.DAO.ReviewDao;
import consigliaViaggiDesktop.model.DAO.ReviewDaoJSON;
import consigliaViaggiDesktop.model.DTO.JsonPageResponse;
import consigliaViaggiDesktop.model.Review;
//import consigliaViaggiDesktop.model.ReviewDao;
import consigliaViaggiDesktop.model.SearchParamsAccommodation;
import consigliaViaggiDesktop.model.SearchParamsReview;
import consigliaViaggiDesktop.view.ReviewDetailView;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
public class ViewReviewController {

	
    private ReviewDao reviewDao;
    private ObservableList<Review> observableReviewList;
    private SearchParamsReview currentSearchParamsReview;
    private ExecutorService executor;
    private int currentAccommodationId;
    private LongProperty pageNumber;
    private LongProperty totalPageNumber;
    private LongProperty totalElementNumber;
    
    public ViewReviewController() {
        totalPageNumber= new SimpleLongProperty();
        pageNumber= new SimpleLongProperty(-1);
        totalElementNumber= new SimpleLongProperty();
        executor=initExecutor(4);
        reviewDao= new ReviewDaoJSON();
        observableReviewList= FXCollections.observableArrayList();
    }

    public LongProperty getPageNumber() {
        return pageNumber;
    }
    public LongProperty getTotalPageNumber() {
        return totalPageNumber;
    }
    public LongProperty getTotalElementNumber() {
        return  totalElementNumber;
    }


    public ObservableList<Review> getObsarvableReviewList() {
        return observableReviewList;
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
        initExecutor(4);
        Thread testThread = new Thread(task);
        executor.execute(testThread);

        return observableReviewList;
    }


    public void refreshList() throws DaoException {
        loadReviewListAsync(currentSearchParamsReview);
    }


	public void addReviewtoListAsync(int reviewId) {
    	Task task = new Task() {
    		@Override
            public Void call() throws InterruptedException {
    			
    			Review review= reviewDao.getReviewById(reviewId);
    			observableReviewList.add(review);
    			observableReviewList.notifyAll();
				return null;
            }
        };
        
        Thread testThread = new Thread(task);
        executor.execute(testThread);
     
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

    // create sublist of length size
    public List<Review> reviewSubList(List<Review> reviewList, final int size) {
        List<Review> sublist = new  ArrayList<Review>();
        for(int i=0;i<size;i++){
            sublist.add((Review) reviewList.get(i));
        }
        return sublist;
    }

    public List<Review> filterReviewList(List<Review> reviewList, float minRating, float maxRating) {

        List<Review> filteredList = new ArrayList<Review>();
        for(Review review : reviewList){
            if(review.getRating()>minRating&&review.getRating()<maxRating)
                filteredList.add(review);
        }
        return filteredList;
    }

    public List<Review> copyList(List<Review> acList) {

        List<Review> copyList= new ArrayList<Review>();
        for(Review ac : (ArrayList<Review>) acList){
            copyList.add(ac);
        }

        return copyList;
    }


	public void reviewSelected(int reviewId) {

        ReviewDetailView reviewDetailView=new ReviewDetailView();
        reviewDetailView.setId(reviewId);
        reviewDetailView.setViewReviewController(this);
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

	public ObjectProperty<Review> approveReview(int reviewId) {
        ObjectProperty<Review> updatedReview= new SimpleObjectProperty<>();
        Task task = new Task() {
            @Override
            public Void call() throws InterruptedException, DaoException {

                Review review=  reviewDao.approveReview(reviewId);
                updatedReview.setValue(review);
                NavigationController.getInstance().buildInfoBox("Valutazione recensione","Recensione approvata con successo!");
                refreshList();
                return null;
            }
        };

        Thread testThread = new Thread(task);
        executor.execute(testThread);

        return updatedReview;
    }
    public ObjectProperty<Review> rejectReview(int reviewId) {
        ObjectProperty<Review> updatedReview= new SimpleObjectProperty<>();
        Task task = new Task() {
            @Override
            public Void call() throws InterruptedException, DaoException {

                Review review=  reviewDao.rejectReview(reviewId);
                updatedReview.setValue(review);
                NavigationController.getInstance().buildInfoBox("Valutazione recensione","Recensione rigettata!");
                refreshList();
                return null;
            }
        };

        Thread testThread = new Thread(task);
        executor.execute(testThread);

        return updatedReview;

    }


    private ExecutorService initExecutor(int threadPullNumber) {
        return new ThreadPoolExecutor(threadPullNumber, threadPullNumber, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10));

    }

}
