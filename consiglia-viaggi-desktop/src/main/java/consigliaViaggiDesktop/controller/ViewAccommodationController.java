package consigliaViaggiDesktop.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.AccommodationDao;
import consigliaViaggiDesktop.model.AccommodationDaoJson;
import consigliaViaggiDesktop.model.AccommodationDaoStub;
import consigliaViaggiDesktop.view.AccommodationDetailView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class ViewAccommodationController {


    private AccommodationDao accommodationDao;
    private ObservableList<Accommodation> observableAccommodationList;
    private ExecutorService executor;
    
    public ViewAccommodationController() {

    	executor=initExecutor(4);
    	accommodationDao= new AccommodationDaoStub();
		//accommodationDao= new AccommodationDaoJson(); 
        observableAccommodationList= FXCollections.observableArrayList();		
    }
    
    public ObservableList<Accommodation> getObsarvableAccommodationList() {
		return observableAccommodationList;
	}

    public void loadAccommodationListAsync(String city) {
    	
    	observableAccommodationList.clear();
    	Task task = new Task() {
    		@Override
            public Void call() throws InterruptedException {
    			
    			List<Accommodation> accommodationList= accommodationDao.getAccommodationList(city);
    			observableAccommodationList.addAll(accommodationList);
    			observableAccommodationList.notifyAll();
				return null;
            }
        };
        initExecutor(4);
        Thread testThread = new Thread(task);
        executor.execute(testThread);
     
    }
    
    private ExecutorService initExecutor(int threadPullNumber) {
    	return Executors.newFixedThreadPool(threadPullNumber);
		
	}

    public void addAccommodationtoListAsync(int id) {
    	Task task = new Task() {
    		@Override
            public Void call() throws InterruptedException {
    			
    			Accommodation accommodation= accommodationDao.getAccommodationById(id);
    			observableAccommodationList.add(accommodation);
    			observableAccommodationList.notifyAll();
				return null;
            }
        };
        
        Thread testThread = new Thread(task);
        executor.execute(testThread);
     
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
    
    public void accommodationSelected(int accommodationId) {
		try {	
			
			AccommodationDetailView accommodationDetailView=new AccommodationDetailView();
			accommodationDetailView.setId(accommodationId);
			accommodationDetailView.setViewAccommodationController(this);
			NavigationController.getInstance().navigateToView(Constants.ACCOMMODATION_DETAIL_VIEW,accommodationDetailView);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
    public void goBack() {
		executor.shutdownNow();
    	NavigationController.getInstance().navigateBack();
		
	}
    
}
