package consiglia.viaggi.desktop.controller;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import consiglia.viaggi.desktop.model.Accommodation;
import consiglia.viaggi.desktop.model.AccommodationDao;
import consiglia.viaggi.desktop.model.AccommodationDaoStub;
import consiglia.viaggi.desktop.model.Review;
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
        observableAccommodationList= FXCollections.observableArrayList();		
    }
    
    public ObservableList<Accommodation> getObsarvableAccommodationList() {
		return observableAccommodationList;
	}

    public void loadAccommodationListAsync(int id) {
    	
    	observableAccommodationList.clear();
    	Task task = new Task() {
    		@Override
            public Void call() throws InterruptedException {
    			
    			List<Accommodation> accommodationList= accommodationDao.getAccommodationList(id);
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
    			observableAccommodation.set(accommodation);
				return null;
            }
        };
        Thread testThread = new Thread(task);
        testThread.start();
		return observableAccommodation;
     
    }
}
