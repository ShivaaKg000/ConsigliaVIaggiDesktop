package consigliaViaggiDesktop.controller.manageAccommodation;

import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.DAO.AccommodationDao;
import consigliaViaggiDesktop.model.DAO.DaoFactory;
import consigliaViaggiDesktop.model.DAO.DaoException;

import javafx.beans.property.*;
import javafx.concurrent.Task;


public class EditAccommodationController {

    private final AccommodationDao accommodationDao;
    private final AccommodationController accommodationController;

    public EditAccommodationController(AccommodationController accommodationController) {
        accommodationDao= DaoFactory.getAccommodationDao();
        this.accommodationController=accommodationController;
    }

    public ObjectProperty<Accommodation> getAccommodationAsync(int id) {
        ObjectProperty<Accommodation>  observableAccommodation = new SimpleObjectProperty<>();
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {

                try {
                    Accommodation accommodation = accommodationDao.getAccommodationById(id);
                    observableAccommodation.setValue(accommodation);
                } catch (DaoException e) {
                    NavigationController.getInstance().buildInfoBox("Data error",e.getErrorMessage()+"("+e.getErrorCode()+")");
                }

                return null;
            }
        };
        Thread editAccommodationThread = new Thread(task);
        editAccommodationThread.start();
        return observableAccommodation;

    }

    public BooleanProperty editAccommodationAsync(Accommodation editedAccommodation) {
        BooleanProperty result= new SimpleBooleanProperty();
        Task<Void> task = new Task<>() {
            @Override
            public Void call(){
                try {
                    result.set(accommodationDao.editAccommodation(editedAccommodation));
                    NavigationController.getInstance().buildInfoBox("Modifica","Modifica avvenuta con successo!");
                    accommodationController.refreshAccommodationList();
                } catch (DaoException e) {
                    NavigationController.getInstance().buildInfoBox("Modifica",e.getErrorMessage()+"("+e.getErrorCode()+")");
                }
                return null;
            }
        };
        Thread editAccommodationThread = new Thread(task);
        editAccommodationThread.start();
        return result;
    }
}