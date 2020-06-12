package consigliaViaggiDesktop.view;

import consigliaViaggiDesktop.controller.manageReview.ReviewController;
import consigliaViaggiDesktop.model.*;
import consigliaViaggiDesktop.model.DAO.DaoException;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class ReviewView {

	@FXML private Label errorLabel;
	@FXML private ComboBox<OrderByChoice> orderByComboBox;

	@FXML private TableView<Review> tableReview;
	@FXML private TableColumn<Review, Long> reviewId;
	@FXML private TableColumn<Review, String> author;
	@FXML private TableColumn<Review, String> accommodationName;
	@FXML private TableColumn<Review, Integer> accommodationId;
	@FXML private TableColumn<Review, String> reviewText;
	@FXML private TableColumn<Review, Status> status;
	@FXML private TableColumn<Review, String> creationData;

	@FXML private TextField idTextField;
	@FXML private TextField accommodationIdTextField;
	@FXML private TextField accommodationNameTextField;
	@FXML private TextField contentTextField;
	@FXML private TextField authorTextField;
	@FXML private ComboBox<String> statusComboBox;
	@FXML private Label pageLabel;

	private ObservableList<Review> reviewList = FXCollections.observableArrayList();
	private ReviewController reviewController;

	private ObservableList<String> status_list= FXCollections.observableArrayList();
	private ObservableList<OrderByChoice> orderBy_list= FXCollections.observableArrayList();

	private IntegerProperty pageNumber;
	private IntegerProperty totalPageNumber;
	private IntegerProperty totalElementNumber;

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

		public static  ObservableList<OrderByChoice> getList(ObservableList<OrderByChoice> list_orderBy) {

        	OrderByChoice notOrderBy = new OrderByChoice("   ", "id","DESC");
			OrderByChoice orderByAuthorDesc = new OrderByChoice("Autore A-Z", "nome","DESC");
			OrderByChoice orderByAuthorAsc = new OrderByChoice("Autore Z-A", "nome","ASC");
			OrderByChoice orderByIdDesc = new OrderByChoice("Piu` recente", "id","DESC");
			OrderByChoice orderByIdAsc = new OrderByChoice("Meno recente", "id","ASC");
			OrderByChoice orderByAccommodationDesc = new OrderByChoice("Struttura piu` recente", "accommodationId","DESC");
			OrderByChoice orderByAccommodationAsc = new OrderByChoice("Struttura meno recente", "accommodationId","ASC");
			OrderByChoice orderByAccommodationNameDesc = new OrderByChoice("Struttura A-Z", "accommodationName","DESC");
			OrderByChoice orderByAccommodationNameAsc = new OrderByChoice("Struttura Z-A", "accommodationName","ASC");
			OrderByChoice orderByRatingDesc = new OrderByChoice("Rating piu` alto", "rating","DESC");
			OrderByChoice orderByRatingAsc = new OrderByChoice("Rating piu` basso", "rating","ASC");

			list_orderBy.addAll(notOrderBy,
								orderByAuthorAsc,
								orderByAuthorDesc,
								orderByIdAsc,
								orderByIdDesc,
								orderByAccommodationAsc,
								orderByAccommodationDesc,
								orderByAccommodationNameAsc,
								orderByAccommodationNameDesc,
								orderByRatingAsc,
								orderByRatingDesc);

			return list_orderBy;
		}

		@Override
        public String toString() {
            return label;
        }
    }

	public void initialize() throws DaoException {

		reviewController = new ReviewController();

        orderByComboBox.setItems(OrderByChoice.getList(orderBy_list));
		orderByComboBox.getSelectionModel().select(0);

		status_list.addAll(Status.getStatusList());
		statusComboBox.setItems(status_list);
		statusComboBox.getSelectionModel().select(0);

		reviewId.setCellValueFactory(new PropertyValueFactory<Review, Long>("id"));
		author.setCellValueFactory(new PropertyValueFactory<Review, String>("author"));
		accommodationName.setCellValueFactory(new PropertyValueFactory<Review, String>("accommodationName"));
		accommodationId.setCellValueFactory(new PropertyValueFactory<Review, Integer>("accommodationId"));
		reviewText.setCellValueFactory(new PropertyValueFactory<Review, String>("reviewText"));
		creationData.setCellValueFactory(new PropertyValueFactory<Review, String>("data"));
		status.setCellValueFactory(new PropertyValueFactory<Review, Status>("status"));
		status.setCellFactory(column -> {
			return new TableCell<Review, Status>() {
				@Override
				protected void updateItem(Status item, boolean empty) {
					super.updateItem(item, empty); //This is mandatory

					if (item == null || empty) //If the cell is empty
					{
						setText(null);
						setStyle("");
					} else                        //If the cell is not empty
					{

						setText(item.label); //Put the String data in the cell
						if (item == Status.PENDING) {
							this.setTextFill(javafx.scene.paint.Paint.valueOf("#000000")); //The text in red
							setStyle("-fx-background-color: yellow ; -fx-alignment: center"); //The background of the cell in yellow

						} else if (item == Status.APPROVED) {
							this.setTextFill(javafx.scene.paint.Paint.valueOf("#00cc00"));
							setStyle("-fx-alignment: center"); //The background of the cell in white

						} else{
							setTextFill(javafx.scene.paint.Paint.valueOf("#ff0000"));
							setStyle("-fx-alignment: center"); //The background of the cell in white

						}
					}
				}
			};

		});

		setTableClickEvent(tableReview);

		reviewList = reviewController.loadReviewListAsync(new SearchParamsReview());
		tableReview.setItems(reviewList);
		bindView();

	}

	private Callback<TableColumn<Review, Status>, TableCell<Review, Status>> createStatusCellFactory() {
		return column -> {
			return new TableCell<Review, Status>() {
				@Override
				protected void updateItem(Status item, boolean empty) {
					super.updateItem(item, empty); //This is mandatory

					if (item == null || empty) //If the cell is empty
					{
						setText(null);
						setStyle("");
					} else                        //If the cell is not empty
					{
						setText(String.valueOf(item)); //Put the String data in the cell
						if (item == Status.PENDING) {
							this.setTextFill(javafx.scene.paint.Paint.valueOf("#000000")); //The text in red
							setStyle("-fx-background-color: yellow"); //The background of the cell in yellow
						} else if (item == Status.APPROVED) {
							this.setTextFill(javafx.scene.paint.Paint.valueOf("#00cc00"));
						} else
							setTextFill(javafx.scene.paint.Paint.valueOf("#ff0000"));
					}
				}
			};

		};
	}

	private void bindView() {

		totalPageNumber= reviewController.getTotalPageNumber();
		pageNumber = reviewController.getPageNumber();
		totalElementNumber = reviewController.getTotalElementNumber();

		reviewList.addListener(new ListChangeListener<Review>() {
			@Override
			public void onChanged(Change<? extends Review> c) {
				updateGui();
			}
		});
	}
	private void updateGui() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pageLabel.setText("Pagina: "+String.valueOf(1+pageNumber.getValue())+" / "+String.valueOf(totalPageNumber.getValue())
						+"                Totale recensioni trovate: "+String.valueOf(totalElementNumber.getValue()));
			}
		});

	}

	private void setTableClickEvent(TableView<Review> table) {

		table.setOnKeyPressed((KeyEvent e) -> {
			if (!table.getSelectionModel().isEmpty() && e.getCode() == KeyCode.ENTER) {
				Review rowData = table.getSelectionModel().getSelectedItem();
				reviewController.reviewSelected(rowData.getId());
				e.consume();
			}
		});
		table.setRowFactory(tv -> {
			TableRow<Review> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Review rowData = row.getItem();
					reviewController.reviewSelected(rowData.getId());

				}
			});
			return row;
		});

	}

	@FXML private void indietro() {
		reviewController.goBackToMenu();
	}
	@FXML private void cerca() throws DaoException {
		reviewController.loadReviewListAsync(buildSearchParam());
	}
	@FXML private void searchTextFieldKeyPressed(KeyEvent event) throws DaoException {

		if(event.getCode().toString().equals("ENTER")){
			reviewController.loadReviewListAsync(buildSearchParam());
		}
	}
	@FXML private void nextPageAction(ActionEvent event) throws DaoException {
		reviewController.nextPage();
	}
	@FXML private void prevPageAction(ActionEvent event) throws DaoException {
		reviewController.previousPage();
	}

	private SearchParamsReview buildSearchParam() {

		errorLabel.setText("");

		String author=(authorTextField.getText());
		if(author.length()<3 && author.length()>0 ){
			author="";
			errorLabel.setText("									La parola cercata deve contenere almeno tre caratteri!");
			authorTextField.clear();
		}
		String accommodationName=(accommodationNameTextField.getText());
		if(accommodationName.length()<3 && accommodationName.length()>0){
			accommodationName="";
			errorLabel.setText("									La parola cercata deve contenere almeno tre caratteri!");
			accommodationNameTextField.clear();
		}
		String content=(contentTextField.getText());
		if(content.length()<3 && content.length()>0){
			content="";
			errorLabel.setText("									La parola cercata deve contenere almeno tre caratteri!");
			contentTextField.clear();
		}

		return new SearchParamsReview.Builder().
						setId(idTextField.getText()).
						setAccommodationId(accommodationIdTextField.getText()).
						setAuthor(author).
						setAccommodationName(accommodationName).
						setContent(content).
						setStatus(Status.getStatusByLabel(statusComboBox.getValue())).
						setOrderBy(orderByComboBox.getValue().getParam()).
                        setDirection(orderByComboBox.getValue().getDirection()).
						build();
	}
}
