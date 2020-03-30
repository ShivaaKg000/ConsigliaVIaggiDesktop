package consiglia.viaggi.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {

	@Override
	public void start(Stage stage) {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("viewfxml/LoginView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("viewfxml/application.css").toExternalForm());
			stage.setScene(scene);
			stage.setHeight(480);
			stage.setWidth(640);
		    stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}


	public static void main(String[] args) {
		launch(args);
	}
}
