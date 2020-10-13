package consigliaViaggiDesktop.view;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.NavigationController;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuView {

	@FXML private Text welcomeText;
    @FXML private BorderPane menuView;

    public String userName;
    
    public void nomeUser(String username){
    	this.userName=username;
    	
    }
    public void initialize() {
    	welcomeText.setText("Benvenuto, "+this.userName);
    }
    
    @FXML
    private void logout(){
        NavigationController.getInstance().navigateBack();
    }
    @FXML private void recensioni()
    {
        Stage window = (Stage) menuView.getScene().getWindow();
        NavigationController.getInstance().setCurrentStage(window);
        NavigationController.getInstance().navigateToView(Constants.REVIEW_VIEW);

	}
    @FXML
    private void strutture()
    {
		NavigationController.getInstance().navigateToView(Constants.ACCOMMODATION_VIEW);
        
    }
}
