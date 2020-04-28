package consigliaViaggiDesktop.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;



public class Review implements Comparable{

	private int id;
	private int idAccommodation;
	private String nameAccommodation;
	private String author;
    private String reviewText;
    private float rating;
    private String data;
    private Status status;
    //private final BooleanProperty approve;


    public Review(Builder builder) {
    	this.id=builder.id;
    	this.idAccommodation=builder.idAccommodation;
    	this.nameAccommodation=builder.nameAccommodation;
        this.author=builder.author;
        this.reviewText=builder.reviewText;
        this.rating=builder.rating;
        this.data= builder.data;
        this.status=builder.status;
        //this.approve = new SimpleBooleanProperty(this, "approve", status);
        
    }
    
    /*public BooleanProperty approveProperty() {
        return approve;
    }*/

    public int getId() {
		return id;
	}
    public int getIdAccommodation() {
  		return idAccommodation;
  	}
      
    public String getNameAccommodation() {
  		return nameAccommodation;
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

    public Status getStatus() {
		return status;
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
    	private int idAccommodation;
    	private String nameAccommodation;
        private String data;
        private String author;
        private String reviewText;
        private float rating;
        private Status status;

        public Builder setApproved(Status approved) {
        	this.status=approved;
        	return this;
        }
        public Builder setId(int id) {
            this.id = id;
            return this;
        }
        
        public Builder setIdAccommodation(int idAccommodation) {
            this.idAccommodation = idAccommodation;
            return this;
        }
        
        public Builder setNameAccommodation(String nameAccommodation) {
			this.nameAccommodation = nameAccommodation;
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