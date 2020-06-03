package consigliaViaggiDesktop.controller.manageAccommodation;

import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.model.DAO.AccommodationDao;
import consigliaViaggiDesktop.model.DAO.AccommodationDaoJSON;
import consigliaViaggiDesktop.model.DAO.DaoException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;


public class DeleteAccommodationController {
    private final AccommodationDao accommodationDao;
    private AccommodationController accommodationController;

    public DeleteAccommodationController(AccommodationController accommodationController) {

        accommodationDao= new AccommodationDaoJSON();
        this.accommodationController=accommodationController;
    }

    public BooleanProperty deleteAccommodation(int accommodationId) {
        BooleanProperty response = new SimpleBooleanProperty();
        Task task = new Task() {
            @Override
            public Void call() {
                try {
                    response.setValue(accommodationDao.deleteAccommodation(accommodationId));
                    NavigationController.getInstance().buildInfoBox("Cancellazione", "Struttura eliminata con successo ");
                    accommodationController.refreshAccommodationList();
                } catch (DaoException e) {
                    NavigationController.getInstance().buildInfoBox("Cancellazione", e.getErrorMessage());
                }
                response.notifyAll();
                return null;
            }
        };
        Thread deleteAccommodationThread = new Thread(task);
        deleteAccommodationThread.start();
        return response;
    }
}
