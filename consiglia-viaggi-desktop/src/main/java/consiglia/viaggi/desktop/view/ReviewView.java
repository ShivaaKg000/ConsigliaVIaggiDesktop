package consiglia.viaggi.desktop.view;

import java.io.IOException;

import consiglia.viaggi.desktop.controller.ViewReviewController;
import consiglia.viaggi.desktop.model.Review;
import consiglia.viaggi.desktop.model.ReviewOld;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

		@FXML BorderPane reviewView;
		@FXML TableView<Review> TableReview;
		
		@FXML private TableColumn<Review, String> testo ;
		@FXML private TableColumn<Review, Integer> id ;
		@FXML private TableColumn<Review, Integer> idAccomodation ;

		private  ObservableList<Review> reviewList = FXCollections.observableArrayList();

		private  ViewReviewController viewReviewController;
		
		private String UserName;

		public void NomeUser(String Username){
		    	UserName=Username;
		}

		public void initialize()
		{
			testo.setCellValueFactory(new PropertyValueFactory<Review,String>("reviewText"));
			id.setCellValueFactory(new PropertyValueFactory<Review, Integer>("id"));
			idAccomodation.setCellValueFactory(new PropertyValueFactory<Review, Integer>("idAccomodation"));
			
			viewReviewController = new ViewReviewController();
			//reviewList.addAll(viewReviewController.getReviewList(1));
			//TableReview.setItems(reviewList);
			
			/*bind di reviewList all'observable nel controller*/
			reviewList=viewReviewController.getObsarvableReviewList();
			
			/*set del listener per intercettare il notify dell'observable*/
			reviewList.addListener(new InvalidationListener() {
				
				@Override
				public void invalidated(Observable observable) {
				
					TableReview.setItems(reviewList);
					
				}
			});;
			
			
			/*richiesta al controller di fare l'update della lista in background*/
			viewReviewController.loadReviewListAsync(1);
				

		}

		

		@FXML
		public void indietro() throws IOException{
			loadMenuView(UserName);
		}

		@FXML
		public void cerca() {

			


		}

		public void loadMenuView(String UserName) throws IOException
	    {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("view/MenuView.fxml"));
	        Parent view = loader.load();

	        Scene viewscene = new Scene(view);

	        //access the controller and call a method
	        MenuView controller = loader.getController();
	        controller.NomeUser(UserName);

	        Stage window = (Stage) reviewView.getScene().getWindow();
	        window.setScene(viewscene);
	        window.setHeight(480);
			window.setWidth(640);
	        window.show();
	    }
}
