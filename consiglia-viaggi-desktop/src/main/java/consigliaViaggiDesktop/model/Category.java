package consigliaViaggiDesktop.model;

public enum Category{
	RESTAURANT("Restaurant"),
	ATTRACTION("Attraction"),
	HOTEL("Hotel");

	public final String label;

	Category(final String label){
		this.label=label;
	}


	public static boolean is(String category){
		return (category.equals(RESTAURANT.toString())||
				category.equals(ATTRACTION.toString())||
				category.equals(HOTEL.toString()));
	}
	public String getCategoryName(){
		return label;
	}
}