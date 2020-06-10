package consigliaViaggiDesktop.model;

public class SearchParamsAccommodation {

    private String currentSearchString,currentCategory,currentSubCategory;
    private String orderBy;
    private String direction;
    long currentpage;

    public SearchParamsAccommodation(Builder builder) {
        this.currentCategory=builder.currentCategory;
        this.currentpage=builder.currentpage;
        this.currentSubCategory=builder.currentSubCategory;
        this.currentSearchString=builder.currentSearchString;
        this.direction=builder.direction;
        this.orderBy=builder.orderBy;
    }

    public static class Builder {
        private String currentSearchString="",currentCategory="",currentSubCategory="";
        private String orderBy="id";
        private String direction="DESC";
        long currentpage=0;

        public Builder setCurrentSearchString(String currentSearchParam) {
            this.currentSearchString = currentSearchParam;
            return this;
        }

        public Builder setCurrentCategory(String currentCategory) {
            this.currentCategory = currentCategory;
            return this;
        }

        public Builder setCurrentSubCategory(String currentSubCategory) {
            this.currentSubCategory = currentSubCategory;
            return this;
        }

        public Builder setCurrentpage(long currentpage) {
            this.currentpage = currentpage;
            return this;
        }
        public Builder setOrderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }
        public Builder setDirection(String direction) {
            this.direction = direction;
            return this;
        }



        public SearchParamsAccommodation create() {
            return new SearchParamsAccommodation(this);
        }

    }

    public String getCurrentSearchString() {
        return currentSearchString;
    }

    public String getCurrentCategory() {
        return currentCategory;
    }

    public String getCurrentSubCategory() {
        return currentSubCategory;
    }

    public long getCurrentPage() {
        return currentpage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getDirection() {
        return direction;
    }



}
