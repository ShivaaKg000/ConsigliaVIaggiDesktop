package consiglia.viaggi.desktop.view;

import java.io.IOException;

import consiglia.viaggi.desktop.controller.NavigationController;
import consiglia.viaggi.desktop.controller.ViewAccommodationController;
import consiglia.viaggi.desktop.model.Accommodation;
import consiglia.viaggi.desktop.model.Category;
import consiglia.viaggi.desktop.model.Location;
import consiglia.viaggi.desktop.model.Subcategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class AccommodationView {
	
	@FXML	private BorderPane accommodationView;
	@FXML 	private TableView<Accommodation> tableAccommodation;
	@FXML 	private TableColumn<Accommodation, Integer> id ;
	@FXML 	private TableColumn<Accommodation, String> name ;
	@FXML 	private TableColumn<Accommodation, String> description ;
	@FXML 	private TableColumn<Accommodation, Category> category ;
	@FXML 	private TableColumn<Accommodation, Subcategory> subcategory;
	//@FXML 	private TableColumn<Accommodation, String> city;
	
	
	private  ObservableList<Accommodation> accommodationList = FXCollections.observableArrayList();
	private  ViewAccommodationController viewAccommodationController;

	
	public void initialize(){
		
		viewAccommodationController = new ViewAccommodationController();
		
		id.setCellValueFactory(new PropertyValueFactory<Accommodation,Integer>("id"));
		name.setCellValueFactory(new PropertyValueFactory<Accommodation, String>("name"));
		description.setCellValueFactory(new PropertyValueFactory<Accommodation, String>("description"));
		category.setCellValueFactory(new PropertyValueFactory<Accommodation, Category>("category"));
		subcategory.setCellValueFactory(new PropertyValueFactory<Accommodation,Subcategory>("subcategory"));
		//accommodationLocation.setCellValueFactory(new PropertyValueFactory<Accommodation,String>("city"));
		
		
		accommodationList=viewAccommodationController.getObsarvableAccommodationList();
		viewAccommodationController.loadAccommodationListAsync(1);
		tableAccommodation.setItems(accommodationList);
	}

	@FXML
	public void indietro() throws IOException {
		NavigationController.getInstance().navigateBack();
	}

}
