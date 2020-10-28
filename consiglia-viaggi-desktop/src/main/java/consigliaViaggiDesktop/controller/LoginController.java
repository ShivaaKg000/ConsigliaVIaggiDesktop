package consigliaViaggiDesktop.controller;
import consigliaViaggiDesktop.model.DAO.DaoException;
import consigliaViaggiDesktop.model.DAO.LoginDao;
import consigliaViaggiDesktop.model.DAO.LoginDaoFactory;
import consigliaViaggiDesktop.model.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

public class LoginController {

	private static LoginDao loginDao;
	private static LoginController loginController;
	private User currentUser;
	private StringProperty errorMessage;

	public static LoginController getInstance(){
		if(loginController ==null) {
			loginController = new LoginController();
			loginDao= LoginDaoFactory.getLoginDao();
		}

		return loginController;
	}

	public BooleanProperty authenticate(String username, String pwd)  {
		errorMessage = new SimpleStringProperty();
		BooleanProperty loginResult = new SimpleBooleanProperty();
		Task<Boolean> task = new Task<>() {
			@Override
			public Boolean call(){

				try {
					if(loginDao.authenticate(username,pwd))
					{
						saveUserInstance(loginDao.getAuthenticatedUserToken(), username);
						return true;
					} else {
						errorMessage.set("Username/Password is not valid");
						return false;
					}
				} catch (DaoException e) {
					e.printStackTrace();
					errorMessage.set(e.getErrorMessage());
				}
				return false;
			}
		};
		task.setOnSucceeded(t -> {
			loginResult.set(task.getValue());

		});
		Thread loginThread = new Thread(task);
		loginThread.start();
		return loginResult;
	}

	private void saveUserInstance(String token, String username) {
		currentUser=new User(username);
		currentUser.setToken(token);
	}

	public StringProperty errorMessageProperty() {
		return errorMessage;
	}

	public String getCurrentUserAuthenticationToken(){
		return currentUser.getAuthToken();
	}
}
