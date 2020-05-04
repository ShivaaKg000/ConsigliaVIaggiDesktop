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
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;

public class AccommodationDetailView {

	@FXML private Text text_id;
	@FXML private Text text_name;
	@FXML private Text text_description;
	@FXML private Text text_path;
	@FXML private Text text_rating;
	@FXML private ComboBox<Category> choice_category;
	@FXML private ComboBox<Subcategory> choice_subcategory;
	
	
	private int accommodationId;
	private ViewAccommodationController viewAccommodationController;
	private ObservableList<Category> category_list= FXCollections.observableArrayList(Category.class.getEnumConstants());
	private ObservableList<Subcategory> subcategory_list= FXCollections.observableArrayList();
	
	
	
	public void setId(int id) {
		accommodationId=id;
	}
	public void setViewAccommodationController(ViewAccommodationController viewAccommodationController) {
    	this.viewAccommodationController=viewAccommodationController;
		
	}
	
	public void initialize(){
		
		choice_category.setItems(category_list);
		choice_subcategory.setItems(subcategory_list);

		/*choice_category listener*/
		choice_category.getSelectionModel().selectedIndexProperty().addListener(
				new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
						System.out.print("selected "+category_list.get((Integer) newValue));
						subcategory_list.clear();
						subcategory_list.addAll(dynamicSubCategoryChoice(category_list.get((Integer) newValue)));

					}
				}

		);
		/*choice_category listener end*/
		
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
				subcategory_list.addAll(dynamicSubCategoryChoice(accommodation.getCategory()));
				choice_subcategory.setItems(subcategory_list);
				choice_subcategory.setValue(accommodation.getSubcategory());
				
			}
			   
			});
		
	}

	private ObservableList<Subcategory> dynamicSubCategoryChoice(Category category) {


		switch (category){
			case HOTEL:
				return FXCollections.observableArrayList(Subcategory.hotels);
			case RESTAURANT:
				return FXCollections.observableArrayList(Subcategory.restaurants);
			case ATTRACTION:
				return FXCollections.observableArrayList(Subcategory.attractions);
			default:
				return FXCollections.observableArrayList();
		}
	}

	@FXML
	void backButtonClicked() {
	  viewAccommodationController.goBack();
    }

    	
    

}
