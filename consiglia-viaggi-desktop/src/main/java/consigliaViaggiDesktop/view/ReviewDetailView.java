package consigliaViaggiDesktop.view;

import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.controller.ViewReviewController;
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
    private ViewReviewController viewReviewController;
    
    public void setId(int id) {
		reviewId=id;
	}
    
    public void setViewReviewController(ViewReviewController viewReviewController) {
    	this.viewReviewController=viewReviewController;
	}
    
    public void initialize() {
    	
    	if(viewReviewController==null) {
    		viewReviewController= new ViewReviewController();
    	}
    	
    	viewReviewController.getReviewAsync(reviewId).addListener(new ChangeListener<Review>() {

			@Override
			public void changed(ObservableValue<? extends Review> observable, Review oldValue, Review newValue) {
				updateReviewDetailGui(newValue);		
			}
   		
    	});
    	
    }
    
    @FXML
    void backButtonClicked() {
		viewReviewController.goBack();
    }

    @FXML
    void approveButtonClicked() {
		if(NavigationController.getInstance().buildAlert("Confirmation Dialog","Approvare questa recensione?")) {
			ObjectProperty<Review> updatedReview= viewReviewController.approveReview(reviewId);
			updatedReview.addListener(new ChangeListener<Review>() {
				@Override
				public void changed(ObservableValue<? extends Review> observable, Review oldValue, Review newValue) {
					System.out.print("\n Review Updated \n"+
							newValue.getStatus());

				}
			});
		}
    }

	@FXML
	void rejectButtonClicked() {
		if(NavigationController.getInstance().buildAlert("Confirmation Dialog","Rigettare questa recensione?")) {
			ObjectProperty<Review> updatedReview= viewReviewController.rejectReview(reviewId);
			updatedReview.addListener(new ChangeListener<Review>() {
				@Override
				public void changed(ObservableValue<? extends Review> observable, Review oldValue, Review newValue) {
					System.out.print("\n Review Updated \n"+
							newValue.getStatus());

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

