package consigliaViaggiDesktop.view;

import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.controller.manageReview.ModerateReviewController;
import consigliaViaggiDesktop.controller.manageReview.ReviewController;
import consigliaViaggiDesktop.model.Review;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;


public class ReviewDetailView{

    @FXML	private Label username_label;
    @FXML	private Text username_text;
    @FXML	private Label accommodation_label;
    @FXML   private Text accommodation_text;
    @FXML	private Label address_label;
    @FXML   private Text address_text;
    @FXML	private Label rating_label;
    @FXML	private Text rating_text;
    @FXML	private TextArea review_content;
    
    private int reviewId;
    private ReviewController reviewController;
    private ModerateReviewController moderateReviewController;
    
    public void setId(int id) {
		reviewId=id;
	}
    
    public void setReviewController(ReviewController reviewController) {
    	this.reviewController = reviewController;
	}
    
    public void initialize() {

    	/*if(reviewController ==null) {
    		reviewController = new ReviewController();
    	}*/

		moderateReviewController=new ModerateReviewController(reviewController);
    	
    	reviewController.getReviewAsync(reviewId).addListener(new ChangeListener<Review>() {

			@Override
			public void changed(ObservableValue<? extends Review> observable, Review oldValue, Review newValue) {
				updateReviewDetailGui(newValue);		
			}
   		
    	});
    	
    }
    
    @FXML
    void backButtonClicked() {
		reviewController.goBack();
    }

    @FXML
    void approveButtonClicked() {
		if(NavigationController.getInstance().buildAlert("Confirmation Dialog","Approvare questa recensione?")) {
			ObjectProperty<Review> updatedReview= moderateReviewController.approveReview(reviewId);
			updatedReview.addListener(new ChangeListener<Review>() {
				@Override
				public void changed(ObservableValue<? extends Review> observable, Review oldValue, Review newValue) {
					updateReviewDetailGui(newValue);
				}
			});
		}
    }

	@FXML
	void rejectButtonClicked() {
		if(NavigationController.getInstance().buildAlert("Confirmation Dialog","Rigettare questa recensione?")) {
			ObjectProperty<Review> updatedReview= moderateReviewController.rejectReview(reviewId);
			updatedReview.addListener(new ChangeListener<Review>() {
				@Override
				public void changed(ObservableValue<? extends Review> observable, Review oldValue, Review newValue) {
					updateReviewDetailGui(newValue);
				}
			});
		}
	}
    
    private void updateReviewDetailGui(Review review) {
    	Platform.runLater(new Runnable(){

			@Override
			public void run() {
				username_text.setText(review.getAuthor());
				accommodation_text.setText(review.getAccommodationName());
				rating_text.setText(String.valueOf(review.getRating()));
				review_content.setText(review.getReviewText());
				
			}
			   
		});
	}
	
}

