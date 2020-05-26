package consigliaViaggiDesktop.model.DAO;

import consigliaViaggiDesktop.model.Accommodation;
import java.io.IOException;
import java.util.List;

public interface AccommodationDao {
	List<Accommodation> getAccommodationList(String category, String subCategory, String searchParam, int page);
	Accommodation getAccommodationById(int id);
	Accommodation createAccommodation(Accommodation accommodation) throws IOException, DaoException;
	Boolean deleteAccommodation(int idAccommodation) throws IOException, DaoException;
	Boolean editAccommodation(Accommodation accommodation) throws IOException, DaoException;
}
