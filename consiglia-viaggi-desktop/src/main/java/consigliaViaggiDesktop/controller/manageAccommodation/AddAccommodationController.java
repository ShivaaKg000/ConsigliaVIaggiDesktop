package consigliaViaggiDesktop.controller.manageAccommodation;

import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.DAO.AccommodationDao;
import consigliaViaggiDesktop.model.DAO.AccommodationDaoFactory;
import consigliaViaggiDesktop.model.DAO.DaoException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;

public class AddAccommodationController {

    private final AccommodationDao accommodationDao;
    private final AccommodationController accommodationController;

    public AddAccommodationController(AccommodationController accommodationController){
        accommodationDao= AccommodationDaoFactory.getAccommodationDao();
        this.accommodationController=accommodationController;
    }

    public ObjectProperty<Accommodation> createAccommodationAsync(Accommodation accommodation) {
        ObjectProperty<Accommodation>  response = new SimpleObjectProperty<>();
        Task<Void> task = new Task<>() {
            @Override
            public Void call(){
                try {
                    response.set(accommodationDao.createAccommodation(accommodation));
                    NavigationController.getInstance().buildInfoBox("Creazione","Struttura creata con successo! (Id:"+response.get().getId()+")");
                    accommodationController.refreshAccommodationList();
                } catch (IOException e) {
                    NavigationController.getInstance().buildInfoBox("Creazione",e.getMessage());
                } catch (DaoException e) {
                    NavigationController.getInstance().buildInfoBox("Creazione",e.getErrorMessage()+"("+e.getErrorCode()+")");
                }
                response.notifyAll();
                return null;
            }
        };
        Thread createAccommodationThread = new Thread(task);
        createAccommodationThread.start();
        return response;
    }

    public StringProperty uploadAccommodationImage(File img, String name){
        StringProperty urlImage = new SimpleStringProperty();
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {

                try {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    urlImage.setValue(accommodationDao.uploadAccommodationImage(img, name));
                } catch (DaoException e) {
                    NavigationController.getInstance().buildInfoBox("Server error", e.getErrorMessage()+"("+e.getErrorCode()+")");
                    urlImage.setValue("");
                }

                return null;
            }
        };
        Thread uploadAccommodationImageThread = new Thread(task);
        uploadAccommodationImageThread.start();
        return  urlImage;
    }
}
