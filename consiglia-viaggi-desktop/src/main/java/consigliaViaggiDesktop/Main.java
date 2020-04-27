package consigliaViaggiDesktop;

import consigliaViaggiDesktop.controller.NavigationController;
import javafx.application.Application;
import javafx.stage.Stage;


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
