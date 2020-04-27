package consigliaViaggiDesktop.model;

import java.util.List;

public interface AccommodationDao {
	List<Accommodation> getAccommodationList(int id);
	Accommodation getAccommodationById(int id);
}
