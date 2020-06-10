package consigliaViaggiDesktop.model;

public class SearchParamsReview {

    private String id;
    private String author;
    private String accommodationName;
    private String content;
    private String accommodationId;
    private String status;
    private String orderBy;
    int currentpage=0;

    public SearchParamsReview(Builder builder) {
        this.id=builder.id;
        this.currentpage=builder.currentpage;
        this.accommodationName=builder.accommodationName;
        this.content=builder.content;
        this.author=builder.author;
        this.status=builder.status;
        this.accommodationId=builder.accommodationId;
        this.orderBy=builder.orderBy;
    }

    public SearchParamsReview() {
        this.id=null;
        this.currentpage=0;
        this.accommodationName=null;
        this.content=null;
        this.author=null;
        this.status="";
        this.accommodationId=null;
        this.orderBy="id";
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public String getContent() {
        return content;
    }

    public String getAccommodationId() {
        return accommodationId;
    }

    public String getStatus() {
        return status;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public static class Builder {

        private String id;
        private String author;
        private String accommodationName;
        private String content;
        private String accommodationId;
        private String status;
        private String orderBy;
        int currentpage=0;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }
        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }public Builder setAccommodationName(String accommodationName ) {
            this.accommodationName = accommodationName;
            return this;
        }public Builder setContent(String content) {
            this.content = content;
            return this;
        }public Builder setAccommodationId(String accommodationId) {
            this.accommodationId = accommodationId;
            return this;
        }public Builder setStatus(String status) {
            this.status = status;
            return this;
        }
        public Builder setCurrentPage(int page){
            this.currentpage=page;
            return this;
        }
        public Builder setOrderBy(String orderBy){
            this.orderBy=orderBy;
            return this;
        }
        public SearchParamsReview build(){
            return new SearchParamsReview(this);
        }
    }
}

