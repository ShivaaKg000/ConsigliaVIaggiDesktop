package consigliaViaggiDesktop.controller.manageAccommodation;

import java.util.List;
import java.util.concurrent.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.DAO.AccommodationDao;
import consigliaViaggiDesktop.model.DAO.AccommodationDaoJSON;
import consigliaViaggiDesktop.model.DAO.DaoException;
import consigliaViaggiDesktop.model.DTO.JsonPageResponse;
import consigliaViaggiDesktop.model.SearchParamsAccommodation;
import consigliaViaggiDesktop.view.AccommodationDetailView;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class AccommodationController {

    private final AccommodationDao accommodationDao;
    private final ObservableList<Accommodation> observableAccommodationList;
    private ExecutorService executor;
   	private SearchParamsAccommodation currentSearchParamsAccommodation;
	private LongProperty pageNumber;
	private LongProperty totalPageNumber;
	private LongProperty totalElementNumber;

	public AccommodationController() {
		totalPageNumber= new SimpleLongProperty();
		pageNumber= new SimpleLongProperty(-1);
		totalElementNumber= new SimpleLongProperty();
    	executor= new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
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

	public ObservableList<Accommodation> loadAccommodationListAsync(SearchParamsAccommodation params) {

    	currentSearchParamsAccommodation =params;

    	Task task = new Task<JsonPageResponse<Accommodation>>() {
    		@Override
            public JsonPageResponse<Accommodation> call(){
				JsonPageResponse<Accommodation> page= null;

				try {
					page = accommodationDao.getAccommodationList(currentSearchParamsAccommodation);
				} catch (DaoException e) {
					NavigationController.getInstance().buildInfoBox("Ricerca",e.getMessage());
				}

    			/*pageNumber.setValue(page.getPage());
				totalPageNumber.setValue(page.getTotalPages());
				totalElementNumber.setValue(page.getTotalElements());

				List<Accommodation> accommodationList= page.getContent();
				observableAccommodationList.remove(0,observableAccommodationList.size());
				observableAccommodationList.addAll(accommodationList);*/

				return page;
            }
        };
		task.setOnSucceeded(t -> {
			JsonPageResponse<Accommodation> pageResult= (JsonPageResponse<Accommodation>) task.getValue();
			pageNumber.setValue(pageResult.getPage());
			totalPageNumber.setValue(pageResult.getTotalPages());
			totalElementNumber.setValue(pageResult.getTotalElements());

			List<Accommodation> accommodationList= pageResult.getContent();
			observableAccommodationList.remove(0,observableAccommodationList.size());
			observableAccommodationList.addAll(accommodationList);

		});
		executor= new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        Thread testThread = new Thread(task);
        executor.execute(testThread);

        return observableAccommodationList;
    }
	public void refreshAccommodationList(){
		loadAccommodationListAsync(currentSearchParamsAccommodation);
	}

	public void loadCreateAccommodationDetailView() {
		AccommodationDetailView accommodationDetailView=new AccommodationDetailView();
		accommodationDetailView.setAccommodationController(this);
		NavigationController.getInstance().navigateToView(Constants.ACCOMMODATION_DETAIL_VIEW,accommodationDetailView);
	}
    
    public void loadAccommodationDetailView(int accommodationId) {

		AccommodationDetailView accommodationDetailView=new AccommodationDetailView();
		accommodationDetailView.setId(accommodationId);
		accommodationDetailView.setAccommodationController(this);
		NavigationController.getInstance().navigateToView(Constants.ACCOMMODATION_DETAIL_VIEW,accommodationDetailView);

	}

	public void nextPage() {
		if (pageNumber.getValue()+1<totalPageNumber.getValue()) {
			currentSearchParamsAccommodation = new SearchParamsAccommodation.Builder().setCurrentpage(currentSearchParamsAccommodation.getCurrentPage() + 1)
					.setCurrentSubCategory(currentSearchParamsAccommodation.getCurrentSubCategory())
					.setCurrentSearchString(currentSearchParamsAccommodation.getCurrentSearchString())
					.setCurrentCategory(currentSearchParamsAccommodation.getCurrentCategory())
					.setDirection(currentSearchParamsAccommodation.getDirection())
					.setOrderBy(currentSearchParamsAccommodation.getOrderBy())
					.create();
			loadAccommodationListAsync(currentSearchParamsAccommodation);
		}
	}
	public void previousPage() {
		if (pageNumber.getValue()>0) {
			currentSearchParamsAccommodation = new SearchParamsAccommodation.Builder().setCurrentpage(currentSearchParamsAccommodation.getCurrentPage() - 1)
					.setCurrentSubCategory(currentSearchParamsAccommodation.getCurrentSubCategory())
					.setCurrentSearchString(currentSearchParamsAccommodation.getCurrentSearchString())
					.setCurrentCategory(currentSearchParamsAccommodation.getCurrentCategory())
					.setDirection(currentSearchParamsAccommodation.getDirection())
					.setOrderBy(currentSearchParamsAccommodation.getOrderBy())
					.create();
			loadAccommodationListAsync(currentSearchParamsAccommodation);
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
