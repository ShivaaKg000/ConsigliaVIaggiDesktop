package consigliaViaggiDesktop.view;

import java.io.IOException;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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
    	loadAccommodationView();
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
	public void loadAccommodationView() throws IOException
    {
		
		NavigationController.getInstance().navigateToView(Constants.ACCOMMODATION_VIEW);
        
    }
}
