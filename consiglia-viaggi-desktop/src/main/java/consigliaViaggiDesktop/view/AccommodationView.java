package consigliaViaggiDesktop.view;
import consigliaViaggiDesktop.controller.manageAccommodation.AccommodationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.Category;
import consigliaViaggiDesktop.model.SearchParamsAccommodation;
import consigliaViaggiDesktop.model.Subcategory;
import javafx.application.Platform;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class AccommodationView {

	// Elementi della vista
	@FXML   private Label errorLabel;
	@FXML   private ComboBox<OrderByChoice> orderByComboBox;
	@FXML 	private TableView<Accommodation> tableAccommodation;
	@FXML 	private TableColumn<Accommodation, Integer> id ;
	@FXML 	private TableColumn<Accommodation, String> name ;
	@FXML 	private TableColumn<Accommodation, String> description ;
	@FXML 	private TableColumn<Accommodation, Category> category ;
	@FXML 	private TableColumn<Accommodation, Subcategory> subCategory;
	@FXML 	private TableColumn<Accommodation, String> city;
	@FXML 	private ChoiceBox<Category> categoryChoiceBox;
	@FXML 	private ChoiceBox<Subcategory> subCategoryChoiceBox;
	@FXML	private TextField searchParamTextEdit;
	@FXML	private Label pageLabel;
		//@FXML	private BorderPane accommodationView;


	private static class OrderByChoice{
		private String label;
		private String param;
		private String direction;

		public OrderByChoice(String label,String param,String direction){
			this.label=label;
			this.param=param;
			this.direction=direction;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getParam() {
			return param;
		}
		public String getDirection() {return direction; }

		public static  ObservableList<AccommodationView.OrderByChoice> getList(ObservableList<AccommodationView.OrderByChoice> list_orderBy) {

			AccommodationView.OrderByChoice notOrderBy = new AccommodationView.OrderByChoice("   ", "id","DESC");
			AccommodationView.OrderByChoice orderByidDesc = new AccommodationView.OrderByChoice("Piu` recente", "id","DESC");
			AccommodationView.OrderByChoice orderByidAsc = new AccommodationView.OrderByChoice("Meno Recente", "id","ASC");
			AccommodationView.OrderByChoice orderBynameAsc = new AccommodationView.OrderByChoice("Struttura A-Z", "name","ASC");
			AccommodationView.OrderByChoice orderBynameDesc = new AccommodationView.OrderByChoice("Struttura Z-A", "name","DESC");
			AccommodationView.OrderByChoice orderByCityAsc = new AccommodationView.OrderByChoice("Citta` A-Z", "city","ASC");
			AccommodationView.OrderByChoice orderByCityDesc = new AccommodationView.OrderByChoice("Citta` Z-A", "city","DESC");

			list_orderBy.addAll(notOrderBy,
					orderByidDesc,
					orderByidAsc,
					orderBynameAsc,
					orderBynameDesc,
					orderByCityAsc,
					orderByCityDesc
					);

			return list_orderBy;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	private ObservableList<Accommodation> accommodationList;

	private AccommodationController accommodationController;

	private ObservableList<Category> category_list= FXCollections.observableArrayList(Category.class.getEnumConstants());
	private ObservableList<Subcategory> subcategory_list= FXCollections.observableArrayList();
	private ObservableList<AccommodationView.OrderByChoice> orderBy_list= FXCollections.observableArrayList();

	private int page=0;
	private LongProperty pageNumber,totalPageNumber,totalElemntNumber;

	public void initialize(){

		orderByComboBox.setItems(OrderByChoice.getList(orderBy_list));

		accommodationController = new AccommodationController();

		categoryChoiceBox.setItems(category_list);
		subCategoryChoiceBox.setItems(subcategory_list);
		/*choice_category listener*/
		categoryChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
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
		accommodationList= accommodationController.loadAccommodationListAsync( new SearchParamsAccommodation.Builder()
				.create());
		tableAccommodation.setItems(accommodationList);
		bindView();


	}

	private void searchAccommodation(){
		String cat="";
		String subCat="";
		if(categoryChoiceBox.getValue()!=null)
			cat= categoryChoiceBox.getValue().toString();
		if(subCategoryChoiceBox.getValue()!=null)
			subCat= subCategoryChoiceBox.getValue().toString();

		errorLabel.setText("");
		String searchParam=(searchParamTextEdit.getText());
		if(searchParam.length()<3 && searchParam.length()>0 ){
			searchParam="";
			errorLabel.setText("									La parola cercata deve contenere almeno tre caratteri!");
			searchParamTextEdit.clear();
		}
		accommodationController.loadAccommodationListAsync(
				new SearchParamsAccommodation.Builder()
						.setCurrentCategory(cat)
						.setCurrentSubCategory(subCat)
						.setCurrentSearchString(searchParam)
						.setCurrentpage(page)
						.setOrderBy(orderByComboBox.getValue().getParam())
						.setDirection(orderByComboBox.getValue().getDirection())
						.create());
	}

	private void bindView() {

		totalPageNumber= accommodationController.getTotalPageNumber();
		pageNumber = accommodationController.getPageNumber();
		totalElemntNumber= accommodationController.getTotalElementNumber();

		accommodationList.addListener(new ListChangeListener<Accommodation>() {
			@Override
			public void onChanged(Change<? extends Accommodation> c) {
				updateGui();
			}
		});
	}
	private void updateGui() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pageLabel.setText("Pagina: "+(1+pageNumber.getValue())+" / "+(totalPageNumber.getValue())
						+" 						Totale strutture trovate: "+(totalElemntNumber.getValue()));
			}
		});

	}

	private void setTableClickEvent(TableView<Accommodation> table) {

		table.setOnKeyPressed((KeyEvent e) -> {
			if (!table.getSelectionModel().isEmpty() && e.getCode() == KeyCode.ENTER) {
				Accommodation rowData = table.getSelectionModel().getSelectedItem();
				accommodationController.loadAccommodationDetailView(rowData.getId());
				e.consume();
			}
		});
		table.setRowFactory(tv -> {
            TableRow<Accommodation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if ( event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Accommodation rowData = row.getItem();
                    accommodationController.loadAccommodationDetailView(rowData.getId());

                }
            });
            return row ;
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

	@FXML private void searchButtonClick(ActionEvent event) {
		searchAccommodation();
	}
	@FXML private void searchTextFieldKeyPressed(KeyEvent event) {

		if(event.getCode().toString().equals("ENTER")){
			searchAccommodation();
		}
	}

	@FXML private void nextPageAction(ActionEvent event) {
		accommodationController.nextPage();
	}
	@FXML private void previousPage(ActionEvent event) {
		accommodationController.previousPage();
	}

	@FXML private void createButtonAction(){
		accommodationController.loadCreateAccommodationDetailView();
	}
	@FXML private void backButtonClicked() {
		accommodationController.goBackToMenu();
	}

}
