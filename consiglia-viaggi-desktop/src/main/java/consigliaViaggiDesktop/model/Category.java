package consigliaViaggiDesktop.model;

public enum Category{

	NOT(""),
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

	@Override
	public String toString() {
		return label;
	}
}