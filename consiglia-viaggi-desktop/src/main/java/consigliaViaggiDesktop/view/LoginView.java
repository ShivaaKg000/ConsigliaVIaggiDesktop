package consigliaViaggiDesktop.view;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.LoginController;
import consigliaViaggiDesktop.controller.NavigationController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginView {


    @FXML private TextField userNameField ;
    @FXML private PasswordField passwordField ;
    @FXML private Label errorLabel ;

    @FXML
    private void ok() {

    	String userName = userNameField.getText();
        String password = passwordField.getText();

        LoginController.getInstance().authenticate(userName, password).addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                Platform.runLater(() -> {
                    errorLabel.setText("");
                    loadMenuView(userName);
                });
            }
        });

       LoginController.getInstance().errorMessageProperty().addListener((observable, oldValue, newValue) -> {
           Platform.runLater(() -> {
               errorLabel.setText(newValue);
           });

       });

        clearFields();
    }

    private void clearFields() {
        userNameField.setText("");
        passwordField.setText("");
    }

    public void initialize() {
    }

    public void loadMenuView(String userName) {
    	MenuView controller= new MenuView();
    	controller.nomeUser(userName);
    	NavigationController.getInstance().navigateToView(Constants.MENU_VIEW, controller);

    }

}
