package consigliaViaggiDesktop.view;

import consigliaViaggiDesktop.controller.ViewAccommodationController;
import consigliaViaggiDesktop.model.Accommodation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AccommodationDetailView {

	@FXML private Text text_id;
	@FXML private Text text_name;
	@FXML private Text text_description;
	
	
	private int accommodationId;
	private ViewAccommodationController viewAccommodationController;
	
	public void setId(int id) {
		accommodationId=id;
	}
	public void setViewAccommodationController(ViewAccommodationController viewAccommodationController) {
    	this.viewAccommodationController=viewAccommodationController;
		
	}
	
	public void initialize(){
		if(viewAccommodationController==null) {
    		viewAccommodationController= new ViewAccommodationController();
    	}

    	viewAccommodationController.getAccommodationAsync(accommodationId).addListener(new ChangeListener<Accommodation>() {

			@Override
			public void changed(ObservableValue<? extends Accommodation> observable, Accommodation oldValue, Accommodation newValue) {
				updateAccommodationDetailGui(newValue);	
				
			}
   		
    	});
	}
	
	private void updateAccommodationDetailGui(Accommodation accommodation) {
		text_id.setText(String.valueOf(accommodation.getId()));
		text_name.setText(accommodation.getName());
		text_description.setText(accommodation.getDescription());
	}
	
	@FXML
    void backButtonClicked() {
	  viewAccommodationController.goBack();
    }

    	
    

}
