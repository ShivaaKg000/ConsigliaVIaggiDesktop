package consiglia.viaggi.desktop.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import consiglia.viaggi.desktop.Constants; 
import consiglia.viaggi.desktop.model.Review;
import consiglia.viaggi.desktop.model.ReviewDao;
import consiglia.viaggi.desktop.model.ReviewDaoStub;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class ViewReviewController {

    private ReviewDao reviewDao;
    private ObservableList observableReviewList;
    
    public ViewReviewController() {

        reviewDao= new ReviewDaoStub();
        observableReviewList= FXCollections.observableArrayList();		
    }

    /*public List<Review> getReviewList(int accommodationId) {
        List<Review> reviewList= reviewDao.getReviewList(accommodationId);
        return reviewList;
    }*/
    
    public void loadReviewListAsync(int accommodationId) {
    	Task task = new Task() {
    		@Override
            public Void call() throws InterruptedException {
    			
    			List<Review> reviewList= reviewDao.getReviewList(accommodationId);
    			observableReviewList.addAll(reviewList);
    			observableReviewList.notifyAll();
				return null;
            }
        };
        Thread testThread = new Thread(task);
        testThread.start();
     
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
        testThread.start();
     
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
}
