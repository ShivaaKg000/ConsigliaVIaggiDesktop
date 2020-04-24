package consiglia.viaggi.desktop;

public final class Constants  {

    public static final String EMPTY_STRING = "";
    public static final String DEFAULT = "default";
    public static final String BEST_RATING = "best";
    public static final String WORST_RATING = "worst";
    public static final String CATEGORY_HOTEL = "-HOTEL-";
    public static final String CATEGORY_RESTAURANT = "-RISTORANTI-";
    public static final String CATEGORY_ATTRACTION = "-ATTRAZIONI-";
    public static final int ASCENDING=1;
    public static final int DESCENDING=2;
    public static final float DEFAULT_MIN_RATING=0;
    public static final float DEFAULT_MAX_RATING=5;
    public static final int DEFAULT_ORDER = 0;
    public static final int BEST_RATING_ORDER = 1;
    public static final int WORST_RATING_ORDER = 2;
    
    /*View fxml resource path from navigation controller*/
    public static final String  REVIEW_DETAIL_VIEW= "../view/review_detail_view.fxml";
    public static final String  REVIEW_VIEW= "../view/review_view.fxml";
    public static final String  LOGIN_VIEW= "../view/login_view.fxml";
    public static final String  MENU_VIEW= "../view/menu_view.fxml";
    public static final String  ACCOMMODATION_VIEW= "../view/accommodation_view.fxml";


    private Constants(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}
