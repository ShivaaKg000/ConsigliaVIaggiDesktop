package consigliaViaggiDesktop.model;

import java.util.List;

public interface AccommodationDao {
	List<Accommodation> getAccommodationList(String category, String subCategory,String searchParam);
	Accommodation getAccommodationById(int id);
	String createAccommodation(Accommodation accommodation);
}
