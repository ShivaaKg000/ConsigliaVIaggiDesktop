package consigliaViaggiDesktop.view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import consigliaViaggiDesktop.controller.ViewAccommodationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.Category;
import consigliaViaggiDesktop.model.Subcategory;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccommodationDetailView implements MapComponentInitializedListener {

	@FXML private Text text_id;
	@FXML private TextField latitudeTextField;
	@FXML private TextField longitudeTextField;
	@FXML private TextField text_name;
	@FXML private TextField text_address;
	@FXML private TextArea text_description;
	@FXML private Text text_rating;
	@FXML private ComboBox<Category> choice_category;
	@FXML private ComboBox<Subcategory> choice_subcategory;
	@FXML private AnchorPane map_pane;
	@FXML private TextField addressTextField;
	@FXML private Button file_chooser_button;
	@FXML private ImageView accommodation_image;

	private Desktop desktop = Desktop.getDesktop();
	GoogleMapView mapView;
	private GoogleMap map;
	private GeocodingService geocodingService;
	private StringProperty address = new SimpleStringProperty();
	
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

		/*Map edit*/
		mapView = new GoogleMapView("it-IT", "AIzaSyCMh5QgPKHyXr_swIaV5JXdDkwaABIXbGU");
		AnchorPane.setBottomAnchor(mapView, 0.0);
		AnchorPane.setTopAnchor(mapView, 0.0);
		AnchorPane.setLeftAnchor(mapView, 0.0);
		AnchorPane.setRightAnchor(mapView, 0.0);
		map_pane.getChildren().add(mapView);
		mapView.toBack();
		mapView.addMapInializedListener(this);
		address.bind(addressTextField.textProperty());
		/*Map edit*/

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
				latitudeTextField.setText(accommodation.getLatitude().toString());
				longitudeTextField.setText(accommodation.getLongitude().toString());
				text_name.setText(accommodation.getName());
				text_description.setText(accommodation.getDescription());
				text_address.setText(accommodation.getAddress());
				//text_path.setText(accommodation.getLogoUrl());
				text_rating.setText(String.valueOf(accommodation.getRating()));
				choice_category.setValue(accommodation.getCategory());
				subcategory_list.addAll(dynamicSubCategoryChoice(accommodation.getCategory()));
				choice_subcategory.setItems(subcategory_list);
				choice_subcategory.setValue(accommodation.getSubcategory());

				//Add markers to the map
				LatLong accommodationLocation = new LatLong(accommodation.getLatitude(), accommodation.getLongitude());
				MarkerOptions markerOptions1 = new MarkerOptions();
				markerOptions1.position(accommodationLocation);

				Marker accommodationMarker = new Marker(markerOptions1);

				if(map!=null)
					map.addMarker( accommodationMarker );

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

		geocodingService = new GeocodingService();
		//Set the initial properties of the map.
		MapOptions mapOptions = new MapOptions();

		mapOptions.center(new LatLong(40.851799, 14.268120))
				.mapType(MapTypeIdEnum.ROADMAP)
				
				.overviewMapControl(false)
				.panControl(false)
				.rotateControl(false)
				.scaleControl(false)
				.streetViewControl(false)
				.zoomControl(false)
				.zoom(12);

		map = mapView.createMap(mapOptions);


	}

	@FXML
	public void addressTextFieldAction(ActionEvent event) {

		geocodingService.geocode(address.get(),(results, status) -> {
			System.out.print("geocoded");
			System.out.print(results[0]);
			LatLong latLong = null;

			if( status == GeocoderStatus.ZERO_RESULTS) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "No matching address found");
				alert.show();
				return;
			} else if( results.length > 1 ) {
				Alert alert = new Alert(Alert.AlertType.WARNING, "Multiple results found, showing the first one.");
				alert.show();
				latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
			} else {
				latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
			}

			map.setCenter(latLong);
		});
	}

	@FXML
	void openFileChooser(ActionEvent event) {
		/*FileChooser*/
		final FileChooser fileChooser = new FileChooser();
		configureFileChooser(fileChooser);

		File file = fileChooser.showOpenDialog(map_pane.getScene().getWindow());
		if (file != null) {
			try {
				System.out.println(file.toURI().toURL().toExternalForm());
				accommodation_image.setImage(new Image(file.toURI().toURL().toExternalForm()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}

	}

	@FXML
	void saveButtonAction(ActionEvent event) {
		if(buildAlert("Confirmation Dialog","Salvare le modifiche?")){
			/*salva dati*/
		}

	}


	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("Scegli immagine");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG", "*.png")
		);
	}

	private boolean buildAlert(String title,String header){
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			return true;
		} else {
			return false;
		}
	}
}
