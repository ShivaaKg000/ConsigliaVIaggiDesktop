package consiglia.viaggi.desktop.controller;

public class LoginController {
	
	public static boolean authenticate(String user, String pswd)
	{
		if ( (user.equals("Ciro") && pswd.equals("Ciro")) || (user.equals("Alessandro") && pswd.equals("Alessandro")) || (user.equals("Paolo") && pswd.equals("Paolo")))
		{	
			return true;
			
		}
		else return false;
	}
	
	
		
}
