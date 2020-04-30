package consigliaViaggiDesktop.model;

import java.util.*;

public enum Subcategory {

    //enums
    BAR(Category.RESTAURANT,"Bar"),
    PIZZERIA(Category.RESTAURANT,"Pizzeria"),
    TRATTORIA(Category.RESTAURANT,"Trattoria"),
    BNB(Category.HOTEL,"BnB"),
    HOSTEL(Category.HOTEL,"Ostello"),
    HOTEL(Category.HOTEL,"Hotel"),
    PARK(Category.ATTRACTION,"Parco"),
    MUSEUM(Category.ATTRACTION,"Museo")
    ;

    private String categoryName;
    private String subCategoryName;
    public static final Set<Subcategory> restaurants = EnumSet.range(BAR, TRATTORIA);
    public static final Set<Subcategory> hotels = EnumSet.range(BNB, HOTEL);
    public static final Set<Subcategory> attractions = EnumSet.range(PARK, MUSEUM);


    private Subcategory(Category cat,String subCat){

        this.categoryName=cat.getCategoryName();
        subCategoryName=subCat;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }
}
