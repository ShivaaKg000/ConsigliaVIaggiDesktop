package consiglia.viaggi.desktop.view;

import java.io.IOException;

import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MenuView {

	@FXML
    private Label benvenuto;
    @FXML
    private BorderPane menuView;
    @FXML
    private Parent login;
    @FXML
    private Parent accommodationView;

    public String UserName;
    
    public void NomeUser(String Username){
    	this.UserName=Username;
    	benvenuto.setText("Benvenuto, "+this.UserName);
    }
    public void initialize() {
    }
    
    @FXML
    private void logout() throws IOException {
    	loadLoginView();
    }
    @FXML
    private void recensioni() throws IOException
    {
    	loadReviewView(this.UserName);
	}
    @FXML
    private void strutture() throws IOException
    {
    	loadAccommodationView(this.UserName);
	}
    public void loadReviewView(String UserName) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ReviewView.fxml"));
        Parent view = loader.load();
        Scene viewscene = new Scene(view);
        ReviewView controller = loader.getController();
        controller.NomeUser(UserName);
        Stage window = (Stage) menuView.getScene().getWindow();
        window.setScene(viewscene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        window.setHeight(700);
		window.setWidth(1180);
		window.setY((screenBounds.getHeight() - 700) / 2);
		window.setX((screenBounds.getWidth() - 1180) / 2);
        window.show();
    }
    public void loadLoginView() throws IOException
    {
        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getResource("LoginView.fxml"));
        Parent view2 = loader2.load();
        Scene viewscene = new Scene(view2);
        Stage window = (Stage) menuView.getScene().getWindow();
        window.setScene(viewscene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        window.setHeight(480);
        window.setWidth(640);
        window.setY((screenBounds.getHeight() - 480) / 2);
		window.setX((screenBounds.getWidth() - 640) / 2);
        window.show();
    }

    public void loadAccommodationView(String UserName) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AccommodationView.fxml"));
        Parent view = loader.load();
        Scene viewscene = new Scene(view);
        AccommodationView controller = loader.getController();
        controller.NomeUser(UserName);
        Stage window = (Stage) menuView.getScene().getWindow();
        window.setScene(viewscene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        window.setHeight(700);
		window.setWidth(1180);
		window.setY((screenBounds.getHeight() - 700) / 2);
		window.setX((screenBounds.getWidth() - 1180) / 2);
        window.show();
    }
}
