package consiglia.viaggi.desktop.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;


public class Review implements Comparable{

	private int id;
	private String author;
    private String reviewText;
    private float rating;
    private String data;

    public Review(Builder builder) {
    	this.id=builder.id;
        this.author=builder.author;
        this.reviewText=builder.reviewText;
        this.rating=builder.rating;
        this.data= builder.data;
    }

    public int getId() {
		return id;
	}
    public String getAuthor() {
        return author;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getData() {
        return data;
    }

    public float getRating() {
        return rating;
    }

    @Override
    public int compareTo(Object o) {
        Review review = (Review) o ;
        try {
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(data);
            Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(review.getData());
            return date1.compareTo(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    static class Builder {

    	private int id;
        private String data;
        private String author;
        private String reviewText;
        private float rating;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }
        
        public Builder setReviewText(String reviewText) {
            this.reviewText = reviewText;
            return this;
        }

        public Builder setRating(float rating) {
            this.rating = rating;
            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }
        public Builder setData(String data) {
            this.data = data;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }

    public static class ReviewRatingComparator implements Comparator<Review> {
        @Override
        public int compare(Review review1, Review review2) {
            return Float.compare(review1.getRating(), review2.getRating());
        }
    }
}
