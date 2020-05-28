package consigliaViaggiDesktop.view;
import consigliaViaggiDesktop.controller.ViewAccommodationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.Category;
import consigliaViaggiDesktop.model.SearchParams;
import consigliaViaggiDesktop.model.Subcategory;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.util.List;

public class AccommodationView {
	
	@FXML	private BorderPane accommodationView;
	@FXML 	private TableView<Accommodation> tableAccommodation;
	@FXML 	private TableColumn<Accommodation, Integer> id ;
	@FXML 	private TableColumn<Accommodation, String> name ;
	@FXML 	private TableColumn<Accommodation, String> description ;
	@FXML 	private TableColumn<Accommodation, Category> category ;
	@FXML 	private TableColumn<Accommodation, Subcategory> subCategory;
	@FXML 	private TableColumn<Accommodation, String> city;
	@FXML 	private ChoiceBox<Category> categoryChoiseBox;
	@FXML 	private ChoiceBox<Subcategory> subCategoryChoiseBox;
	@FXML	private TextField searchParamTextEdit;
	@FXML	private Label pageLabel;


	private ObservableList<Category> category_list= FXCollections.observableArrayList(Category.class.getEnumConstants());
	private ObservableList<Subcategory> subcategory_list= FXCollections.observableArrayList();

	private ObservableList<Accommodation> accommodationList = FXCollections.observableArrayList();
	private ViewAccommodationController viewAccommodationController;
	private int page=0;
	private LongProperty pageNumber,totalPageNumber,totalElemntNumber;

	
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
		subCategory.setCellValueFactory(new PropertyValueFactory<Accommodation,Subcategory>("subCategory"));
		city.setCellValueFactory(new Callback<CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Accommodation, String> accommodation) {
		         StringProperty location=new SimpleStringProperty();
		         location.setValue(accommodation.getValue().getCity());
		         return location;
		     }
		  });
		setTableClickEvent(tableAccommodation);
		accommodationList=viewAccommodationController.loadAccommodationListAsync( new SearchParams.Builder()
				.create());
		tableAccommodation.setItems(accommodationList);
		bindView();

		
	}

	private void setTableClickEvent(TableView<Accommodation> table) {
		
		table.setRowFactory(tv -> {
            TableRow<Accommodation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Accommodation rowData = row.getItem();
                    viewAccommodationController.loadAccommodationDetailView(rowData.getId());
				
                }
            });
            return row ;
        });
		
	}

	@FXML
	public void createButtonAction(){
		viewAccommodationController.loadCreateAccommodationDetailView();
	}

	@FXML
	public void backButtonClicked() {
		viewAccommodationController.goBackToMenu();
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
		searchAccommodation();
	}

	@FXML
	void searchTextFieldKeyPressed(KeyEvent event) {

		if(event.getCode().toString().equals("ENTER")){
			searchAccommodation();
		}
	}

	@FXML
	void nextPageAction(ActionEvent event) {
		viewAccommodationController.nextPage();
	}

	@FXML
	void previousPage(ActionEvent event) {
		viewAccommodationController.previousPage();
	}

	void searchAccommodation(){
		String cat="";
		String subCat="";
		if(categoryChoiseBox.getValue()!=null)
			cat=categoryChoiseBox.getValue().toString();
		if(subCategoryChoiseBox.getValue()!=null)
			subCat=subCategoryChoiseBox.getValue().toString();


		accommodationList=viewAccommodationController.loadAccommodationListAsync(
				new SearchParams.Builder()
				.setCurrentCategory(cat)
				.setCurrentSubCategory(subCat)
				.setCurrentSearchString(searchParamTextEdit.getText())
				.setCurrentpage(page)
				.create());
	}

	private void bindView() {

		totalPageNumber= viewAccommodationController.getTotalPageNumber();
		pageNumber = viewAccommodationController.getPageNumber();
		totalElemntNumber=viewAccommodationController.getTotalElementNumber();

		accommodationList.addListener(new ListChangeListener<Accommodation>() {
			@Override
			public void onChanged(Change<? extends Accommodation> c) {
				updateGui();
			}
		});

		pageNumber.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Platform.runLater(new Runnable() {

					Long page = 1+(Long)newValue;
					@Override
					public void run() {
						pageLabel.setText("Pagina: "+String.valueOf(page)+" / "+String.valueOf(totalPageNumber.getValue())
								+"                Totale strutture trovate: "+String.valueOf(totalElemntNumber.getValue()));
					}
				});
			}
		});
	}

	private void updateGui() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pageLabel.setText("Pagina: "+String.valueOf(1+(Long)pageNumber.getValue())+" / "+String.valueOf(totalPageNumber.getValue())
						+"                Totale strutture trovate: "+String.valueOf(totalElemntNumber.getValue()));
			}
		});

	}

}
