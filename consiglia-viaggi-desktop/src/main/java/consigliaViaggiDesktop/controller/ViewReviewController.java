package consigliaViaggiDesktop.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.model.Review;
import consigliaViaggiDesktop.model.ReviewDao;
import consigliaViaggiDesktop.model.ReviewDaoJSON;
import consigliaViaggiDesktop.model.ReviewDaoStub;
import consigliaViaggiDesktop.view.ReviewDetailView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
public class ViewReviewController {

	
    private ReviewDao reviewDao;
    private ObservableList<Review> observableReviewList;
    private ExecutorService executor;
    
    public ViewReviewController() {

    	executor=initExecutor(4);
        reviewDao= new ReviewDaoJSON();
        //reviewDao= new ReviewDaoStub();
        observableReviewList= FXCollections.observableArrayList();		
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
        initExecutor(4);
        Thread testThread = new Thread(task);
        executor.execute(testThread);
     
    }
    
    private ExecutorService initExecutor(int threadPullNumber) {
    	return Executors.newFixedThreadPool(threadPullNumber);
		
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

	public ObservableList<Review> getObsarvableReviewList() {
		return observableReviewList;
	}

	public void reviewSelected(int reviewId) {
		try {	
			
			ReviewDetailView reviewDetailView=new ReviewDetailView();
			reviewDetailView.setId(reviewId);
			reviewDetailView.setViewReviewController(this);
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
