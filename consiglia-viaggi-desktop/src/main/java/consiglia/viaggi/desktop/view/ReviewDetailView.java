package consiglia.viaggi.desktop.view;

import consiglia.viaggi.desktop.controller.ViewReviewController;
import consiglia.viaggi.desktop.model.Review;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class ReviewDetailView{

    @FXML
    private Label username_label;

    @FXML
    private Text username_text;

    @FXML
    private Label accommodation_label;

    @FXML
    private Text accommodation_text;

    @FXML
    private Label address_label;

    @FXML
    private Text address_text;

    @FXML
    private Label rating_label;
    
    @FXML
    private Text rating_text;
    
    @FXML
    private TextArea review_content;
    
    private int reviewId;
    private ViewReviewController viewReviewController;
    
    public void setId(int id) {
		reviewId=id;
		
	}
    public void setViewReviewController(ViewReviewController viewReviewController) {
    	this.viewReviewController=viewReviewController;
		
	}
    
    public void initialize() {
    	System.out.println("review detail init: "+reviewId);
    	if(viewReviewController==null) {
    	
    		System.out.println("view review controller is null");
    		viewReviewController= new ViewReviewController();
    	}
    	
    	viewReviewController.getReviewAsync(reviewId).addListener(new ChangeListener<Review>() {

			@Override
			public void changed(ObservableValue<? extends Review> observable, Review oldValue, Review newValue) {
				// TODO Auto-generated method stub
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

    	viewReviewController.addReviewtoListAsync(100);
    	viewReviewController.approveReview(reviewId);
    	
    }
    
    private void updateReviewDetailGui(Review review) {
		username_text.setText(review.getAuthor());
		accommodation_text.setText(review.getNameAccommodation());
		rating_text.setText(String.valueOf(review.getRating()));
		review_content.setText(review.getReviewText());
		
	}

	
    
    

	
}

