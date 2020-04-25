package consiglia.viaggi.desktop.view;

import java.io.IOException;

import consiglia.viaggi.desktop.controller.NavigationController;
import consiglia.viaggi.desktop.controller.ViewAccommodationController;
import consiglia.viaggi.desktop.model.Accommodation;
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
	
	private  ObservableList<Accommodation> accommodationList = FXCollections.observableArrayList();
	private  ViewAccommodationController viewAccommodationController;

	
	public void initialize(){
		
		viewAccommodationController = new ViewAccommodationController();
		
		id.setCellValueFactory(new PropertyValueFactory<Accommodation,Integer>("id"));
		name.setCellValueFactory(new PropertyValueFactory<Accommodation, String>("name"));
		description.setCellValueFactory(new PropertyValueFactory<Accommodation, String>("description"));
		
		accommodationList=viewAccommodationController.getObsarvableAccommodationList();
		
		tableAccommodation.setItems(accommodationList);
		viewAccommodationController.loadAccommodationListAsync(1);
	}

	@FXML
	public void indietro() throws IOException {
		NavigationController.getInstance().navigateBack();
	}

}
