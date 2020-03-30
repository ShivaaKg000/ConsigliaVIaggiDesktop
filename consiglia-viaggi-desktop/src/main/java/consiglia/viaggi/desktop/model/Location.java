package consiglia.viaggi.desktop.model;

public class Location {

    private String city;
    private Double latitude;
    private Double longitude;
    private String address;

    public String getCity() {
        return city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }


    private Location(Builder builder){
        city=builder.city;
        latitude=builder.latitude;
        longitude=builder.longitude;
        address=builder.address;
    }

    static class Builder {

        private String city;
        private Double latitude;
        private Double longitude;
        private String address;


        public Builder setCity(String city) {
            this.city = city;
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

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }
        public Location build(){
            return new Location(this);
        }
    }



}
