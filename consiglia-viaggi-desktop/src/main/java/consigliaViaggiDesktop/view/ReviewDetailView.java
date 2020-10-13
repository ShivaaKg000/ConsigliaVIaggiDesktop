package consigliaViaggiDesktop.view;

import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.controller.manageReview.ModerateReviewController;
import consigliaViaggiDesktop.controller.manageReview.ReviewController;
import consigliaViaggiDesktop.model.Review;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;


public class ReviewDetailView{

	@FXML	private Text idText;
    @FXML	private Text authorText;
    @FXML   private Text accommodationText;
    @FXML   private Text statusText;
    @FXML	private Text ratingText;
	@FXML	private Text dateText;
    @FXML	private TextArea reviewContentTextArea;
    
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

		moderateReviewController=new ModerateReviewController(reviewController);
    	
    	reviewController.getReviewAsync(reviewId).addListener((observable, oldValue, newValue) -> updateReviewDetailGui(newValue));
    	
    }
    
    @FXML
    void backButtonClicked() {
		reviewController.goBack();
    }

    @FXML
    void approveButtonClicked() {
		if(NavigationController.getInstance().buildAlert("Confirmation Dialog","Approvare questa recensione?")) {
			ObjectProperty<Review> updatedReview= moderateReviewController.approveReview(reviewId);
			updatedReview.addListener((observable, oldValue, newValue) -> updateReviewDetailGui(newValue));
		}
    }

	@FXML
	void rejectButtonClicked() {
		if(NavigationController.getInstance().buildAlert("Confirmation Dialog","Rigettare questa recensione?")) {
			ObjectProperty<Review> updatedReview= moderateReviewController.rejectReview(reviewId);
			updatedReview.addListener((observable, oldValue, newValue) -> updateReviewDetailGui(newValue));
		}
	}
    
    private void updateReviewDetailGui(Review review) {
    	Platform.runLater(() -> {
			idText.setText(String.valueOf(review.getId()));
			authorText.setText(review.getAuthor());
			accommodationText.setText(review.getAccommodationName());
			statusText.setText(review.getStatusLabel());
			ratingText.setText(String.valueOf(review.getRating()));
			dateText.setText(review.getData());
			reviewContentTextArea.setText(review.getReviewText());

		});
	}
	
}

