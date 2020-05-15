package consigliaViaggiDesktop.controller;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginController {

	private static LoginController loginController;

	private User currentUser;

	public static LoginController getInstance(){
		if(loginController ==null)
			loginController = new LoginController();
		return loginController;
	}
	
	public boolean authenticate(String username, String pwd)
	{
		try {
			BufferedReader reader = getJSONFromUrl(Constants.LOGIN_URL,username,pwd);
			System.out.print(reader);


			JsonElement jsonTree  = JsonParser.parseReader(reader);
			JsonObject jsonResponse = jsonTree.getAsJsonObject();
			String token = jsonResponse.get("token").getAsString();
			if(token!=null) {
				System.out.print(jsonResponse);
				saveUserInstance(token, username);
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private void saveUserInstance(String token, String username) {

		currentUser=new User(username);
		currentUser.setToken(token);

	}


	private BufferedReader getJSONFromUrl(String urlString,String user,String pwd) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = null;
		try {
			String jsonInputString = "{\"username\":\"" + user + "\", \"password\": \"" + pwd + "\"}";
			System.out.print(jsonInputString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

				os.write(input, 0, input.length);
				os.close();
			}
			connection.getResponseCode();

		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader json = null;
		try {
			json = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			throw e;
		}
		return json;
	}
	
	
		
}
