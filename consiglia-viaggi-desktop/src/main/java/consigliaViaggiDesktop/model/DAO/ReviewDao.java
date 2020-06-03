package consigliaViaggiDesktop.model.DAO;

import consigliaViaggiDesktop.model.DTO.JsonPageResponse;
import consigliaViaggiDesktop.model.Review;
import consigliaViaggiDesktop.model.SearchParamsReview;


public interface ReviewDao {

     Review getReviewById(int id);
     JsonPageResponse<Review> getReviewList(SearchParamsReview params) throws DaoException;
     boolean postReview(Review review);
     Review approveReview(int reviewId);
     Review rejectReview(int reviewId);
}


