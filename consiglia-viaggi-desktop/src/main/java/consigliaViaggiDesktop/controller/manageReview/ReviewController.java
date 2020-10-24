package consigliaViaggiDesktop.controller.manageReview;


import java.util.List;
import java.util.concurrent.*;

import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.NavigationController;
import consigliaViaggiDesktop.model.DAO.DaoException;
import consigliaViaggiDesktop.model.DAO.ReviewDao;
import consigliaViaggiDesktop.model.DAO.ReviewDaoFactory;
import consigliaViaggiDesktop.model.DTO.JsonPageResponse;
import consigliaViaggiDesktop.model.Review;
import consigliaViaggiDesktop.model.SearchParamsReview;
import consigliaViaggiDesktop.view.ReviewDetailView;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
public class ReviewController {

	
    private final ReviewDao reviewDao;
    private final ObservableList<Review> observableReviewList;
    private final IntegerProperty pageNumber;
    private final IntegerProperty totalPageNumber;
    private final IntegerProperty totalElementNumber;

    private SearchParamsReview currentSearchParamsReview;
    private ExecutorService executor;

    public IntegerProperty getPageNumber() {
        return pageNumber;
    }
    public IntegerProperty getTotalPageNumber() {
        return totalPageNumber;
    }
    public IntegerProperty getTotalElementNumber() {
        return  totalElementNumber;
    }


    public ReviewController() {

        totalPageNumber= new SimpleIntegerProperty();
        pageNumber= new SimpleIntegerProperty(-1);
        totalElementNumber= new SimpleIntegerProperty();

        executor= new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10));
        reviewDao= ReviewDaoFactory.getReviewDao();
        observableReviewList= FXCollections.observableArrayList();
    }

    public ObservableList<Review> loadReviewListAsync(SearchParamsReview params){

        currentSearchParamsReview =params;

        Task<Void> task = new Task<>() {
            @Override
            public Void call() throws DaoException {
                JsonPageResponse<Review> page=reviewDao.getReviewList(currentSearchParamsReview);
                List<Review> reviewList= page.getContent();
                pageNumber.setValue(page.getPage());
                totalPageNumber.setValue(page.getTotalPages());
                totalElementNumber.setValue(page.getTotalElements());
                observableReviewList.clear();
                observableReviewList.addAll(reviewList);
                return null;
            }
        };
        executor= new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10));

        Thread testThread = new Thread(task);
        executor.execute(testThread);

        return observableReviewList;
    }
    public ObjectProperty<Review> getReviewAsync(int reviewId) {
        ObjectProperty<Review>  observableReview = new SimpleObjectProperty<>();
        Task<Void> task = new Task<>() {
            @Override
            public Void call(){

                try {
                    Review review = reviewDao.getReviewById(reviewId);
                    observableReview.set(review);
                } catch (DaoException e) {
                    NavigationController.getInstance().buildInfoBox("Errore",e.getErrorMessage()+"("+e.getErrorCode()+")");
                }

                return null;
            }
        };
        Thread testThread = new Thread(task);
        testThread.start();
        return observableReview;

    }

    public void refreshList(){
        loadReviewListAsync(currentSearchParamsReview);
    }

	public void reviewSelected(int reviewId) {

        ReviewDetailView reviewDetailView=new ReviewDetailView();
        reviewDetailView.setId(reviewId);
        reviewDetailView.setReviewController(this);
        NavigationController.getInstance().navigateToView(Constants.REVIEW_DETAIL_VIEW,reviewDetailView);
    }

	public void goBack() {
        Platform.runLater(() -> NavigationController.getInstance().navigateBack());
		
	}
	public void goBackToMenu() {
        //per tornare al menu bisogna interrompere l'executor altrimenti resta attivo in bg
        executor.shutdownNow();
        NavigationController.getInstance().navigateBack();

    }

    public void nextPage(){
        if (pageNumber.getValue()+1<totalPageNumber.getValue()) {
            currentSearchParamsReview.setCurrentPage(pageNumber.getValue()+1);
            loadReviewListAsync(currentSearchParamsReview);
        }
    }
    public void previousPage() {
        if (pageNumber.getValue()>0) {
            currentSearchParamsReview.setCurrentPage(pageNumber.getValue()-1);
            loadReviewListAsync(currentSearchParamsReview);
        }
    }

    public SimpleBooleanProperty deleteReview(int reviewId) {

        SimpleBooleanProperty response= new SimpleBooleanProperty();
        Task<Void> task = new Task<>() {
            @Override
            public Void call(){
                try{
                    response.setValue(reviewDao.deleteReview(reviewId));
                    if(response.getValue()) {
                        NavigationController.getInstance().buildInfoBox("Eliminazione recensione",
                                "Recensione eliminata con successo!");
                        refreshList();
                    }
                    else
                    {
                        NavigationController.getInstance().buildInfoBox("Eliminazione recensione",
                                "Record non trovato!");
                    }
                }catch (DaoException e){
                    NavigationController.getInstance().buildInfoBox("Eliminazione recensione",
                            e.getErrorMessage()+"("+e.getErrorCode()+")");
                }
                return null;
            }
        };
        Thread deleteReviewThread = new Thread(task);
        deleteReviewThread.start();

        return response;
    }
}
