package consigliaViaggiDesktop.model;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import consigliaViaggiDesktop.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

public class ReviewDaoJSON implements ReviewDao{
    @Override
    public List<Review> getReviewList(int id) {
        return (List<Review>) getReviewListJSON(id);
    }

    @Override
    public Review getReviewById(int id) {
        return getReviewJSON(id);
    }

    @Override
    public boolean postReview(Review review) {
        return false;
    }

    @Override
    public void approveReview(int reviewId) {

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

    private Review getReviewJSON(int reviewId)  {
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
}
