package consigliaViaggiDesktop.model;

import java.util.List;


public interface ReviewDao {
     List<Review> getReviewList(int id);
     Review getReviewById(int id);
     boolean postReview(Review review);
     Review approveReview(int reviewId);
     Review rejectReview(int reviewId);
}
