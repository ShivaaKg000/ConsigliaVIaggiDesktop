package consigliaViaggiDesktop.view;

import java.awt.*;
import java.io.IOException;

import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.controller.ViewAccommodationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.Category;
import consigliaViaggiDesktop.model.Subcategory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
	@FXML 	private ChoiceBox<Category> categoryChoiseBox;
	@FXML 	private ChoiceBox<Subcategory> subCategoryChoiseBox;
	@FXML	private TextField searchParamTextEdit;

	private ObservableList<Category> category_list= FXCollections.observableArrayList(Category.class.getEnumConstants());
	private ObservableList<Subcategory> subcategory_list= FXCollections.observableArrayList();

	private  ObservableList<Accommodation> accommodationList = FXCollections.observableArrayList();
	private ViewAccommodationController viewAccommodationController;

	
	public void initialize(){
		
		viewAccommodationController = new ViewAccommodationController();

		categoryChoiseBox.setItems(category_list);
		subCategoryChoiseBox.setItems(subcategory_list);
		/*choice_category listener*/
		categoryChoiseBox.getSelectionModel().selectedIndexProperty().addListener(
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
		
		//accommodationList=viewAccommodationController.getObsarvableAccommodationList();
		accommodationList=viewAccommodationController.loadAccommodationListAsync(
				"",
				"",
				"Napoli");
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
	void searchButtonClick(ActionEvent event) {
		String cat="";
		String subCat="";
		if(categoryChoiseBox.getValue()!=null)
			cat=categoryChoiseBox.getValue().toString();
		if(subCategoryChoiseBox.getValue()!=null)
			subCat=subCategoryChoiseBox.getValue().toString();


		accommodationList=viewAccommodationController.loadAccommodationListAsync(
				cat,
				subCat,
				searchParamTextEdit.getText());
	}

}
