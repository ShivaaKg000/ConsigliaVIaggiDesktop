package consigliaViaggiDesktop.model.DAO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import consigliaViaggiDesktop.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class LoginDaoSpring implements LoginDao{

    private String authenticatedUserToken;
    @Override
    public boolean authenticate(String username, String pwd) throws DaoException {
        BufferedReader jsonResponse = null;
        try {
            jsonResponse = getJsonResponseFromLoginUrl(username, pwd);
        } catch (IOException e) {
            throw new DaoException(DaoException.ERROR,e.getMessage());
        }
        if (jsonResponse != null) {
            String token = getTokenFromJsonResponse(jsonResponse);
            System.out.print(token);
            if (token != null) {
                if (checkIfAdmin(token)) {
                    authenticatedUserToken=token;
                    return true;
                } else {
                    //not an Admin
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public String getAuthenticatedUserToken() {
        return authenticatedUserToken;
    }

    private String getTokenFromJsonResponse(BufferedReader reader){
        JsonElement jsonTree  = JsonParser.parseReader(reader);
        JsonObject jsonResponse = jsonTree.getAsJsonObject();
        return jsonResponse.get("token").getAsString();
    }

    private BufferedReader getJsonResponseFromLoginUrl(String user, String pwd) throws IOException {
        URL url = new URL(Constants.LOGIN_URL);
        HttpURLConnection connection;
        int responseCode;

        String jsonInputString = "{\"username\":\"" + user + "\", \"password\": \"" + pwd + "\"}";
        System.out.print(jsonInputString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        OutputStream os = connection.getOutputStream();
        byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);


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
