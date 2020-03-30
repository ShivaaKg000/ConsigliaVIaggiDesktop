package consiglia.viaggi.desktop;

public class Login_Controller {
	
	public static boolean authenticate(String user, String pswd)
	{
		if (user.equals("Ciro") && pswd.equals("Ciro"))
		{	
			return true;
			
		}
		else return false;
	}
	
	
		
}
