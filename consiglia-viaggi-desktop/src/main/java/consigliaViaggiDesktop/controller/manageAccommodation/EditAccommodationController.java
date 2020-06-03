package consigliaViaggiDesktop.controller.manageAccommodation;

import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.DAO.AccommodationDao;
import consigliaViaggiDesktop.model.DAO.AccommodationDaoJSON;
import consigliaViaggiDesktop.model.DAO.DaoException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

public class EditAccommodationController {

    private final AccommodationDao accommodationDao;
    private AccommodationController accommodationController;

    public EditAccommodationController(AccommodationController accommodationController) {
        accommodationDao= new AccommodationDaoJSON();
        this.accommodationController=accommodationController;
    }

    public ObjectProperty<Accommodation> getAccommodationAsync(int id) {
        ObjectProperty<Accommodation>  observableAccommodation = new SimpleObjectProperty<Accommodation>();
        Task task = new Task() {
            @Override
            public Void call() throws InterruptedException, DaoException {

                Accommodation accommodation= accommodationDao.getAccommodationById(id);
                observableAccommodation.setValue(accommodation);
                return null;
            }
        };
        Thread editAccommodationThread = new Thread(task);
        editAccommodationThread.start();
        return observableAccommodation;

    }

    public BooleanProperty editAccommodationAsync(Accommodation editedAccommodation) {
        BooleanProperty result= new SimpleBooleanProperty();
        Task task = new Task() {
            @Override
            public Void call(){
                try {
                    result.set(accommodationDao.editAccommodation(editedAccommodation));
                    NavigationController.getInstance().buildInfoBox("Modifica","Modifica avvenuta con successo!");
                    accommodationController.refreshAccommodationList();
                } catch (DaoException e) {
                    NavigationController.getInstance().buildInfoBox("Modifica",e.getErrorMessage());
                }
                return null;
            }
        };
        Thread editAccommodationThread = new Thread(task);
        editAccommodationThread.start();
        return result;
    }
}
