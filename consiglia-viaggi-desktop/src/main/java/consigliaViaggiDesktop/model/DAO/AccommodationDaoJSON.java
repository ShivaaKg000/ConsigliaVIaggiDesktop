package consigliaViaggiDesktop.model.DAO;
import com.google.common.net.HttpHeaders;
import com.google.gson.*;
import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.controller.LoginController;
import consigliaViaggiDesktop.model.Accommodation;
import consigliaViaggiDesktop.model.Category;
import consigliaViaggiDesktop.model.DTO.JsonPageResponse;
import consigliaViaggiDesktop.model.SearchParamsAccommodation;
import consigliaViaggiDesktop.model.Subcategory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AccommodationDaoJSON implements AccommodationDao {

	//SET
	@Override
	public Accommodation createAccommodation(Accommodation accommodation) throws IOException, DaoException {

		JsonObject jsonAccommodation=encodeAccommodation(accommodation);

		return parseAccommodation(createAccommodationJSON(jsonAccommodation));

	}
	private JsonObject createAccommodationJSON(JsonObject accommodationJson) throws IOException, DaoException {

		HttpURLConnection connection = createAuthenticatedConnection(Constants.CREATE_ACCOMMODATION_URL, "POST");
		int responseCode;

		writeOutputStream(connection,accommodationJson.toString());
		responseCode=connection.getResponseCode();

		BufferedReader jsonResponse;
		if (responseCode== HttpURLConnection.HTTP_CREATED) {
			jsonResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		}
		else if(connection.getResponseCode()==401){
			throw  new DaoException(DaoException.FORBIDDEN_ACCESS,"Non autorizzato");
		}
		else{
			throw  new DaoException(DaoException.ERROR,"Errore di rete");
		}
		return JsonParser.parseReader(jsonResponse).getAsJsonObject();
		//return convertToJsonObject(jsonResponse);
	}

	@Override
	public Boolean deleteAccommodation(int idAccommodation) throws DaoException {

		return deleteAccommodationJSON(idAccommodation);
	}
	private Boolean deleteAccommodationJSON(int idAccommodation) throws DaoException {

		int responseCode;
		HttpURLConnection connection;
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

	@Override
	public String uploadAccommodationImage(File image, String imgName) throws DaoException {
		return uploadImageToServer(image,imgName);
	}

	@Override
	public Boolean editAccommodation(Accommodation accommodation) throws DaoException {
		JsonObject jsonAccommodation=encodeAccommodation(accommodation);
		return  editAccommodationJson(jsonAccommodation);
	}
	private Boolean editAccommodationJson(JsonObject accommodation) throws DaoException {
			int responseCode;
			HttpURLConnection connection;
			try {
				connection = createAuthenticatedConnection(Constants.EDIT_ACCOMMODATION_URL, "PUT");
				writeOutputStream(connection, accommodation.toString());
				responseCode=connection.getResponseCode();
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

	private void writeOutputStream(HttpURLConnection connection,String stream) throws IOException {
		OutputStream os = connection.getOutputStream();
		byte[] input = stream.getBytes(StandardCharsets.UTF_8);
		os.write(input, 0, input.length);
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

	// Authenticate for Create/Delete/Edit methods
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

	// GET
	@Override
	public JsonPageResponse<Accommodation> getAccommodationList(SearchParamsAccommodation params) throws DaoException {

		JsonPageResponse<Accommodation> accommodationList=  getAccommodationListJSONParsing(params);
		return accommodationList;
	}
	private JsonPageResponse <Accommodation> getAccommodationListJSONParsing(SearchParamsAccommodation params) throws DaoException {
		String urlString= Constants.GET_ACCOMMODATION_LIST_URL+
				Constants.QUERY_PARAM+
				URLEncoder.encode(params.getCurrentSearchString(), StandardCharsets.UTF_8)+
				Constants.CATEGORY_PARAM+
				params.getCurrentCategory()+
				Constants.SUBCATEGORY_PARAM+
				params.getCurrentSubCategory()+"&"+
				Constants.PAGE_PARAM+
				params.getCurrentpage();

		System.out.print("\n"+urlString);
		BufferedReader bufferedReader;
		try {
			bufferedReader = getJSONFromUrl(urlString);
		} catch (MalformedURLException e) {
			throw new DaoException(DaoException.ERROR,e.getMessage());

		}
		JsonObject jsonObject= JsonParser.parseReader(bufferedReader).getAsJsonObject();

		return parseAccommodationsPage(jsonObject);
	}

	private JsonPageResponse<Accommodation> parseAccommodationsPage(JsonObject jsonPage){

		List<Accommodation> accommodationCollection = new ArrayList<>();
		JsonArray array= jsonPage.get("content").getAsJsonArray();
		for (JsonElement jo : array) {
			JsonObject accommodationJson = (JsonObject)jo ;
			accommodationCollection.add(parseAccommodation(accommodationJson));
		}
		JsonPageResponse<Accommodation> response = new JsonPageResponse<>();
		response.setContent(accommodationCollection);
		response.setPage(jsonPage.get("page").getAsLong());
		response.setOffset(jsonPage.get("offset").getAsLong());
		response.setPageSize(jsonPage.get("pageSize").getAsLong());
		response.setTotalPages(jsonPage.get("totalPages").getAsLong());
		response.setTotalElements(jsonPage.get("totalElements").getAsLong());
		return response;
	}

	@Override
	public Accommodation getAccommodationById(int id) throws DaoException {
		return getAccommodationJSON(id);

	}
	private Accommodation getAccommodationJSON(int id) throws DaoException {
		String urlString= Constants.GET_ACCOMMODATION_LIST_URL+"?"+Constants.ACCOMMODATION_ID_PARAM+id;

		BufferedReader bufferedReader;
		try {
			bufferedReader = getJSONFromUrl(urlString);
		} catch (MalformedURLException e) {
			throw new DaoException(DaoException.ERROR,e.getMessage());
		}
		JsonElement jsonTree  = JsonParser.parseReader(bufferedReader);
		JsonObject accommodationJSON = jsonTree.getAsJsonObject();
		return parseAccommodation(accommodationJSON);
	}

	private BufferedReader getJSONFromUrl(String urlString) throws MalformedURLException, DaoException {
		URL url = new URL(urlString);
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.getResponseCode();
		} catch (IOException e) {
			throw new DaoException(DaoException.ERROR,e.getMessage());
		}
		BufferedReader json;
		try {
			json = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			throw new DaoException(DaoException.ERROR,e.getMessage());
		}
		return json;
	}


	private String uploadImageToServer(File image, String imageName) throws DaoException {
		HttpClient httpclient = HttpClientBuilder.create()
				.build();

		HttpPost post = new HttpPost(Constants.SERVER_URL+"accommodation/image/");
		String message = "This is a multipart post";
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addBinaryBody
				("file", image, ContentType.DEFAULT_BINARY, imageName+".jpg" );
		builder.addTextBody("text", message, ContentType.TEXT_PLAIN);

		HttpEntity entity = builder.build();
		post.setEntity(entity);
		post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + LoginController.getInstance().getCurrentUserAuthenticationToken());
		BufferedReader jsonResponse;
		String imgUrl = null;
		try {
			HttpResponse response = httpclient.execute(post);
			if(response.getStatusLine().getStatusCode()!=200)
				throw new DaoException(DaoException.ERROR,"Server Error:"+response.getStatusLine());
			jsonResponse = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			imgUrl = JsonParser.parseReader(jsonResponse).getAsJsonObject().get("imgUrl").getAsString();
			System.out.println(imgUrl);
			System.out.println(response.getStatusLine());
		} catch (IOException e) {
			throw new DaoException(DaoException.ERROR,e.getMessage());
		}
		return imgUrl;
	}

}





