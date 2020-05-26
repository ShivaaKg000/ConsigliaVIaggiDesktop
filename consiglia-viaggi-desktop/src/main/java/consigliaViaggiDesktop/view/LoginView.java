package consigliaViaggiDesktop.view;

import java.io.IOException;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.LoginController;
import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

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
    private void ok() {

    	String userName = userNameField.getText();
        String password = passwordField.getText();

        try {
            if (LoginController.getInstance().authenticate(userName, password))
            {
                errorLabel.setText("");
                loadMenuView("");
            }
            else
            {
                errorLabel.setText("Username/Password is not valid");
            }
        } catch (IOException e) {
            errorLabel.setText(e.getMessage());
        }
        clearFields();
    }

    private void clearFields() {
        userNameField.setText("");
        passwordField.setText("");
    }

    public void initialize() {
    }

    public void loadMenuView(String userName) throws IOException
    {
    	MenuView controller= new MenuView();
    	controller.nomeUser(userName);
    	NavigationController.getInstance().navigateToView(Constants.MENU_VIEW, controller);

    }

}
