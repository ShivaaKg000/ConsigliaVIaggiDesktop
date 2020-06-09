package consigliaViaggiDesktop.model.DAO;

import com.google.gson.*;
import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.LoginController;
import consigliaViaggiDesktop.model.DTO.JsonPageResponse;
import consigliaViaggiDesktop.model.Review;
import consigliaViaggiDesktop.model.*;
import consigliaViaggiDesktop.model.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewDaoJSON implements ReviewDao {

    @Override  public Review getReviewById(int id) throws DaoException {
        return getReviewJsonById(id);
    }
    private Review getReviewJsonById(int reviewId) throws DaoException {
        String urlString= Constants.GET_REVIEW_URL+"?"+Constants.REVIEW_ID_PARAM+reviewId;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = getJSONFromUrl(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Review review = parseReview(JsonParser.parseReader(bufferedReader).getAsJsonObject());
        return review;
    }

    @Override  public JsonPageResponse<Review> getReviewList(SearchParamsReview params) throws DaoException {
        JsonPageResponse<Review> reviewListPage=  getReviewListJSONParsing(params);
        return reviewListPage;

    }
    private JsonPageResponse <Review> getReviewListJSONParsing(SearchParamsReview params) throws DaoException {
        String urlString= Constants.GET_REVIEW_LIST_URL+"?";
        if(params.getId()!=null && !params.getId().equals(""))
            urlString+=Constants.REVIEW_ID_PARAM+params.getId()+"&";
        if(params.getAccommodationName()!=null && !params.getAccommodationName().equals(""))
            urlString+=Constants.ACCOMMODATION_NAME_PARAM+params.getAccommodationName()+"&";
        if(params.getAccommodationId()!=null && !params.getAccommodationId().equals(""))
            urlString+=Constants.ACCOMMODATION_ID_PARAM+params.getAccommodationId()+"&";
        if(params.getStatus()!=null && !params.getStatus().equals("") )
            urlString+=Constants.STATUS_PARAM+params.getStatus()+"&";
        if(params.getContent()!=null && !params.getContent().equals(""))
            urlString+=Constants.CONTENT_PARAM+params.getContent()+"&";
        urlString+=Constants.PAGE_PARAM+params.getCurrentpage()+"&";

        System.out.println(urlString);

        BufferedReader bufferedReader;
        try {
            bufferedReader = getJSONFromUrl(urlString);
        } catch (MalformedURLException e) {
            throw new DaoException(DaoException.ERROR,e.getMessage());

        }
        JsonObject jsonObject= JsonParser.parseReader(bufferedReader).getAsJsonObject();

        return parseReviewPage(jsonObject);
    }

    @Override  public Review approveReview(int reviewId) {
        try {
            Review review=editReview(reviewId, Status.APPROVED);
            if(review!=null){
                return review;
            }
        } catch (IOException | DaoException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override  public Review rejectReview(int reviewId) {
        try {
            Review review=editReview(reviewId,Status.REJECTED);
            if(review!=null){
                return review;
            }
        } catch (IOException | DaoException e) {
            e.printStackTrace();
        }
        return new Review.Builder().build();
    }
    private Review editReview(int id, Status status) throws IOException, DaoException {
        URL url = new URL(Constants.APPROVE_REVIEW+id+"?"+Constants.STATUS_PARAM+status);
        HttpURLConnection connection = null;
        int responseCode=0;
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization","Bearer "+ LoginController.getInstance().getCurrentUserAuthenticationToken());
        connection.setRequestProperty("Content-Type","application/json");
        responseCode=connection.getResponseCode();

        BufferedReader jsonResponse = null;
        if (responseCode== HttpURLConnection.HTTP_OK) {
            jsonResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Review review = parseReview(JsonParser.parseReader(jsonResponse).getAsJsonObject());
            return review;
        }
        return null;
    }

    private JsonPageResponse<Review> parseReviewPage(JsonObject jsonPage) throws DaoException {

        List<Review> reviewCollection = new ArrayList<>();
        JsonArray array= jsonPage.get("content").getAsJsonArray();
        for (JsonElement jo : array) {
            JsonObject reviewJson = (JsonObject)jo ;
            reviewCollection.add(parseReview(reviewJson));
        }
        System.out.println("COLLECTION" + reviewCollection);
        JsonPageResponse<Review> response = new JsonPageResponse<>();
        response.setContent(reviewCollection);
        response.setPage(jsonPage.get("page").getAsLong());
        response.setOffset(jsonPage.get("offset").getAsLong());
        response.setPageSize(jsonPage.get("pageSize").getAsLong());
        response.setTotalPages(jsonPage.get("totalPages").getAsLong());
        response.setTotalElements(jsonPage.get("totalElements").getAsLong());
        return response;
    }
    private Review parseReview(JsonObject reviewJson) throws DaoException {
        int id = reviewJson.get("id").getAsInt();
        int accommodationId = reviewJson.get("accommodationId").getAsInt();
        String accommodationName = reviewJson.get("accommodationName").getAsString();
        Status stato = Status.valueOf(reviewJson.get("stato").getAsString());
        String name = reviewJson.get("nome").getAsString();
        String creationDate =  reviewJson.get("creationDate").getAsString();
        creationDate = creationDate.substring(0,creationDate.indexOf("+"));
        String formattedDate = formatDate(creationDate);
        float rating = reviewJson.get("rating").getAsFloat();
        String content = reviewJson.get("content").getAsString();

        return new Review.Builder()
                .setId(id)
                .setAccommodationId(accommodationId)
                .setAccommodationName(accommodationName)
                .setApproved(stato)
                .setAuthor(name)
                .setData(formattedDate)
                .setRating(rating)
                .setReviewText(content)
                .build();

    }

    private String formatDate(String creationDate) throws DaoException {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss.SSS").parse(creationDate);
        } catch (ParseException e) {
            throw new DaoException(DaoException.ERROR, e.getMessage());
        }
        return new SimpleDateFormat("dd/MM/yyyy, Hm").format(date);

    }

    private BufferedReader getJSONFromUrl(String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader json  = null;
        try {
            json = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
    }




