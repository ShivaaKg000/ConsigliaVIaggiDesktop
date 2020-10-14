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
    private final AccommodationController accommodationController;

    public DeleteAccommodationController(AccommodationController accommodationController) {

        accommodationDao= new AccommodationDaoJSON();
        this.accommodationController=accommodationController;
    }

    public SimpleBooleanProperty deleteAccommodation(int accommodationId) {
        SimpleBooleanProperty response= new SimpleBooleanProperty();
        Task task = new Task<Void>() {
            @Override
            public Void call(){
                try{
                    response.setValue(accommodationDao.deleteAccommodation(accommodationId));
                    if(response.getValue()) {
                        NavigationController.getInstance().buildInfoBox("Eliminazione struttura",
                                "Struttura eliminata con successo!");
                        accommodationController.refreshAccommodationList();
                    }
                    else
                    {
                        NavigationController.getInstance().buildInfoBox("Eliminazione struttura",
                                "Record non trovato!");
                    }
                }catch (DaoException e){
                    NavigationController.getInstance().buildInfoBox("Eliminazione struttura",
                            e.getErrorMessage()+"("+e.getErrorCode()+")");
                }
                return null;
            }
        };
        Thread deleteAccommodationThread = new Thread(task);
        deleteAccommodationThread.start();

        return response;
    }
}
