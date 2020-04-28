package consigliaViaggiDesktop.view;

import consigliaViaggiDesktop.controller.ViewAccommodationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.Category;
import consigliaViaggiDesktop.model.Subcategory;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

public class AccommodationDetailView {

	@FXML private Text text_id;
	@FXML private Text text_name;
	@FXML private Text text_description;
	@FXML private Text text_path;
	@FXML private Text text_rating;
	@FXML private ChoiceBox<Category> choice_category;
	@FXML private ChoiceBox<Subcategory> choice_subcategory;
	
	
	private int accommodationId;
	private ViewAccommodationController viewAccommodationController;
	private ObservableList<Category> category_list= FXCollections.observableArrayList(Category.class.getEnumConstants());
	private ObservableList<Subcategory> subcategory_list= FXCollections.observableArrayList(Subcategory.class.getEnumConstants());
	
	
	
	public void setId(int id) {
		accommodationId=id;
	}
	public void setViewAccommodationController(ViewAccommodationController viewAccommodationController) {
    	this.viewAccommodationController=viewAccommodationController;
		
	}
	
	public void initialize(){
		
		choice_category.setItems(category_list);
		choice_subcategory.setItems(subcategory_list);
		
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
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				text_id.setText(String.valueOf(accommodation.getId()));
				text_name.setText(accommodation.getName());
				text_description.setText(accommodation.getDescription());
				text_path.setText(accommodation.getLogoUrl());
				text_rating.setText(String.valueOf(accommodation.getRating()));
				choice_category.setValue(accommodation.getCategory());
				choice_subcategory.setValue(accommodation.getSubcategory());
				
			}
			   
			});
		
	}
	
	@FXML
    void backButtonClicked() {
	  viewAccommodationController.goBack();
    }

    	
    

}
