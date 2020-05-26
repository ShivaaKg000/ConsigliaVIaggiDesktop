package consigliaViaggiDesktop.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.DAO.AccommodationDao;
import consigliaViaggiDesktop.model.DAO.AccommodationDaoJSON;
import consigliaViaggiDesktop.model.DAO.DaoException;
import consigliaViaggiDesktop.view.AccommodationDetailView;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class ViewAccommodationController {


    private AccommodationDao accommodationDao;
    private ObservableList<Accommodation> observableAccommodationList;
    private ExecutorService executor;
    private String currentSearchParam,currentCategory,currentSubCategory;
    int currentpage;


    public ViewAccommodationController() {

    	executor=initExecutor(4);
    	accommodationDao= new AccommodationDaoJSON();
        observableAccommodationList= FXCollections.observableArrayList();		
    }


    public ObservableList<Accommodation> loadAccommodationListAsync(String category, String subCategory,String searchParam,int page) {

    	currentCategory=category;
    	currentSubCategory=subCategory;
    	currentSearchParam=searchParam;
    	currentpage=page;

    	observableAccommodationList.clear();
    	Task task = new Task() {
    		@Override
            public Void call() {
    			
    			List<Accommodation> accommodationList= accommodationDao.getAccommodationList(category,subCategory,searchParam,0);
    			observableAccommodationList.addAll(accommodationList);
    			observableAccommodationList.notifyAll();
				return null;
            }
        };
        initExecutor(4);
        Thread testThread = new Thread(task);
        executor.execute(testThread);

        return observableAccommodationList;
    }
    
    private ExecutorService initExecutor(int threadPullNumber) {
		return new ThreadPoolExecutor(threadPullNumber, threadPullNumber, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());


	}
    
    public ObjectProperty<Accommodation> getAccommodationAsync(int id) {
    	ObjectProperty<Accommodation>  observableAccommodation = new SimpleObjectProperty<Accommodation>();
    	Task task = new Task() {
    		@Override
            public Void call() throws InterruptedException {
    			
    			Accommodation accommodation= accommodationDao.getAccommodationById(id);
    			observableAccommodation.setValue(accommodation);
				return null;
            }
        };
        Thread testThread = new Thread(task);
        testThread.start();
		return observableAccommodation;
     
    }

	public ObjectProperty<Accommodation> createAccommodationAsync(Accommodation accommodation) {
		ObjectProperty<Accommodation>  response = new SimpleObjectProperty();
		Task task = new Task() {
			@Override
			public Void call() throws InterruptedException {
				try {
					response.set(accommodationDao.createAccommodation(accommodation));
					NavigationController.getInstance().buildInfoBox("Creazione","Struttura creata con successo! (Id:"+response.get().getId()+")");
					refreshAccommodationList();
				} catch (IOException e) {
					NavigationController.getInstance().buildInfoBox("Creazione",e.getMessage());
				} catch (DaoException e) {
					NavigationController.getInstance().buildInfoBox("Creazione",e.getErrorMessage());
				}
				response.notifyAll();
				return null;
			}
		};
		Thread testThread = new Thread(task);
		testThread.start();
		return response;
	}
    
    public void loadAccommodationDetailView(int accommodationId) {

		AccommodationDetailView accommodationDetailView=new AccommodationDetailView();
		accommodationDetailView.setId(accommodationId);
		accommodationDetailView.setViewAccommodationController(this);
		NavigationController.getInstance().navigateToView(Constants.ACCOMMODATION_DETAIL_VIEW,accommodationDetailView);

	}

	public void goBack() {
    	NavigationController.getInstance().navigateBack();
		
	}

	public void goBackToMenu() {
		executor.shutdownNow();
		NavigationController.getInstance().navigateBack();

	}

	public void deleteAccommodation(int accommodationId) {
		try {
			accommodationDao.deleteAccommodation(accommodationId);
			NavigationController.getInstance().buildInfoBox("Cancellazione","Struttura eliminata con successo ");
		} catch (IOException e) {
			NavigationController.getInstance().buildInfoBox("Cancellazione","Errore sconosciuto");
		} catch (DaoException e) {
			NavigationController.getInstance().buildInfoBox("Cancellazione",e.getErrorMessage());
		}


	}

	public void loadCreateAccommodationDetailView() {
		AccommodationDetailView accommodationDetailView=new AccommodationDetailView();
		accommodationDetailView.setViewAccommodationController(this);
		NavigationController.getInstance().navigateToView(Constants.ACCOMMODATION_DETAIL_VIEW,accommodationDetailView);
	}

	public BooleanProperty editAccommodationAsync(Accommodation editedAccommodation) {
		BooleanProperty result= new SimpleBooleanProperty();
		Task task = new Task() {
			@Override
			public Void call(){
				try {
					result.set(accommodationDao.editAccommodation(editedAccommodation));
					NavigationController.getInstance().buildInfoBox("Modifica","Modifica avvenuta con successo!");
					refreshAccommodationList();
				} catch (IOException e) {
					NavigationController.getInstance().buildInfoBox("Modifica",e.getMessage());
				} catch (DaoException e) {
					NavigationController.getInstance().buildInfoBox("Modifica",e.getErrorMessage());
				}
				return null;
			}
		};
		Thread testThread = new Thread(task);
		testThread.start();
		return result;
	}

	public void refreshAccommodationList(){
		loadAccommodationListAsync(currentCategory,currentSubCategory,currentSearchParam,currentpage);
	}

}
