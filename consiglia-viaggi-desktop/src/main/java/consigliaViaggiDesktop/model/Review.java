package consigliaViaggiDesktop.model;


public class Review{

	private final int id;
	private final int accommodationId;
	private final String accommodationName;
	private final String author;
    private final String reviewText;
    private final float rating;
    private final String data;
    private final Status status;
   
    public Review(Builder builder) {
    	this.id=builder.id;
    	this.accommodationId =builder.accommodationId;
    	this.accommodationName =builder.accommodationName;
        this.author=builder.author;
        this.reviewText=builder.reviewText;
        this.rating=builder.rating;
        this.data= builder.data;
        this.status=builder.status;
  
    }
 
    public int getId() {
		return id;
	}
    public int getAccommodationId() {
  		return accommodationId;
  	}
    public String getAccommodationName() {
  		return accommodationName;
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
    public Status getStatus() { return status; }
    public String getStatusLabel() {
        return status.label;
    }

	public static class Builder {

    	private int id;
    	private int accommodationId;
    	private String accommodationName;
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
        
        public Builder setAccommodationId(int accommodationId) {
            this.accommodationId = accommodationId;
            return this;
        }
        
        public Builder setAccommodationName(String accommodationName) {
			this.accommodationName = accommodationName;
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
}
