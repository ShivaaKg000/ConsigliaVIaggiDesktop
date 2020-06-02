package consigliaViaggiDesktop.model;

public class SearchParamsReview {

    private String currentSearchString;
    long currentpage=0;

    public SearchParamsReview(SearchParamsReview.Builder builder) {
        this.currentpage=builder.currentpage;
        this.currentSearchString=builder.currentSearchString;
    }

    public static class Builder {
        private String currentSearchString="";
        long currentpage=0;

        public SearchParamsReview.Builder setCurrentSearchString(String currentSearchParam) {
            this.currentSearchString = currentSearchParam;
            return this;
        }

        public SearchParamsReview.Builder setCurrentpage(long currentpage) {
            this.currentpage = currentpage;
            return this;
        }

        public SearchParamsReview create() {
            return new SearchParamsReview(this);
        }

    }

    public String getCurrentSearchString() {
        return currentSearchString;
    }

    public long getCurrentpage() {
        return currentpage;
    }


}
