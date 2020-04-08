package consiglia.viaggi.desktop.view;


import java.awt.Checkbox;
import java.io.IOException;

import consiglia.viaggi.desktop.controller.ViewReviewController;
import consiglia.viaggi.desktop.model.Review;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
public class ReviewView {

	
		@FXML private BorderPane reviewView;
		@FXML private TableView<Review> TableReview;
		@FXML private TableColumn<Review, String> author ;
		@FXML private TableColumn<Review, String> nameAccommodation ;
		@FXML private TableColumn<Review, Integer> idAccomodation ;
		@FXML private TableColumn<Review, String> reviewText ;
		@FXML private TableColumn<Review, Boolean> approved ;
		
		private  ObservableList<Review> reviewList = FXCollections.observableArrayList();

		private  ViewReviewController viewReviewController;
		
		private String UserName;

		public void NomeUser(String Username){
		    	UserName=Username;
		}

		public void initialize()
		{

			viewReviewController = new ViewReviewController();
			
			author.setCellValueFactory(new PropertyValueFactory<Review,String>("author"));
			nameAccommodation.setCellValueFactory(new PropertyValueFactory<Review, String>("nameAccommodation"));
			idAccomodation.setCellValueFactory(new PropertyValueFactory<Review, Integer>("idAccommodation"));
			reviewText.setCellValueFactory(new PropertyValueFactory<Review, String>("reviewText"));
			
			approved.setCellValueFactory(new PropertyValueFactory<Review, Boolean>("approve"));
			approved.setCellFactory(CheckBoxTableCell.forTableColumn(approved));  
			
			
			viewReviewController = new ViewReviewController();
			
			/*bind di reviewList all'observable nel controller*/
			reviewList=viewReviewController.getObsarvableReviewList();
			TableReview.setItems(reviewList);
			
			/*set del listener per intercettare il notify dell'observable
			reviewList.addListener(new InvalidationListener() {
				
				@Override
				public void invalidated(Observable observable) {
				
					TableReview.setItems(reviewList);
					
				}
			});;*/
			
			
			/*richiesta al controller di fare l'update della lista in background*/
			viewReviewController.loadReviewListAsync(1);
			/*test modifica lista aggiungendo una nuova riga*/
			viewReviewController.addReviewtoListAsync(11);
				
		}

		
		@FXML
		public void indietro() throws IOException{
			loadMenuView(UserName);
		}
		@FXML
		public void cerca() { 
			//body
		}
		

		public void loadMenuView(String UserName) throws IOException
	    {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("view/MenuView.fxml"));
	        Parent view = loader.load();
	        Scene viewscene = new Scene(view);
	        MenuView controller = loader.getController();
	        controller.NomeUser(UserName);
	        Stage window = (Stage) reviewView.getScene().getWindow();
	        window.setScene(viewscene);
	        window.setHeight(480);
			window.setWidth(640);
	        window.show();
	    }
}
