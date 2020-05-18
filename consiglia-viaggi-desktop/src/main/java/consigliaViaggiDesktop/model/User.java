package consigliaViaggiDesktop.model;

public class User {

	private final String userName ;

    public String getAuthToken() {
        return authToken;
    }

    private String authToken;

    public User(String userName) {
        this.userName = userName ;
    }

    public void setToken(String token) {
        authToken=token;
    }

    public String getUserName() {
        return userName ;
    }

}
