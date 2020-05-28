package consigliaViaggiDesktop.model.DAO;
import com.google.gson.*;
import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.LoginController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.Category;
import consigliaViaggiDesktop.model.DTO.JsonPageResponse;
import consigliaViaggiDesktop.model.SearchParams;
import consigliaViaggiDesktop.model.Subcategory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AccommodationDaoJSON implements AccommodationDao {

	@Override
	public JsonPageResponse<Accommodation> getAccommodationList(SearchParams params)  {

		JsonPageResponse<Accommodation> accommodationList=  getAccommodationListJSONParsing(params);
		System.out.print(accommodationList);
		return accommodationList;
	}

	@Override
	public Accommodation getAccommodationById(int id) {
		return getAccommodationJSON(id);

	}

	@Override
	public Accommodation createAccommodation(Accommodation accommodation) throws IOException, DaoException {

		JsonObject jsonAccommodation=encodeAccommodation(accommodation);

		return parseAccommodation(createAccommodationJSON(jsonAccommodation));

	}

	@Override
	public Boolean deleteAccommodation(int idAccommodation) throws DaoException {

		return deleteAccommodationJSON(idAccommodation);
	}

	@Override
	public Boolean editAccommodation(Accommodation accommodation) throws DaoException {
		JsonObject jsonAccommodation=encodeAccommodation(accommodation);
		return  editAccommodationJson(jsonAccommodation);
	}

	private Boolean editAccommodationJson(JsonObject accommodation) throws DaoException {
		int responseCode=0;
		HttpURLConnection connection = null;
		try {
			connection = createAuthenticatedConnection(Constants.EDIT_ACCOMMODATION_URL, "PUT");
			writeOutputStream(connection, accommodation.toString());
			responseCode=connection.getResponseCode();
			//BufferedReader jsonResponse = null;
		} catch (IOException e) {
			throw new DaoException(DaoException.ERROR,"Errore di rete");
		}
		if(responseCode!=HttpsURLConnection.HTTP_OK){
			if (responseCode== HttpURLConnection.HTTP_NOT_FOUND) {
				throw  new DaoException(DaoException.NOT_FOUND,"Record non trovato");
			}
			else
				throw  new DaoException(DaoException.ERROR,"Bad Request");
		}
		else return true;
	}

	private Boolean deleteAccommodationJSON(int idAccommodation) throws DaoException {

		int responseCode=0;
		HttpURLConnection connection = null;
		try {
			connection = createAuthenticatedConnection(Constants.DELETE_ACCOMMODATION_URL+idAccommodation, "DELETE");
			responseCode=connection.getResponseCode();
			//BufferedReader jsonResponse = null;
		} catch (IOException e) {
			throw new DaoException(DaoException.ERROR,"Errore di rete");
		}

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

	private JsonPageResponse <Accommodation> getAccommodationListJSONParsing(SearchParams params)  {
		String urlString= Constants.GET_ACCOMMODATION_LIST_URL+
				Constants.QUERY_PARAM+
				URLEncoder.encode(params.getCurrentSearchString(), StandardCharsets.UTF_8)+
				Constants.CATEGORY_PARAM+
				params.getCurrentCategory()+
				Constants.SUBCATEGORY_PARAM+
				params.getCurrentSubCategory()+
				Constants.PAGE_PARAM+
				params.getCurrentpage();

		System.out.print("\n"+urlString);
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = getJSONFromUrl(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//JsonObject jsonObject= JsonParser.parseReader(bufferedReader).getAsJsonObject();
		//JsonArray accommodationListJson=jsonObject.get("content").getAsJsonArray();
		/*JsonElement jsonTree  = JsonParser.parseReader(bufferedReader);
		Collection<Accommodation> accommodationCollection = new ArrayList<>();
		if (jsonTree .isJsonArray()) {
			JsonArray array= jsonTree.getAsJsonArray();
            for (JsonElement jo : array) {
                JsonObject accommodationJson = (JsonObject)jo ;
                accommodationCollection.add(parseAccommodation(accommodationJson));
            }
			System.out.print(accommodationCollection);
		}*/

		JsonObject jsonObject= JsonParser.parseReader(bufferedReader).getAsJsonObject();
		JsonPageResponse<Accommodation> pageResponse = parseAccommodationsPage(jsonObject);
		List<Accommodation> ac = pageResponse.getContent();
		return pageResponse;
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

	private JsonObject createAccommodationJSON(JsonObject accommodationJson) throws IOException, DaoException {

		HttpURLConnection connection = createAuthenticatedConnection(Constants.CREATE_ACCOMMODATION_URL, "POST");
		int responseCode=0;

		writeOutputStream(connection,accommodationJson.toString());
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

	private HttpURLConnection createAuthenticatedConnection(String urlString,String requestMethod) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection;
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(requestMethod);
		connection.setDoOutput(true);
		connection.setRequestProperty("Authorization","Bearer "+LoginController.getInstance().getCurrentUserAuthenticationToken());
		connection.setRequestProperty("Content-Type","application/json");
		return connection;
	}

	private void writeOutputStream(HttpURLConnection connection,String stream) throws IOException {
		OutputStream os = connection.getOutputStream();
		byte[] input = stream.getBytes(StandardCharsets.UTF_8);
		os.write(input, 0, input.length);
	}

	private JsonPageResponse getAccommodationPage(SearchParams params)  {

		String urlString="http://localhost:5000/accommodation?city=napoli&page=0";

		System.out.print("\n"+urlString);
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = getJSONFromUrl(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		JsonObject jsonObject= JsonParser.parseReader(bufferedReader).getAsJsonObject();
		JsonPageResponse<Accommodation> pageResponse = parseAccommodationsPage(jsonObject);
		List<Accommodation> ac = pageResponse.getContent();
		//JsonObject accommodationJson = JsonParser.parseString(gson.toJson(bufferedReader)).getAsJsonObject();
		System.out.println("\nAuto encoded JsonPage: "+ac.get(0).getName());

		return pageResponse;
	}

	private JsonPageResponse parseAccommodationsPage(JsonObject jsonPage){

		List<Accommodation> accommodationCollection = new ArrayList<>();
		JsonArray array= jsonPage.get("content").getAsJsonArray();
		for (JsonElement jo : array) {
			JsonObject accommodationJson = (JsonObject)jo ;
			accommodationCollection.add(parseAccommodation(accommodationJson));
		}
		System.out.print(accommodationCollection);

		JsonPageResponse response = new JsonPageResponse<Accommodation>();
		response.setContent(accommodationCollection);
		response.setPage(jsonPage.get("page").getAsLong());
		response.setOffset(jsonPage.get("offset").getAsLong());
		response.setPageSize(jsonPage.get("pageSize").getAsLong());
		response.setTotalPages(jsonPage.get("totalPages").getAsLong());
		response.setTotalElements(jsonPage.get("totalElements").getAsLong());
		return response;
	}

}




