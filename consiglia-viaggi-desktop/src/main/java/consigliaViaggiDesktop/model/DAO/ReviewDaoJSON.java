package consigliaViaggiDesktop.model.DAO;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.LoginController;
import consigliaViaggiDesktop.model.Review;
import consigliaViaggiDesktop.model.*;
import consigliaViaggiDesktop.model.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

public class ReviewDaoJSON implements ReviewDao {

    GsonBuilder builder;
    Gson gson;

    public ReviewDaoJSON(){
        builder = new GsonBuilder();
        gson = builder.create();
    }

    @Override
    public List<Review> getReviewList(int id) {
        return (List<Review>) getReviewListJSON(id);
    }

    @Override
    public Review getReviewById(int id) {
        return getReviewJsonById(id);
    }

    @Override
    public boolean postReview(Review review) {
        return false;
    }

    @Override
    public Review approveReview(int reviewId) {
        try {
            Review review=editReview(reviewId, Status.APPROVED);
            if(review!=null){
                return review;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Review rejectReview(int reviewId) {
        try {
            Review review=editReview(reviewId,Status.REJECTED);
            if(review!=null){
                return review;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Review.Builder().build();
    }

    private Collection<Review> getReviewListJSON(int accommodationId)  {
        String urlString= Constants.GET_REVIEW_LIST_URL+Constants.ACCOMMODATION_ID_PARAM+accommodationId;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = getJSONFromUrl(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Type collectionType = new TypeToken<Collection<Review>>(){}.getType();
        Collection<Review> reviewCollection = gson.fromJson(bufferedReader, collectionType);
        return reviewCollection;
    }

    private Review getReviewJsonById(int reviewId)  {
        String urlString= Constants.GET_REVIEW_LIST_URL+Constants.REVIEW_ID_PARAM+reviewId;
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = getJSONFromUrl(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Review review = gson.fromJson(bufferedReader,Review.class);
        return review;
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

    private Review editReview(int id, Status status) throws IOException {
        URL url = new URL(Constants.APPROVE_REVIEW+id+Constants.STATUS_PARAM+status);
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
            return gson.fromJson(jsonResponse,Review.class);
        }
        if(connection.getResponseCode()==401){
            System.out.print("\nResponse: Non autorizzato"); //bisogna implementare qualcosa
            //return JsonParser.parseString("\"error\":\"non autorizzato\"").getAsJsonObject();
        }
        // if json null???
        return null;
    }
}
