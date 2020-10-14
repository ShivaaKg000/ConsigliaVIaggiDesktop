package consigliaViaggiDesktop.model.DAO;

import consigliaViaggiDesktop.model.DTO.JsonPageResponse;
import consigliaViaggiDesktop.model.Review;
import consigliaViaggiDesktop.model.SearchParamsReview;


public interface ReviewDao {

     Review getReviewById(int id) throws DaoException;
     JsonPageResponse<Review> getReviewList(SearchParamsReview params) throws DaoException;
     Review approveReview(int reviewId);
     Review rejectReview(int reviewId);
     Boolean deleteReview(int reviewId) throws DaoException;
}


