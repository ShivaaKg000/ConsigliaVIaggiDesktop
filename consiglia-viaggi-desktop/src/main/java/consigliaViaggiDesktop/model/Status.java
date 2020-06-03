package consigliaViaggiDesktop.model;

public enum Status {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    public final String label;

    Status(final String label){
        this.label=label;
    }


    public static boolean is(String category){
        return (category.equals(PENDING.toString())||
                category.equals(APPROVED.toString())||
                category.equals(REJECTED.toString()));
    }
    public String getStatus(){
        return label;
    }
}
