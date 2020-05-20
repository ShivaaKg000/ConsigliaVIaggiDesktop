package consigliaViaggiDesktop.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import consigliaViaggiDesktop.Constants;
import consigliaViaggiDesktop.model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LoginController {

	private static LoginController loginController;
	private User currentUser;

	public String getCurrentUserAuthenticationToken(){
		return currentUser.getAuthToken();
	}

	public static LoginController getInstance(){
		if(loginController ==null)
			loginController = new LoginController();
		return loginController;
	}
	
	public boolean authenticate(String username, String pwd)
	{
		try {
			BufferedReader jsonResponse = getJsonResponseFromLoginUrl(username,pwd);
			if(jsonResponse!=null) {
				String token = getTokenFromJsonResponse(jsonResponse);
				System.out.print(token);
				if (token != null) {
					if (checkIfAdmin(token)) {
						saveUserInstance(token, username);
						return true;
					} else {
						//not an Admin
						return false;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	private void saveUserInstance(String token, String username) {
		currentUser=new User(username);
		currentUser.setToken(token);

	}


	private String getTokenFromJsonResponse(BufferedReader reader){
		JsonElement jsonTree  = JsonParser.parseReader(reader);
		JsonObject jsonResponse = jsonTree.getAsJsonObject();
		return jsonResponse.get("token").getAsString();
	}

	private BufferedReader getJsonResponseFromLoginUrl(String user, String pwd) throws IOException {
		URL url = new URL(Constants.LOGIN_URL);
		HttpURLConnection connection = null;
		int responseCode = 0;

		String jsonInputString = "{\"username\":\"" + user + "\", \"password\": \"" + pwd + "\"}";
		System.out.print(jsonInputString);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/json; utf-8");
		try (OutputStream os = connection.getOutputStream()) {
			byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
			os.write(input, 0, input.length);

		}
		responseCode=connection.getResponseCode();

		BufferedReader jsonResponse = null;
		if (responseCode== HttpURLConnection.HTTP_OK) {
			jsonResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		}
		return jsonResponse; //potrebbe essere null
	}

	private boolean checkIfAdmin(String jwtToken){
		Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
		String[] parts = jwtToken.split("\\."); // split out the "parts" (header, payload and signature)
		//String headerJson = new String(decoder.decode(parts[0]));
		String payload = new String(decoder.decode(parts[1]));
		//String signatureJson = new String(decoder.decode(parts[2]));
		JsonObject jsonPayload = JsonParser.parseString(payload).getAsJsonObject();
		JsonArray roles=jsonPayload.getAsJsonArray("roles");
		for (JsonElement role :roles) {
			JsonObject roleAsObject=(JsonObject) role;
			if(roleAsObject.get("authority").getAsString().equals(Constants.ROLE_ADMIN)){
				System.out.print("\nIs Admin");
				return true;
			}
		}
		System.out.print("\nNot Admin");
		return false;
	}
	
	
		
}
