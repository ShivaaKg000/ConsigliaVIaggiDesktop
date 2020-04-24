package consiglia.viaggi.desktop;

import consiglia.viaggi.desktop.controller.NavigationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {

	@Override
	public void start(Stage stage) {
		try {
			
			NavigationController.getInstance().setCurrentStage(stage);
			
			NavigationController.getInstance().navigateToView(Constants.LOGIN_VIEW);
		    stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}


	public static void main(String[] args) {
		launch(args);
	}
}
