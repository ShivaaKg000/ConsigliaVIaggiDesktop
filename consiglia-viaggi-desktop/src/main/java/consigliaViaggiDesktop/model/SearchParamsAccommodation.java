package consigliaViaggiDesktop.model;

public class SearchParamsAccommodation {

    private String currentSearchString,currentCategory,currentSubCategory;
    long currentpage=0;

    public SearchParamsAccommodation(Builder builder) {
        this.currentCategory=builder.currentCategory;
        this.currentpage=builder.currentpage;
        this.currentSubCategory=builder.currentSubCategory;
        this.currentSearchString=builder.currentSearchString;
    }

    public static class Builder {
        private String currentSearchString="",currentCategory="",currentSubCategory="";
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

    public long getCurrentpage() {
        return currentpage;
    }


}
