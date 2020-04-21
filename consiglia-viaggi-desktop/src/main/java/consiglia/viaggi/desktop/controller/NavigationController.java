package consiglia.viaggi.desktop.controller;

import java.io.IOException;
import java.util.LinkedList;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class NavigationController {
	
	private static final NavigationController navigationController = new NavigationController();
	private Stage currentStage;
	private LinkedList<Scene> previousSceneStack = new LinkedList<>();

	public static NavigationController getInstance() {
		return navigationController;
	}
	
	private NavigationController() {
		
	}

	public void navigateToView(String newViewFxml,Object controller) throws IOException
    {		
		Scene previousScene=currentStage.getScene();
		previousSceneStack.addLast(previousScene);
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(newViewFxml));
        loader.setController(controller);
        
        Parent view = loader.load();
        Scene viewscene = new Scene(view);
        currentStage.setScene(viewscene);
        //Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        currentStage.setHeight(700);
        currentStage.setWidth(1180);
        currentStage.show();
        
    }
	
	public void navigateToView(String newViewFxml) throws IOException
    {		
		Scene previousScene=currentStage.getScene();
		previousSceneStack.addLast(previousScene);
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(newViewFxml));
        Parent view = loader.load();
        Scene viewscene = new Scene(view);
        currentStage.setScene(viewscene);
        //Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        currentStage.setHeight(700);
        currentStage.setWidth(1180);
        //currentStage.setY((screenBounds.getHeight() - 700) / 2);
        //currentStage.setX((screenBounds.getWidth() - 1180) / 2);      
        currentStage.show();
        
    }
	
	public void navigateBack() {
		if(!previousSceneStack.isEmpty()) {
			currentStage.setScene(previousSceneStack.pollLast());
			currentStage.show();
		}
			
		
	}

	public Stage getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(Stage currentStage) {
		this.currentStage = currentStage;
	}

	public void showCurrentStage() {
		currentStage.show();
		
	}

}
