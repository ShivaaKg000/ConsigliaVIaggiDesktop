package consigliaViaggiDesktop.view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.controller.ViewAccommodationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.Category;
import consigliaViaggiDesktop.model.Subcategory;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;

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
	@FXML private AnchorPane imageViewAnchorPane;
	@FXML private HBox mainHbox;

	private BooleanProperty mapInitialized;
	private Desktop desktop = Desktop.getDesktop();
	private Accommodation selectedAccommodation;
	private GoogleMapView mapView;
	private GoogleMap map;
	private GeocodingService geocodingService;
	private StringProperty address = new SimpleStringProperty();
	private Integer accommodationId;

	private ViewAccommodationController viewAccommodationController;
	private ObservableList<Category> category_list= FXCollections.observableArrayList(Category.class.getEnumConstants());
	private ObservableList<Subcategory> subcategory_list= FXCollections.observableArrayList();

	public void initialize(){

		/*Map edit*/
		mapInitialized = new SimpleBooleanProperty();
		//mapView = new GoogleMapView("it-IT", "");
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
		if(accommodationId!=null)
			viewAccommodationController.getAccommodationAsync(accommodationId).addListener(new ChangeListener<Accommodation>() {

			@Override
			public void changed(ObservableValue<? extends Accommodation> observable, Accommodation oldValue, Accommodation newValue) {
				selectedAccommodation=newValue;
				updateAccommodationDetailGui(newValue);

			}

		});

	}

	@FXML
	void backButtonClicked() {
	  viewAccommodationController.goBack();
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
	void saveButtonAction(ActionEvent event) {
		if(NavigationController.getInstance().buildAlert("Confirmation Dialog","Salvare le modifiche?")){
			if(accommodationId==null){
				ObjectProperty<Accommodation> response= viewAccommodationController.createAccommodationAsync(createNewAccommodationFromNewFields());
				response.addListener(new ChangeListener<Accommodation>() {
				@Override
				public void changed(ObservableValue<? extends Accommodation> observable, Accommodation oldValue, Accommodation newValue) {
					updateAccommodationDetailGui(newValue);
					}
				});
			}
			else{
				BooleanProperty response= viewAccommodationController.editAccommodationAsync(editAccommodationFromNewFields());
				response.addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

					}
				});
			}
		}
	}

    @FXML
	void accommodationImageSelected(MouseEvent event) {
		openImageFileChooser();
	}

	@FXML
	void clearImageButtonClicked(ActionEvent event) {

		setAccommodationImage(Constants.IMG_PLACEHOLDER);

	}

	@FXML
	void deleteButtonAction(ActionEvent event){
		viewAccommodationController.deleteAccommodation(accommodationId);
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
		mapInitialized.setValue(true);

	}

	public void setId(int id) {
		accommodationId=id;
	}

	public void setViewAccommodationController(ViewAccommodationController viewAccommodationController) {
		this.viewAccommodationController=viewAccommodationController;

	}

	private Accommodation createNewAccommodationFromNewFields() {
		return new Accommodation.Builder()
				.setName(text_name.getText())
				.setImages("")
				.setCity("Napoli")
				.setAddress(text_address.getText())
				.setLongitude(Double.valueOf(longitudeTextField.getText()))
				.setLatitude(Double.valueOf(latitudeTextField.getText()))
				.setDescription(text_description.getText())
				.setCategory(choice_category.getValue())
				.setSubcategory(choice_subcategory.getValue())
				.setRating(0)
				.create();

	}
	private Accommodation editAccommodationFromNewFields() {
		return new Accommodation.Builder()
				.setId(Integer.valueOf(text_id.getText()))
				.setName(text_name.getText())
				.setImages("")
				.setCity("Napoli")
				.setAddress(text_address.getText())
				.setLongitude(Double.valueOf(longitudeTextField.getText()))
				.setLatitude(Double.valueOf(latitudeTextField.getText()))
				.setDescription(text_description.getText())
				.setCategory(choice_category.getValue())
				.setSubcategory(choice_subcategory.getValue())
				.setRating(Float.parseFloat(text_rating.getText()))
				.create();

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
				choice_subcategory.setValue(accommodation.getSubCategory());
				setAccommodationImage(accommodation.getImages());

				/*choice_category listener*//*
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

				if(mapInitialized.getValue()){
					addMapMarker(accommodation.getName(),accommodation.getLatitude(),accommodation.getLongitude());
				}else {
					mapInitialized.addListener(new ChangeListener<Boolean>() {
						@Override
						public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
							if (newValue)
								addMapMarker(accommodation.getName(), accommodation.getLatitude(), accommodation.getLongitude());
						}
					});
				}

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

	private void addMapMarker(String name,Double lat, Double lon){
		LatLong accommodationLocation = new LatLong(lat,lon);
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(accommodationLocation);
		markerOptions.title(name);
		markerOptions.visible(true);

		Marker accommodationMarker = new Marker(markerOptions);
		map.addMarker(accommodationMarker);
		map.setCenter(accommodationLocation);
	}

	private void setAccommodationImage(String url){
		System.out.println("\n imageUrl: "+url);
		BackgroundImage myBackgroundImage= new BackgroundImage(
				new Image(url,200,200,true,true),
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				BackgroundSize.DEFAULT);

		imageViewAnchorPane.setBackground(new Background(myBackgroundImage));
	}

	private void openImageFileChooser(){
		/*FileChooser*/
		final FileChooser fileChooser = new FileChooser();
		configureFileChooser(fileChooser);

		File file = fileChooser.showOpenDialog(mainHbox.getScene().getWindow());
		if (file != null) {
			try {
				setAccommodationImage(file.toURI().toURL().toExternalForm());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

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

}
