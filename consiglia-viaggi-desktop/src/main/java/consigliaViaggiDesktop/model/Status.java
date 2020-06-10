package consigliaViaggiDesktop.model;

import java.util.ArrayList;
import java.util.List;

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

    public static String getStatusByLabel(String label){
        if(label!=null) {
            if (label.equals(PENDING.label)) return PENDING.toString();
            if (label.equals(APPROVED.label)) return APPROVED.toString();
            if (label.equals(REJECTED.label)) return REJECTED.toString();
        }
        return "";
    }

    public static List<String> getStatusList(){
        List<String> result= new ArrayList();
        result.add("");
        result.add(PENDING.label);
        result.add(APPROVED.label);
        result.add(REJECTED.label);
        return result;
    }
}
