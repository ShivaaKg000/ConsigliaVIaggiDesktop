package consiglia.viaggi.desktop.view;

import java.io.IOException;

import consiglia.viaggi.desktop.Constants;
import consiglia.viaggi.desktop.controller.NavigationController;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuView {

	@FXML
    private Text welcomeText;
    @FXML
    private BorderPane menuView;
    @FXML
    private Parent login;
    @FXML
    private Parent accommodationView;

    public String userName;
    
    public void nomeUser(String username){
    	this.userName=username;
    	
    }
    public void initialize() {
    	welcomeText.setText("Benvenuto, "+this.userName);
    }
    
    @FXML
    private void logout() throws IOException {
    	loadLoginView();
    }
    @FXML
    private void recensioni() throws IOException
    {
    	loadReviewView(this.userName);
	}
    @FXML
    private void strutture() throws IOException
    {
    	loadAccommodationView(this.userName);
	}
    public void loadReviewView(String UserName) throws IOException
    {
    	Stage window = (Stage) menuView.getScene().getWindow();
    	NavigationController.getInstance().setCurrentStage(window);
    	NavigationController.getInstance().navigateToView(Constants.REVIEW_VIEW);
        
    }
    public void loadLoginView() throws IOException
    {
    	logOut();
    	NavigationController.getInstance().navigateBack();
        
    }

    private void logOut() {
		// TODO Auto-generated method stub
		
	}
	public void loadAccommodationView(String UserName) throws IOException
    {
		
		NavigationController.getInstance().navigateToView(Constants.ACCOMMODATION_VIEW);
        
    }
}
