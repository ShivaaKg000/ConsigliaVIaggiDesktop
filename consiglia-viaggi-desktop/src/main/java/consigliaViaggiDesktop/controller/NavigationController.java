package consigliaViaggiDesktop.controller;

import java.io.IOException;
import java.util.LinkedList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavigationController {
	
	private static NavigationController navigationController;
	private Stage currentStage;
	private LinkedList<Scene> previousSceneStack = new LinkedList<>();

	public static NavigationController getInstance() {
		if(navigationController==null)
			navigationController= new NavigationController();
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
		currentStage.setHeight(720);
        currentStage.setWidth(1280);
	}

	public void showCurrentStage() {
		currentStage.show();
		
	}

}
