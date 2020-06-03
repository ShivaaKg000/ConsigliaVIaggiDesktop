package consigliaViaggiDesktop.view;

import java.io.IOException;
import consigliaViaggiDesktop.controller.manageReview.ReviewController;
import consigliaViaggiDesktop.model.*;
import consigliaViaggiDesktop.model.DAO.DaoException;
import javafx.beans.property.LongProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ReviewView {
	
		@FXML private BorderPane reviewView;
		@FXML private TableView<Review> tableReview;

		@FXML private TableColumn<Review, String> author ;
		@FXML private TableColumn<Review, String> accommodationName;
		@FXML private TableColumn<Review, Integer> accommodationId;
		@FXML private TableColumn<Review, String> reviewText ;
		@FXML private TableColumn<Review, Status> approved ;
		
		private  ObservableList<Review> reviewList = FXCollections.observableArrayList();
		private ReviewController reviewController;

		private int page=0;
		private LongProperty pageNumber,totalPageNumber,totalElemntNumber;


	public void initialize() throws DaoException {
			reviewController = new ReviewController();
			
			author.setCellValueFactory(new PropertyValueFactory<Review,String>("author"));
			accommodationName.setCellValueFactory(new PropertyValueFactory<Review, String>("accommodationName"));
			accommodationId.setCellValueFactory(new PropertyValueFactory<Review, Integer>("accommodationId"));
			reviewText.setCellValueFactory(new PropertyValueFactory<Review, String>("reviewText"));
			
			approved.setCellValueFactory(new PropertyValueFactory<Review, Status>("status"));
			setApprovedCellStyle(approved);
			setTableClickEvent(tableReview);


			reviewList= reviewController.loadReviewListAsync(new SearchParamsReview.Builder().
					setCurrentpage(0).
					setCurrentSearchString("22")
					.create());

			tableReview.setItems(reviewList);
			//bindView();
				
		}
/*
	private void bindView() {

		totalPageNumber= viewReviewController.getTotalPageNumber();
		pageNumber = viewReviewController.getPageNumber();
		totalElemntNumber=viewReviewController.getTotalElementNumber();

		reviewList.addListener(new ListChangeListener<Review>() {
			@Override
			public void onChanged(Change<? extends Review> c) {

			}
		});

	}
*/

	private void setTableClickEvent(TableView<Review> table) {
			
			table.setRowFactory(tv -> {
	            TableRow<Review> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                    Review rowData = row.getItem();
	                    System.out.println("Double click on: "+rowData.getId());
	                    reviewController.reviewSelected(rowData.getId());
					
	                }
	            });
	            return row ;
	        });
			
		}

		private void setApprovedCellStyle(TableColumn<Review, Status> approved) {
			approved.setCellFactory(column -> {
				return new TableCell<Review, Status>() {
			        @Override
			        protected void updateItem(Status item, boolean empty) 
			        {
			            super.updateItem(item, empty); //This is mandatory

			            if (item == null || empty) //If the cell is empty 
			            { 
			                setText(null);
			                setStyle("");
			            } 
		            	else						//If the cell is not empty
			            {                 
			            	setText(String.valueOf(item)); //Put the String data in the cell
            				if (item==Status.PENDING) 
            				{
            					this.setTextFill(javafx.scene.paint.Paint.valueOf("#000000")); //The text in red
        						setStyle("-fx-background-color: yellow"); //The background of the cell in yellow
        					} else if (item == Status.APPROVED) 
        							{
    									this.setTextFill(javafx.scene.paint.Paint.valueOf("#00cc00")); 
        							} 
        							else	
        								setTextFill(javafx.scene.paint.Paint.valueOf("#ff0000"));
   						}
			          }
			        };
			   
			});
			
		}

		@FXML
		public void indietro(){
			//loadMenuView(UserName);
			reviewController.goBackToMenu();
		}
		@FXML
		public void cerca() { 
			//body
		}
		
		public void loadMenuView(String UserName) throws IOException
	    {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("MenuView.fxml"));
	        Parent view = loader.load();
	        Scene viewscene = new Scene(view);
	        MenuView controller = loader.getController();
	        controller.nomeUser(UserName);
	        Stage window = (Stage) reviewView.getScene().getWindow();
	        window.setScene(viewscene);
	        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
	        window.setHeight(480);
			window.setWidth(640);
			window.setY((screenBounds.getHeight() - 480) / 2);
			window.setX((screenBounds.getWidth() - 640) / 2);
	        window.show();
	    }
}
