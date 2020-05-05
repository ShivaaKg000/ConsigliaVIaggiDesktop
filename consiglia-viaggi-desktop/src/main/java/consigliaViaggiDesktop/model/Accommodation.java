package consigliaViaggiDesktop.model;

public class Accommodation{

    private Integer id;
    private String name;
    private String description;
    private String logoUrl;
    private String images;
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
    public String getImages() {
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
    public String getCity(){
        return accommodationLocation.getCity();
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
        this.accommodationLocation= new Location.Builder()
                .setAddress(builder.address)
                .setCity(builder.city)
                .setLatitude(builder.latitude)
                .setLongitude(builder.longitude)
                .build();
    }

    static class Builder {

        private Integer id;
        private String description;
        private String name;
        private String logoUrl;
        private String images;
        private Float rating;
        private Subcategory subcategory;
        private Category category;

        private String city;
        private String address;
        private Double latitude;
        private Double longitude;


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


        public Builder setImages(String images) {
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

        public Builder setLatitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }
        public Builder setLongitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }
        public Builder setCity(String city) {
            this.city = city;
            return this;
        }
        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Accommodation create() {
            return new Accommodation(this);
        }
    }

}
