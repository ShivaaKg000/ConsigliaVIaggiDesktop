package consiglia.viaggi.desktop.model;

import java.util.Comparator;
import java.util.List;

public class Accommodation implements Comparable<Accommodation>{

    private Integer id;
    private String name;
    private String description;
    private String logoUrl;
    private List<String> images;
    private Float rating;
    private Subcategory subcategory;
    private Category category;
    private Location accommodationLocation;

    public Integer getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public String getName() {
        return name;
    }
    public String getLogoUrl() {
        return logoUrl;
    }
    public List<String> getImages() {
        return images;
    }
    public Float getRating() {
        return rating;
    }
    public Subcategory getSubcategory() {
        return subcategory;
    }
    public Category getCategory() {
        return category;
    }
    public Location getAccommodationLocation() {
        return accommodationLocation;
    }
    public Double getLatitude() {
        return accommodationLocation.getLatitude();
    }
    public Double getLongitude() {
        return accommodationLocation.getLongitude();
    }
    public String getAddress(){
        return accommodationLocation.getAddress();
    }

    private Accommodation(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description=builder.description;
        this.logoUrl = builder.logoUrl;
        this.images = builder.images;
        this.rating = builder.rating;
        this.subcategory = builder.subcategory;
        this.category = builder.category;
        this.accommodationLocation=builder.accommodationLocation;
    }

    @Override
    public int compareTo(Accommodation o) {
            Accommodation accommodation = (Accommodation) o;
            return Float.compare(this.rating, accommodation.getRating());
    }


    static class Builder {

        private Integer id;
        private String description;
        private String name;
        private String logoUrl;
        private List<String> images;
        private Float rating;
        private Subcategory subcategory;
        private Category category;
        private Location accommodationLocation;


        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String desc) {
            this.description = desc;
            return this;
        }


        public Builder setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }


        public Builder setImages(List<String> images) {
            this.images = images;
            return this;
        }


        public Builder setRating(float rating) {
            this.rating = rating;
            return this;
        }


        public Builder setSubcategory(Subcategory subcategory) {
            this.subcategory = subcategory;
            return this;
        }

        public Builder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public Builder setAccommodationLocation(Location accommodationLocation) {
            this.accommodationLocation = accommodationLocation;
            return this;
        }

        public Accommodation create() {
            return new Accommodation(this);
        }
    }

    public static class ReviewRatingComparator implements Comparator<Accommodation> {
        @Override
        public int compare(Accommodation ac1, Accommodation ac2) {
            return Float.compare(ac1.getRating(), ac2.getRating());
        }
    }

}
