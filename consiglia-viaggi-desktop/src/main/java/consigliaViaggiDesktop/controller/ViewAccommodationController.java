package consigliaViaggiDesktop.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.DAO.AccommodationDao;
import consigliaViaggiDesktop.model.DAO.AccommodationDaoJSON;
import consigliaViaggiDesktop.model.DAO.DaoException;
import consigliaViaggiDesktop.model.DTO.JsonPageResponse;
import consigliaViaggiDesktop.model.SearchParams;
import consigliaViaggiDesktop.view.AccommodationDetailView;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class ViewAccommodationController {


    private final AccommodationDao accommodationDao;
    private final ObservableList<Accommodation> observableAccommodationList;
    private final ExecutorService executor;
   	private SearchParams currentSearchParams;
	private LongProperty pageNumber;
	private LongProperty totalPageNumber;
	private LongProperty totalElementNumber;


	public ViewAccommodationController() {
		totalPageNumber= new SimpleLongProperty();
		pageNumber= new SimpleLongProperty(-1);
		totalElementNumber= new SimpleLongProperty();
    	executor=initExecutor(4);
    	accommodationDao= new AccommodationDaoJSON();
        observableAccommodationList= FXCollections.observableArrayList();		
    }

	public LongProperty getPageNumber() {
		return pageNumber;
	}

	public LongProperty getTotalPageNumber() {
		return totalPageNumber;
	}

	public LongProperty getTotalElementNumber() {
		return  totalElementNumber;
	}

    public ObservableList<Accommodation> loadAccommodationListAsync(SearchParams params) {

    	currentSearchParams=params;
    	observableAccommodationList.clear();
    	Task task = new Task() {
    		@Override
            public Void call() throws DaoException {
				JsonPageResponse<Accommodation> page=accommodationDao.getAccommodationList(currentSearchParams);
    			List<Accommodation> accommodationList= page.getContent();
    			pageNumber.setValue(page.getPage());
				totalPageNumber.setValue(page.getTotalPages());
				totalElementNumber.setValue(page.getTotalElements());
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
            public Void call() throws InterruptedException, DaoException {
    			
    			Accommodation accommodation= accommodationDao.getAccommodationById(id);
    			observableAccommodation.setValue(accommodation);
				return null;
            }
        };
		Thread testThread = new Thread(task);
		executor.execute(testThread);
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
		executor.execute(testThread);
		return response;
	}
    
    public void loadAccommodationDetailView(int accommodationId) {

		AccommodationDetailView accommodationDetailView=new AccommodationDetailView();
		accommodationDetailView.setId(accommodationId);
		accommodationDetailView.setViewAccommodationController(this);
		NavigationController.getInstance().navigateToView(Constants.ACCOMMODATION_DETAIL_VIEW,accommodationDetailView);

	}

	public BooleanProperty deleteAccommodation(int accommodationId) {

		BooleanProperty response = new SimpleBooleanProperty();
		Task task = new Task() {
			@Override
			public Void call() {
				try {
					response.setValue(accommodationDao.deleteAccommodation(accommodationId));
					NavigationController.getInstance().buildInfoBox("Cancellazione", "Struttura eliminata con successo ");
					refreshAccommodationList();
				} catch (DaoException e) {
					NavigationController.getInstance().buildInfoBox("Cancellazione", e.getErrorMessage());
				}
				response.notifyAll();
				return null;
			}
		};
		Thread testThread = new Thread(task);
		testThread.start();
		return response;
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
		loadAccommodationListAsync(currentSearchParams);
	}

	public void nextPage() {
		if (pageNumber.getValue()+1<totalPageNumber.getValue()) {
			currentSearchParams = new SearchParams.Builder().setCurrentpage(currentSearchParams.getCurrentpage() + 1)
					.setCurrentSubCategory(currentSearchParams.getCurrentSubCategory())
					.setCurrentSearchString(currentSearchParams.getCurrentSearchString())
					.setCurrentCategory(currentSearchParams.getCurrentCategory())
					.create();
			loadAccommodationListAsync(currentSearchParams);
		}
	}

	public void previousPage() {
		if (pageNumber.getValue()>0) {
			currentSearchParams = new SearchParams.Builder().setCurrentpage(currentSearchParams.getCurrentpage() - 1)
					.setCurrentSubCategory(currentSearchParams.getCurrentSubCategory())
					.setCurrentSearchString(currentSearchParams.getCurrentSearchString())
					.setCurrentCategory(currentSearchParams.getCurrentCategory())
					.create();
			loadAccommodationListAsync(currentSearchParams);
		}
	}

	public void goBack() {
		NavigationController.getInstance().navigateBack();

	}

	public void goBackToMenu() {
		executor.shutdownNow();
		NavigationController.getInstance().navigateBack();

	}

}
