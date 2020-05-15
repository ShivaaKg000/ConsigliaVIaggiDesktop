package consigliaViaggiDesktop.model;
import com.google.gson.*;
import consigliaViaggiDesktop.Constants;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccommodationDaoJSON implements AccommodationDao {

	@Override
	public List<Accommodation> getAccommodationList(String category, String subCategory,String searchParam)  {
		List<Accommodation> accommodationList= (List<Accommodation>) getAccommodationListJSONParsing(searchParam);
		System.out.print(accommodationList);
		return accommodationList;
	}

	@Override
	public Accommodation getAccommodationById(int id) {
		return getAccommodationJSON(id);

	}

	@Override
	public String createAccommodation(Accommodation accommodation) {
		return String.valueOf(createAccommodationJSON(accommodation));

	}

	private JsonObject createAccommodationJSON(Accommodation accommodation) {
		JsonObject jsonAccommodation=encodeAccommodation(accommodation);

		return jsonAccommodation;
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

	private Collection<Accommodation> getAccommodationListJSONParsing(String city)  {
		String urlString= Constants.GET_ACCOMMODATION_LIST_URL+Constants.CITY_PARAM+city;


		BufferedReader bufferedReader = null;
		try {
			bufferedReader = getJSONFromUrl(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// if(bufferReader!=null) ... ??
		JsonElement jsonTree  = JsonParser.parseReader(bufferedReader);
		Collection<Accommodation> accommodationCollection = new ArrayList<>();
		if (jsonTree .isJsonArray()) {
			JsonArray array= jsonTree.getAsJsonArray();
            for (JsonElement jo : array) {
                JsonObject accommodationJson = (JsonObject)jo ;
                accommodationCollection.add(parseAccommodation(accommodationJson));
            }
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
}




