package consiglia.viaggi.desktop.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import consiglia.viaggi.desktop.Constants; 
import consiglia.viaggi.desktop.model.Review;
import consiglia.viaggi.desktop.model.ReviewDao;
import consiglia.viaggi.desktop.model.ReviewDaoStub;

public class ViewReviewController {

    private ReviewDao reviewDao;
    public ViewReviewController() {

        reviewDao= new ReviewDaoStub();
    }

    public List<Review> getReviewList(int accommodationId) {
        List<Review> reviewList= reviewDao.getReviewList(accommodationId);
        return reviewList;
    }

    // create sublist of length size
    public List<Review> reviewSubList(List<Review> reviewList, final int size) {
        List<Review> sublist = new  ArrayList<Review>();
        for(int i=0;i<size;i++){
            sublist.add((Review) reviewList.get(i));
        }
        return sublist;
    }

    public List<Review> orderReviewListByDate(List reviewList){
            Collections.sort(reviewList, Collections.reverseOrder());
        return reviewList;
    }
    public List orderReviewListByRating(List reviewList, int order){
        if(order== Constants.ASCENDING)
            Collections.sort(reviewList, new Review.ReviewRatingComparator());
        else if (order == Constants.DESCENDING)
            Collections.sort(reviewList, Collections.reverseOrder (new Review.ReviewRatingComparator()));
        return reviewList;
    }

    public List filterReviewList(List<Review> reviewList, float minRating, float maxRating) {

        List filteredList = new ArrayList();
        for(Review review : reviewList){
            if(review.getRating()>minRating&&review.getRating()<maxRating)
                filteredList.add(review);
        }
        return filteredList;
    }

    public List copyList(List acList) {

        List copyList= new ArrayList();
        for(Review ac : (ArrayList<Review>) acList){
            copyList.add(ac);
        }

        return copyList;
    }
}
