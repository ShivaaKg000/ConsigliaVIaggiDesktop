package consiglia.viaggi.desktop.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AccommodationView {
	
	@FXML
    private BorderPane accommodationView;
	@FXML 
	private Parent menuView;
	
	private String UserName;

	public void NomeUser(String Username){
	    	UserName=Username;
	}
	
	public void initialize(){
		//body
	}

	@FXML
	public void indietro() throws IOException {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MenuView.fxml"));
        Parent view = loader.load();
        Scene viewscene = new Scene(view);
        MenuView controller = loader.getController();
        controller.NomeUser(UserName);
        Stage window = (Stage) accommodationView.getScene().getWindow();
        window.setScene(viewscene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        window.setHeight(480);
		window.setWidth(640);
		window.setY((screenBounds.getHeight() - 480) / 2);
		window.setX((screenBounds.getWidth() - 640) / 2);
        window.show();
	}

}
