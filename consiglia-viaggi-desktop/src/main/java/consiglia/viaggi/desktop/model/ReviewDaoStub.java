package consiglia.viaggi.desktop.model;

import java.util.ArrayList;
import java.util.List;

public class ReviewDaoStub implements ReviewDao{

    @Override
    public List<Review> getReviewList(int id) {
        List<Review> reviewList=createReviewList(id);
        return reviewList;
    }

    @Override
    public Review getReviewById(int id) {
    	return createReview(id);
       
    }

    @Override
    public boolean postReview(Review review) {
        return false;
    }

    private List<Review> createReviewList(int id) {
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<Review> reviewList=new ArrayList<Review>();
        for (int i=0; i<10;i++){
            String sDate=(i+1)+"/12/2020";
            reviewList.add(new Review.Builder()
            		.setId(i)
            		.setIdAccommodation(i)
            		.setNameAccommodation("Da Peppino")
                    .setAuthor("Paolo")
                    .setReviewText("Peppino � na schifezz "+i)
                    .setRating((float) (1 + Math.random() * (5 - 1)))
                    .setData(sDate)
                    .setApproved(Status.PENDING)
                    .build()
            );

        }

        return reviewList;
    }
    
    private Review createReview(int id) {
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        String sDate=(id)+"/12/2020";
        return new Review.Builder()
        		.setId(id)
        		.setIdAccommodation(id)
        		.setNameAccommodation("Da Peppino")
                .setAuthor("Paolo")
                .setReviewText("Peppino � na schifezz "+id)
                .setRating((float) (1 + Math.random() * (5 - 1)))
                .setData(sDate)
                .setApproved(Status.PENDING)
                .build();
        
    }
}
