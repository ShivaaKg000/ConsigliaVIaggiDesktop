package consigliaViaggiDesktop.model.DAO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginDaoSpringTest {

    @Test
    void authenticateWithCorrectUserAndPassword() {
        LoginDaoSpring loginDaoSpring = new LoginDaoSpring();
        try {
            assertEquals(true, loginDaoSpring.authenticate("ciro","ciro" ),"Login must be true");
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @Test
    void authenticateWithCorrectUserAndWrongPassword() {
        LoginDaoSpring loginDaoSpring = new LoginDaoSpring();
        try {
            assertEquals(false, loginDaoSpring.authenticate("ciro","wrong" ),"Wrong password");
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @Test
    void authenticateWithWrongUserAndRightPassword() {
        LoginDaoSpring loginDaoSpring = new LoginDaoSpring();
        try {
            assertEquals(false, loginDaoSpring.authenticate("ciruz","ciro" ),"Wrong user");
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @Test
    void authenticateWithWrongUserAndWrongPassword() {
        LoginDaoSpring loginDaoSpring = new LoginDaoSpring();
        try {
            assertEquals(false, loginDaoSpring.authenticate("ciruz","wrong" ),"Wrong user and wrong password");
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}