package consigliaViaggiDesktop.view;

import java.io.IOException;

import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.controller.ViewAccommodationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.Category;
import consigliaViaggiDesktop.model.Subcategory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class AccommodationView {
	
	@FXML	private BorderPane accommodationView;
	@FXML 	private TableView<Accommodation> tableAccommodation;
	@FXML 	private TableColumn<Accommodation, Integer> id ;
	@FXML 	private TableColumn<Accommodation, String> name ;
	@FXML 	private TableColumn<Accommodation, String> description ;
	@FXML 	private TableColumn<Accommodation, Category> category ;
	@FXML 	private TableColumn<Accommodation, Subcategory> subcategory;
	@FXML 	private TableColumn<Accommodation, String> city;
	
	private  ObservableList<Accommodation> accommodationList = FXCollections.observableArrayList();
	private ViewAccommodationController viewAccommodationController;

	
	public void initialize(){
		
		viewAccommodationController = new ViewAccommodationController();
		
		id.setCellValueFactory(new PropertyValueFactory<Accommodation,Integer>("id"));
		name.setCellValueFactory(new PropertyValueFactory<Accommodation, String>("name"));
		description.setCellValueFactory(new PropertyValueFactory<Accommodation, String>("description"));
		category.setCellValueFactory(new PropertyValueFactory<Accommodation, Category>("category"));
		subcategory.setCellValueFactory(new PropertyValueFactory<Accommodation,Subcategory>("subcategory"));
		city.setCellValueFactory(new Callback<CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Accommodation, String> accommodation) {
		         StringProperty location=new SimpleStringProperty();
		         location.setValue(accommodation.getValue().getCity());
		         return location;
		     }
		  });
		setTableClickEvent(tableAccommodation);
		
		accommodationList=viewAccommodationController.getObsarvableAccommodationList();
		viewAccommodationController.loadAccommodationListAsync(1);
		tableAccommodation.setItems(accommodationList);
		
	}

	private void setTableClickEvent(TableView<Accommodation> table) {
		
		table.setRowFactory(tv -> {
            TableRow<Accommodation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Accommodation rowData = row.getItem();
                    viewAccommodationController.accommodationSelected(rowData.getId());
				
                }
            });
            return row ;
        });
		
	}
	
	@FXML
	public void backButtonClicked() {
		NavigationController.getInstance().navigateBack();
	}

}
