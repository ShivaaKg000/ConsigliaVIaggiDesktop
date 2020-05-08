package consigliaViaggiDesktop.view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
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
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class AccommodationDetailView implements MapComponentInitializedListener {

	@FXML private Text text_id;
	@FXML private Text text_name;
	@FXML private TextArea text_description;
	//@FXML private Text text_path;
	@FXML private Text text_rating;
	@FXML private ComboBox<Category> choice_category;
	@FXML private ComboBox<Subcategory> choice_subcategory;
	@FXML private GoogleMapView mapView;

	private GoogleMap map;
	
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

		mapView.addMapInializedListener(this);

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
				//text_path.setText(accommodation.getLogoUrl());
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


	@Override
	public void mapInitialized() {
		LatLong joeSmithLocation = new LatLong(47.6197, -122.3231);
		LatLong joshAndersonLocation = new LatLong(47.6297, -122.3431);
		LatLong bobUnderwoodLocation = new LatLong(47.6397, -122.3031);
		LatLong tomChoiceLocation = new LatLong(47.6497, -122.3325);
		LatLong fredWilkieLocation = new LatLong(47.6597, -122.3357);


		//Set the initial properties of the map.
		MapOptions mapOptions = new MapOptions();

		mapOptions.center(new LatLong(47.6097, -122.3331))

				.overviewMapControl(false)
				.panControl(false)
				.rotateControl(false)
				.scaleControl(false)
				.streetViewControl(false)
				.zoomControl(false)
				.zoom(12);

		map = mapView.createMap(mapOptions);

		//Add markers to the map
		MarkerOptions markerOptions1 = new MarkerOptions();
		markerOptions1.position(joeSmithLocation);

		MarkerOptions markerOptions2 = new MarkerOptions();
		markerOptions2.position(joshAndersonLocation);

		MarkerOptions markerOptions3 = new MarkerOptions();
		markerOptions3.position(bobUnderwoodLocation);

		MarkerOptions markerOptions4 = new MarkerOptions();
		markerOptions4.position(tomChoiceLocation);

		MarkerOptions markerOptions5 = new MarkerOptions();
		markerOptions5.position(fredWilkieLocation);

		Marker joeSmithMarker = new Marker(markerOptions1);
		Marker joshAndersonMarker = new Marker(markerOptions2);
		Marker bobUnderwoodMarker = new Marker(markerOptions3);
		Marker tomChoiceMarker= new Marker(markerOptions4);
		Marker fredWilkieMarker = new Marker(markerOptions5);

		map.addMarker( joeSmithMarker );
		map.addMarker( joshAndersonMarker );
		map.addMarker( bobUnderwoodMarker );
		map.addMarker( tomChoiceMarker );
		map.addMarker( fredWilkieMarker );

	}
}
