package consigliaViaggiDesktop.model;

import java.util.*;

public enum Subcategory {

    //enums
    BLANK_RESTAURANT(Category.RESTAURANT,""),
    BAR(Category.RESTAURANT,"Bar"),
    PIZZERIA(Category.RESTAURANT,"Pizzeria"),
    TRATTORIA(Category.RESTAURANT,"Trattoria"),

    BLANK_HOTEL(Category.HOTEL,""),
    BNB(Category.HOTEL,"BnB"),
    HOSTEL(Category.HOTEL,"Ostello"),
    HOTEL(Category.HOTEL,"Hotel"),

    BLANK_ATTRACTION(Category.ATTRACTION,""),
    PARK(Category.ATTRACTION,"Parco"),
    MUSEUM(Category.ATTRACTION,"Museo");

    private final String categoryName;
    private final String subCategoryName;
    public static final Set<Subcategory> restaurants = EnumSet.range(BLANK_RESTAURANT, TRATTORIA);
    public static final Set<Subcategory> hotels = EnumSet.range(BLANK_HOTEL, HOTEL);
    public static final Set<Subcategory> attractions = EnumSet.range(BLANK_ATTRACTION, MUSEUM);

    @Override
    public String toString() {
        return subCategoryName;
    }

    Subcategory(Category cat, String subCat){

        this.categoryName=cat.getCategoryName();
        subCategoryName=subCat;
    }



}
