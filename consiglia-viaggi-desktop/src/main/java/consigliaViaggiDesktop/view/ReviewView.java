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
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class ReviewView {

	@FXML private BorderPane reviewView;

	@FXML private ComboBox<String> orderByComboBox;

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

	private ObservableList<String> orderBy_list= FXCollections.observableArrayList();

	private int page = 0;
	private IntegerProperty pageNumber;
	private IntegerProperty totalPageNumber;
	private IntegerProperty totalElemntNumber;


	public void initialize() throws DaoException {

		reviewController = new ReviewController();

		orderBy_list.addAll("","nome","id","accommodation_id","accommodation_name","content","stato","rating","creation_data");
		orderByComboBox.setItems(orderBy_list);
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
		totalElemntNumber= reviewController.getTotalElementNumber();

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
						+"                Totale recensioni trovate: "+String.valueOf(totalElemntNumber.getValue()));
			}
		});

	}

	private void setTableClickEvent(TableView<Review> table) {

		table.setRowFactory(tv -> {
			TableRow<Review> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Review rowData = row.getItem();
					System.out.println("Double click on: " + rowData.getId());
					reviewController.reviewSelected(rowData.getId());

				}
			});
			return row;
		});

	}

	@FXML public void indietro() {
		reviewController.goBackToMenu();
	}
	@FXML public void cerca() throws DaoException {
		reviewController.loadReviewListAsync(buildSearchParam());
	}

	@FXML
	void nextPageAction(ActionEvent event) throws DaoException {
		reviewController.nextPage();
	}

	@FXML
	void prevPageAction(ActionEvent event) throws DaoException {
		reviewController.previousPage();
	}

	private SearchParamsReview buildSearchParam() {
		return new SearchParamsReview.Builder().
						setId(idTextField.getText()).
						setAccommodationId(accommodationIdTextField.getText()).
						setAuthor(authorTextField.getText()).
						setAccommodationName(accommodationNameTextField.getText()).
						setContent(contentTextField.getText()).
						setStatus(Status.getStatusByLabel(statusComboBox.getValue())).
						setCurrentPage(page).
						setOrderBy(orderByComboBox.getValue()).
                        setDirection("DESC").
						build();
	}
}
