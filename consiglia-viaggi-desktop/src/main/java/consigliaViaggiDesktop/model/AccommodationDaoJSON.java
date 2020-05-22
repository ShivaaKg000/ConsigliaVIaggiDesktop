package consigliaViaggiDesktop.model;
import com.google.gson.*;
import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.LoginController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccommodationDaoJSON implements AccommodationDao {

	@Override
	public List<Accommodation> getAccommodationList(String category, String subCategory,String searchParam)  {

		List<Accommodation> accommodationList=null;

		if(Category.is(category) && !Subcategory.is(subCategory)){
			if(searchParam.length()>3)
				accommodationList= (List<Accommodation>) getAccommodationListGenericAndCategoryJSONParsing(searchParam,category);
			else accommodationList= (List<Accommodation>) getAccommodationListCategoryJSONParsing(category);
		}
		else if(Category.is(category) && Subcategory.is(subCategory)){
			if(searchParam.length()>3){
				accommodationList= (List<Accommodation>) getAccommodationListGenericAndSubCategoryJSONParsing(searchParam,subCategory);
			}
			else
				accommodationList= (List<Accommodation>) getAccommodationListSubCategoryJSONParsing(subCategory);
		}
		else accommodationList= (List<Accommodation>) getAccommodationListGenericJSONParsing(searchParam);

		System.out.print(accommodationList);
		return accommodationList;
	}

	private Collection<Accommodation> getAccommodationListGenericAndCategoryJSONParsing(String generic,String category)
	{
		String urlString= Constants.GET_ACCOMMODATION_LIST_URL+"/cat/"+generic+"/"+category;


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
		}

		return accommodationCollection;
	}


	private Collection<Accommodation> getAccommodationListGenericAndSubCategoryJSONParsing(String generic,String subcategory)
	{
		String urlString= Constants.GET_ACCOMMODATION_LIST_URL+"/sub/"+generic+"/"+subcategory;


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
		}

		return accommodationCollection;
	}
	@Override
	public Accommodation getAccommodationById(int id) {
		return getAccommodationJSON(id);

	}

	@Override
	public String createAccommodation(Accommodation accommodation) {
		return String.valueOf(createAccommodationJSON(accommodation));

	}

	private JsonElement createAccommodationJSON(Accommodation accommodation) {
		JsonObject jsonAccommodation=encodeAccommodation(accommodation);

		JsonObject response;
		try {
			return postAccommodation(jsonAccommodation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		response= JsonParser.parseString("\"error\":\"error\"").getAsJsonObject();
		return response;
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

	private Collection<Accommodation> getAccommodationListCategoryJSONParsing(String category)  {
		String urlString= Constants.GET_ACCOMMODATION_LIST_URL+Constants.CATEGORY_PARAM+category;


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
		}

		return accommodationCollection;
	}

	private Collection<Accommodation> getAccommodationListSubCategoryJSONParsing(String subcategory)
	{
		String urlString= Constants.GET_ACCOMMODATION_LIST_URL+Constants.SUBCATEGORY_PARAM+subcategory;


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
		}

		return accommodationCollection;
}

	private Collection<Accommodation> getAccommodationListGenericJSONParsing(String generic)
	{
		String urlString= Constants.GET_ACCOMMODATION_LIST_URL+Constants.GENERIC_PARAM+generic;
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

	private JsonElement postAccommodation(JsonObject accommodationJson) throws IOException {
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
		if(connection.getResponseCode()==401){
			System.out.print("\nResponse: Non autorizzato"); //bisogna implementare qualcosa
			return JsonParser.parseString("\"error\":\"error\"").getAsJsonObject();
		}
		// if json null???
		return convertToJsonElement(jsonResponse);
	}

	private JsonElement convertToJsonElement(BufferedReader json) {

		JsonElement object = JsonParser.parseReader(json);
		if(object.isJsonObject()){
			System.out.print("\nIs JsonObject ");
			return object;
		}
		else{
			System.out.print("\nNot JsonObject ");
			return null;
		}
	}
}




