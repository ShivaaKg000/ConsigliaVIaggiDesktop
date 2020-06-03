package consigliaViaggiDesktop.controller.manageAccommodation;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.DAO.AccommodationDao;
import consigliaViaggiDesktop.model.DAO.AccommodationDaoJSON;
import consigliaViaggiDesktop.model.DAO.DaoException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AddAccommodationController {

    private final AccommodationDao accommodationDao;
    private AccommodationController accommodationController;

    public AddAccommodationController(AccommodationController accommodationController){
        accommodationDao= new AccommodationDaoJSON();
        this.accommodationController=accommodationController;
    }

    public ObjectProperty<Accommodation> createAccommodationAsync(Accommodation accommodation) {
        ObjectProperty<Accommodation>  response = new SimpleObjectProperty();
        Task task = new Task() {
            @Override
            public Void call() throws InterruptedException {
                try {
                    response.set(accommodationDao.createAccommodation(accommodation));
                    NavigationController.getInstance().buildInfoBox("Creazione","Struttura creata con successo! (Id:"+response.get().getId()+")");
                    accommodationController.refreshAccommodationList();
                } catch (IOException e) {
                    NavigationController.getInstance().buildInfoBox("Creazione",e.getMessage());
                } catch (DaoException e) {
                    NavigationController.getInstance().buildInfoBox("Creazione",e.getErrorMessage());
                }
                response.notifyAll();
                return null;
            }
        };
        Thread createAccommodationThread = new Thread(task);
        createAccommodationThread.start();
        return response;
    }
}
