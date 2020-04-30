package consigliaViaggiDesktop.model;

public enum Category{
	RESTAURANT("Restaurant"),
	ATTRACTION("Attraction"),
	HOTEL("Hotel");

	public final String label;

	private Category(final String label){
		this.label=label;
	}

	public String getCategoryName(){
		return label;
	}
}