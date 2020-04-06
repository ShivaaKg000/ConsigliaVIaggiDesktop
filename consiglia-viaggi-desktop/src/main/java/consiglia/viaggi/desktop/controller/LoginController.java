package consiglia.viaggi.desktop.controller;

public class LoginController {
	
	public static boolean authenticate(String user, String pswd)
	{
		if (user.equals("Ciro") && pswd.equals("Ciro"))
		{	
			return true;
			
		}
		else return false;
	}
	
	
		
}
