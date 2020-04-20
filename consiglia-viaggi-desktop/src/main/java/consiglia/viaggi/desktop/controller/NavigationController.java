package consiglia.viaggi.desktop.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class NavigationController {
	
	private static final NavigationController navigationController = new NavigationController();
	private Stage currentStage;

	public static NavigationController getInstance() {
		return navigationController;
	}
	
	private NavigationController() {}
	
	public void createNewView(Stage stage,String fxmlResource) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/"+fxmlResource));
        Parent view = loader.load();
        Scene viewscene = new Scene(view);
        //Stage window = (Stage) menuView.getScene().getWindow();
        stage.setScene(viewscene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setHeight(700);
        stage.setWidth(1180);
        stage.setY((screenBounds.getHeight() - 700) / 2);
        stage.setX((screenBounds.getWidth() - 1180) / 2);
        stage.show();
    }
	
	public Stage navigateToView(String newView) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(newView));
        Parent view = loader.load();
        Scene viewscene = new Scene(view);
        //Stage window = (Stage) menuView.getScene().getWindow();
        currentStage.setScene(viewscene);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        currentStage.setHeight(700);
        currentStage.setWidth(1180);
        currentStage.setY((screenBounds.getHeight() - 700) / 2);
        currentStage.setX((screenBounds.getWidth() - 1180) / 2);
        currentStage.show();
        return currentStage;
    }

	public Stage getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(Stage currentStage) {
		this.currentStage = currentStage;
	}

}
