package consigliaViaggiDesktop.model.DAO;

import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.SearchParams;

import java.io.IOException;
import java.util.List;

public interface AccommodationDao {
	List<Accommodation> getAccommodationList(SearchParams params);
	Accommodation getAccommodationById(int id);
	Accommodation createAccommodation(Accommodation accommodation) throws IOException, DaoException;
	Boolean deleteAccommodation(int idAccommodation) throws DaoException;
	Boolean editAccommodation(Accommodation accommodation) throws  DaoException;
}
