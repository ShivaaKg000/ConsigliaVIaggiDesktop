package consigliaViaggiDesktop;

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
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String  IMG_PLACEHOLDER = "/images/placeholder.png";
    
    /*View fxml resource path from navigation controller*/
    public static final String  REVIEW_DETAIL_VIEW= "/fxml/review_detail_view.fxml";
    public static final String  REVIEW_VIEW= "/fxml/review_view.fxml";
    public static final String  LOGIN_VIEW= "/fxml/login_view.fxml";
    public static final String  MENU_VIEW= "/fxml/menu_view.fxml";
    public static final String  ACCOMMODATION_VIEW= "/fxml/accommodation_view.fxml";
	public static final String  ACCOMMODATION_DETAIL_VIEW = "/fxml/accommodation_detail.fxml";

	/*JSON URLs*/
    public static final String SERVER_URL="http://localhost:5000/";
    public static final String LOGIN_URL = SERVER_URL+"authenticate";
    public static final String PAGE_PARAM ="page=";
    //Accommodation
    public static final String GET_ACCOMMODATION_LIST_URL = SERVER_URL+"accommodation_generic";
    public static final String CREATE_ACCOMMODATION_URL = SERVER_URL+"accommodation/create";
    public static final String DELETE_ACCOMMODATION_URL = SERVER_URL+"accommodation/delete/";
    public static final String EDIT_ACCOMMODATION_URL =SERVER_URL+"accommodation/edit/" ;
    public static final String QUERY_PARAM ="query=";
    public static final String CATEGORY_PARAM ="category=";
    public static final String SUBCATEGORY_PARAM ="subCategory=";
    //Review
    public static final String GET_REVIEW_LIST_URL = SERVER_URL+"review_view";
    public static final String APPROVE_REVIEW = SERVER_URL+"review/edit/";
    public static final String REVIEW_ID_PARAM ="reviewId=";
    public static final String STATUS_PARAM ="status=";
    public static final String ACCOMMODATION_ID_PARAM ="accommodationId=";
    public static final String CONTENT_PARAM ="content=" ;
    public static final String ACCOMMODATION_NAME_PARAM ="accommodationName=" ;
    public static final String GET_REVIEW_URL=SERVER_URL+"single_review_view";
    public static final String ORDER_BY_PARAM ="orderBy=" ;
    public static final String DIRECTION_PARAM ="direction=" ;


    private Constants(){
        //this prevents even the native class from
        //calling this ctor as well :
        throw new AssertionError();
    }
}
