package consigliaViaggiDesktop.model;

import java.util.List;

public interface AccommodationDao {
	List<Accommodation> getAccommodationList(String city);
	Accommodation getAccommodationById(int id);
}
