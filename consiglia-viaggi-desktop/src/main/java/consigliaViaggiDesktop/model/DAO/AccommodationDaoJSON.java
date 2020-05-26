package consigliaViaggiDesktop.model.DAO;
import com.google.gson.*;
import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.LoginController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.Category;
import consigliaViaggiDesktop.model.Subcategory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AccommodationDaoJSON implements AccommodationDao {

	@Override
	public List<Accommodation> getAccommodationList(String category, String subCategory, String searchParam, int page)  {

		List<Accommodation> accommodationList= (List<Accommodation>) getAccommodationListJSONParsing(searchParam,category,subCategory, page);
		System.out.print(accommodationList);
		return accommodationList;
	}

	@Override
	public Accommodation getAccommodationById(int id) {
		return getAccommodationJSON(id);

	}

	@Override
	public Accommodation createAccommodation(Accommodation accommodation) throws IOException, DaoException {
		return createAccommodationJSON(accommodation);

	}

	@Override
	public Boolean deleteAccommodation(int idAccommodation) throws IOException, DaoException {

		return deleteAccommodationJSON(idAccommodation);
	}

	@Override
	public Boolean editAccommodation(Accommodation accommodation) throws IOException, DaoException {
		return  editAccommodationJson(accommodation);
	}

	private Boolean editAccommodationJson(Accommodation accommodation) throws IOException, DaoException {
		URL url = new URL(Constants.EDIT_ACCOMMODATION_URL);
		HttpURLConnection connection = null;
		int responseCode=0;

		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("PUT");
		connection.setDoOutput(true);
		connection.setRequestProperty("Authorization","Bearer "+LoginController.getInstance().getCurrentUserAuthenticationToken());
		connection.setRequestProperty("Content-Type","application/json");

		OutputStream os = connection.getOutputStream();
		byte[] input = encodeAccommodation(accommodation).toString().getBytes(StandardCharsets.UTF_8);
		os.write(input, 0, input.length);

		responseCode=connection.getResponseCode();
		BufferedReader jsonResponse = null;

		if(responseCode!=HttpsURLConnection.HTTP_OK){
			if (responseCode== HttpURLConnection.HTTP_NOT_FOUND) {
				throw  new DaoException(DaoException.NOT_FOUND,"Record non trovato");
			}
			else
				throw  new DaoException(DaoException.ERROR,"Errore di rete");
		}
		else return true;
	}

	private Boolean deleteAccommodationJSON(int idAccommodation) throws IOException, DaoException {
		URL url = new URL(Constants.DELETE_ACCOMMODATION_URL);
		HttpURLConnection connection = null;
		int responseCode=0;
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("DELETE");
		connection.setDoOutput(true);
		connection.setRequestProperty("Authorization","Bearer "+LoginController.getInstance().getCurrentUserAuthenticationToken());
		connection.setRequestProperty("Content-Type","application/json");

		responseCode=connection.getResponseCode();

		BufferedReader jsonResponse = null;

		if(responseCode!=HttpsURLConnection.HTTP_OK){
			if (responseCode== HttpURLConnection.HTTP_NOT_FOUND) {
				throw  new DaoException(DaoException.NOT_FOUND,"Record non trovato");
			}
			else
				throw  new DaoException(DaoException.ERROR,"Errore di rete");
		}
		else
			return true;
	}

	private Accommodation createAccommodationJSON(Accommodation accommodation) throws DaoException, IOException{

		JsonObject jsonAccommodation=encodeAccommodation(accommodation);
		JsonObject response;

		return parseAccommodation(postAccommodation(jsonAccommodation));

	}

	private Accommodation getAccommodationJSON(int id)  {
		String urlString= Constants.GET_ACCOMMODATION_LIST_URL+Constants.ACCOMMODATION_ID_PARAM+id;

		BufferedReader bufferedReader = null;
		try {
			bufferedReader = getJSONFromUrl(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// if(bufferReader!=null) ... ??
		JsonElement jsonTree  = JsonParser.parseReader(bufferedReader);
		JsonObject accommodationJSON = jsonTree.getAsJsonObject();
		return parseAccommodation(accommodationJSON);
	}

	private Collection<Accommodation> getAccommodationListJSONParsing(String query,String category,String subCategory, int page)  {
		String urlString= Constants.GET_ACCOMMODATION_LIST_URL+Constants.QUERY_PARAM+ URLEncoder.encode(query, StandardCharsets.UTF_8)+Constants.CATEGORY_PARAM+category
				+Constants.SUBCATEGORY_PARAM+subCategory+Constants.PAGE_PARAM+page;

		System.out.print("\n"+urlString);
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = getJSONFromUrl(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		JsonElement jsonTree  = JsonParser.parseReader(bufferedReader);
		Collection<Accommodation> accommodationCollection = new ArrayList<>();
		if (jsonTree .isJsonArray()) {
			JsonArray array= jsonTree.getAsJsonArray();
            for (JsonElement jo : array) {
                JsonObject accommodationJson = (JsonObject)jo ;
                accommodationCollection.add(parseAccommodation(accommodationJson));
            }
			System.out.print(accommodationCollection);
		}

		return accommodationCollection;
	}

	private Accommodation parseAccommodation(JsonObject accommodationJson){
		String name = accommodationJson.get("name").getAsString();
		int id = accommodationJson.get("id").getAsInt();
		String description = accommodationJson.get("description").getAsString();
		Double latitude= accommodationJson.get("latitude").getAsDouble();
		Double longitude= accommodationJson.get("longitude").getAsDouble();
		String address = accommodationJson.get("address").getAsString();
		float rating = accommodationJson.get("rating").getAsFloat();
		String city= accommodationJson.get("city").getAsString();
		String image = accommodationJson.get("images").getAsString();
		Category category = Category.valueOf(accommodationJson.get("category").getAsString());
		Subcategory subcategory = Subcategory.valueOf(accommodationJson.get("subCategory").getAsString());
		String logoURL="";
		if(accommodationJson.get("logoUrl") .isJsonObject()) {
			logoURL = accommodationJson.get("logoUrl").getAsString();
		}
		return new Accommodation.Builder()
				.setName(name)
				.setId(id)
				.setCity(city)
				.setImages(image)
				.setDescription(description)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setAddress(address)
				.setRating(rating)
				.setCategory(category)
				.setSubcategory(subcategory)
				.setLogoUrl(logoURL)
				.create();

	}

	private JsonObject encodeAccommodation(Accommodation accommodation){
		Gson gson = new Gson();
		JsonObject accommodationJson = JsonParser.parseString(gson.toJson(accommodation)).getAsJsonObject();
		System.out.println("\nAuto encoded: "+accommodationJson);
		accommodationJson.remove("accommodationLocation");
		accommodationJson.addProperty("city",accommodation.getCity());
		accommodationJson.addProperty("latitude",accommodation.getLatitude());
		accommodationJson.addProperty("longitude",accommodation.getLongitude());
		accommodationJson.addProperty("address",accommodation.getAddress());
		System.out.println("\nAuto encoded: "+accommodationJson);

		return accommodationJson;
	}

	private BufferedReader getJSONFromUrl(String urlString) throws MalformedURLException {
		URL url = new URL(urlString);
		HttpURLConnection connection = null;
		try {
			/*String auth = "admin" + ":" + "password";
			byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
			String authHeaderValue = "Basic " + new String(encodedAuth);*/
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			//connection.setRequestProperty("Authorization", authHeaderValue);
			connection.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader json  = null;
		try {
			json = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// if json null???
		return json;
	}

	private JsonObject postAccommodation(JsonObject accommodationJson) throws IOException, DaoException {
		URL url = new URL(Constants.CREATE_ACCOMMODATION_URL);
		HttpURLConnection connection = null;
		int responseCode=0;
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Authorization","Bearer "+LoginController.getInstance().getCurrentUserAuthenticationToken());
		connection.setRequestProperty("Content-Type","application/json");
		try (OutputStream os = connection.getOutputStream()) {
			System.out.print( "accommodationJson: "+accommodationJson.toString());
			byte[] input = accommodationJson.toString().getBytes(StandardCharsets.UTF_8);
			os.write(input, 0, input.length);

		}catch (Exception e){
			e.printStackTrace();
		}
		responseCode=connection.getResponseCode();

		BufferedReader jsonResponse = null;
		if (responseCode== HttpURLConnection.HTTP_CREATED) {
			jsonResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		}
		else if(connection.getResponseCode()==401){
				throw  new DaoException(DaoException.FORBIDDEN_ACCESS,"Non autorizzato");
			}
			else{
				throw  new DaoException(DaoException.ERROR,"Errore di rete");
			}

		return convertToJsonObject(jsonResponse);
	}

	private JsonObject convertToJsonObject(BufferedReader json) {
		return JsonParser.parseReader(json).getAsJsonObject();
	}
}




