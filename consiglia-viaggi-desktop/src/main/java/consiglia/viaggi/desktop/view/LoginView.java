package consiglia.viaggi.desktop.view;

import java.io.IOException;

import consiglia.viaggi.desktop.controller.LoginController;
import consiglia.viaggi.desktop.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginView {

    @FXML
    private BorderPane login;
    @FXML
    private TextField userNameField ;
    @FXML
    private PasswordField passwordField ;
    @FXML
    private Label errorLabel ;



    @FXML
    private void ok() throws IOException {

    	String userName = userNameField.getText();
        String password = passwordField.getText();

        if (LoginController.authenticate(userName, password))
        {
            User user =new User(userName);
            errorLabel.setText("");
            loadMenuView(user.getUserName());
        }
        else
        {
            errorLabel.setText("Username/Password is not valid");
        }
        clearFields();
    }

    private void clearFields() {
        userNameField.setText("");
        passwordField.setText("");
    }

    public void initialize() {
    }

    public void loadMenuView(String UserName) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MenuView.fxml"));
        Parent view = loader.load();
        Scene viewscene = new Scene(view);
        MenuView controller = loader.getController();
        controller.NomeUser(UserName);
        Stage window = (Stage) login.getScene().getWindow();
        window.setScene(viewscene);
        window.setHeight(480);
        window.setWidth(640);
        window.show();
    }

}
