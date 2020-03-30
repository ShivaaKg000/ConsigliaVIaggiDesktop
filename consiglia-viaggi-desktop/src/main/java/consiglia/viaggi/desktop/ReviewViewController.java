package consiglia.viaggi.desktop;

import java.io.IOException;
import consiglia.viaggi.desktop.Controller_Review;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
public class ReviewViewController {

		@FXML BorderPane reviewView;

		@FXML TableView<Review> TableReview;

		@FXML private TableColumn<Review, String> testo ;
		@FXML private TableColumn<Review, Integer> id ;
		@FXML private TableColumn<Review, Integer> idAccomodation ;

		private String UserName;

		public void NomeUser(String Username){
		    	UserName=Username;
		}

		public void initialize()
		{
			testo.setCellValueFactory(new PropertyValueFactory<Review, String>("testo"));
			id.setCellValueFactory(new PropertyValueFactory<Review, Integer>("id"));
			idAccomodation.setCellValueFactory(new PropertyValueFactory<Review, Integer>("idAccomodation"));

			reviewList.add(new Review("Ciro",1,2));
			reviewList.add(new Review("Cirro",11,22));

	        TableReview.setItems(reviewList);
			//Controller_Review.recuperaRecensioni();

		}

		private final ObservableList<Review> reviewList = FXCollections.observableArrayList();


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
	        loader.setLocation(getClass().getResource("viewfxml/MenuView.fxml"));
	        Parent view = loader.load();

	        Scene viewscene = new Scene(view);

	        //access the controller and call a method
	        MenuViewController controller = loader.getController();
	        controller.NomeUser(UserName);

	        Stage window = (Stage) reviewView.getScene().getWindow();
	        window.setScene(viewscene);
	        window.setHeight(480);
			window.setWidth(640);
	        window.show();
	    }
}
