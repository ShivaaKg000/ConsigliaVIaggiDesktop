package consigliaViaggiDesktop.model.DAO;

import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.DTO.JsonPageResponse;
import consigliaViaggiDesktop.model.SearchParams;
import java.io.IOException;

public interface AccommodationDao {
	JsonPageResponse<Accommodation> getAccommodationList(SearchParams params) throws DaoException;
	Accommodation getAccommodationById(int id) throws DaoException;
	Accommodation createAccommodation(Accommodation accommodation) throws IOException, DaoException;
	Boolean deleteAccommodation(int idAccommodation) throws DaoException;
	Boolean editAccommodation(Accommodation accommodation) throws  DaoException;
}
