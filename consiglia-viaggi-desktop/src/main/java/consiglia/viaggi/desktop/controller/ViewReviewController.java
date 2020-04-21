package consiglia.viaggi.desktop.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import consiglia.viaggi.desktop.Constants; 
import consiglia.viaggi.desktop.model.Review;
import consiglia.viaggi.desktop.model.ReviewDao;
import consiglia.viaggi.desktop.model.ReviewDaoStub;
import consiglia.viaggi.desktop.view.ReviewDetailView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
public class ViewReviewController {

	
	private static final ViewReviewController viewReviewController = new ViewReviewController();
    private ReviewDao reviewDao;
    private ObservableList<Review> observableReviewList;
    private ExecutorService executor;
    
    private ViewReviewController() {

    	
        reviewDao= new ReviewDaoStub();
        observableReviewList= FXCollections.observableArrayList();		
    }
    
    public static ViewReviewController getInstance() {
    	return viewReviewController;
    }

    public void loadReviewListAsync(int accommodationId) {
    	
    	observableReviewList.clear();
    	Task task = new Task() {
    		@Override
            public Void call() throws InterruptedException {
    			
    			List<Review> reviewList= reviewDao.getReviewList(accommodationId);
    			observableReviewList.addAll(reviewList);
    			observableReviewList.notifyAll();
				return null;
            }
        };
        initExecutor();
        Thread testThread = new Thread(task);
        executor.execute(testThread);
     
    }
    
    private ExecutorService initExecutor() {
    	executor=Executors.newFixedThreadPool(4);
		return null;
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
    
    public ObservableList<Review> getReviewAsync(int reviewId) {
    	ObservableList<Review> observableReview= FXCollections.observableArrayList();
    	Task task = new Task() {
    		@Override
            public Void call() throws InterruptedException {
    			
    			Review review= reviewDao.getReviewById(reviewId);
    			observableReview.add(review);
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

    public List<Review> orderReviewListByDate(List<Review> reviewList){
            Collections.sort(reviewList, Collections.reverseOrder());
        return reviewList;
    }
    public List<Review> orderReviewListByRating(List<Review> reviewList, int order){
        if(order== Constants.ASCENDING)
            Collections.sort(reviewList, new Review.ReviewRatingComparator());
        else if (order == Constants.DESCENDING)
            Collections.sort(reviewList, Collections.reverseOrder (new Review.ReviewRatingComparator()));
        return reviewList;
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

	public ObservableList getObsarvableReviewList() {
		return observableReviewList;
	}

	public void reviewSelected(int reviewId) {
		try {	
			
			ReviewDetailView reviewDetailView=new ReviewDetailView();
			reviewDetailView.setId(reviewId);
			NavigationController.getInstance().navigateToView(Constants.REVIEW_DETAIL_VIEW,reviewDetailView);
			System.out.println("review selected: "+reviewId);
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void goBack() {
		executor.shutdownNow();
    	NavigationController.getInstance().navigateBack();
		
	}

	public void approveReview(int reviewId) {
		reviewDao.approveReview(reviewId);
		
	}

}
