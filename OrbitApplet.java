import java.applet.*;
import java.awt.*;
import java.net.*;
import java.io.*;


public class OrbitApplet
    extends Applet
{


    public static String usernameParam     = "username";
    public static String passwordParam     = "password";
    public static String servernameParam   = "servername";
    public static String portnumberParam   = "portnumber";
    public static String chatroomParam     = "chatroom";
    public static String widthParam        = "xsize";
    public static String heightParam       = "ysize";
    public static String usepasswordsParam = "usepasswords";
    public static String locksettingsParam = "locksettings";
    public static String autoconnectParam  = "autoconnect";
    public static String hidecanvasParam   = "hidecanvas";

    private OrbitWindow window;
    private String name = "";
    private String password = "";
    private String host = "";
    private String port = "";
    private String room = "";
    private int windowWidth = 0;
    private int windowHeight = 0;
    private boolean requirePasswords = true;
    private boolean lockSettings = false;
    private boolean autoConnect = false;
    private boolean showCanvas = true;
    

    public String getAppletInfo()
    {
	return ("VJTI Chat version " + Orbit.VERSION
		+ " by Aditya Malshikhare");
    }
    

    public String[][] getParameterInfo()
    {
	String[][] args = {
	    { usernameParam, "string", "User login handle" },
	    { passwordParam, "string", "User password" },
	    { servernameParam, "string",
	      "Internet hostname/address of VJTI Chat server" },
	    { portnumberParam, "integer 1-65535",
	      "TCP port number to use (default: 12468)" },
	    { chatroomParam, "string",
	      "Name of initial chat room" },
	    { widthParam, "integer > 0",
	      "Initial width of the client window" },
	    { heightParam, "integer > 0",
	      "Initial height of the client window" },
	    { usepasswordsParam, "no",
	      "Do not require the user to enter a password to connect" },
	    { locksettingsParam, "yes",
	      "Don't allow the user to change user name, server, or port" },
	    { autoconnectParam, "yes",
	      "Tells client to connect automatically on startup" },
	    { hidecanvasParam, "yes",
	      "Tells client to hide the drawing canvas on startup" }
	};
	
	return (args);
    }

    
    public void init()
    {


	if (getParameter(usernameParam) != null)
	    name = getParameter(usernameParam);

	if (getParameter(passwordParam) != null)
	    password = getParameter(passwordParam);

	if (getParameter(servernameParam) != null)
	    host = getParameter(servernameParam);

	if (getParameter(portnumberParam) != null)
	    port = getParameter(portnumberParam);

	if (getParameter(chatroomParam) != null)
	    room = getParameter(chatroomParam);

	if (getParameter(widthParam) != null)
	    windowWidth = Integer.parseInt(getParameter(widthParam));

	if (getParameter(heightParam) != null)
	    windowHeight = Integer.parseInt(getParameter(heightParam));


	if (getParameter(usepasswordsParam) != null)
	    {
		if (getParameter(usepasswordsParam).equals("no"))
		    requirePasswords = false;
	    }

	if (getParameter(locksettingsParam) != null)
	    {
		if (getParameter(locksettingsParam).equals("yes"))
		    lockSettings = true;
	    }

	if (getParameter(autoconnectParam) != null)
	    {
		if (getParameter(autoconnectParam).equals("yes"))
		    autoConnect = true;
	    }

	if (getParameter(hidecanvasParam) != null)
	    {
		if (getParameter(hidecanvasParam).equals("yes"))
		    showCanvas = false;
	    }

	if ((host == null) || host.equals(""))
	    host = "localhost";
	if ((port == null) || port.equals(""))
	    port = "12468";

	// Done
	return;
    }


    public void start()
    {
	window = new OrbitWindow(name, password, host, port, showCanvas,
				   getCodeBase());
	
	Dimension tmpSize = window.getSize();
	if (windowWidth > 0)
	    tmpSize.width = windowWidth;
	if (windowHeight > 0)
	    tmpSize.height = windowHeight;
	window.setSize(tmpSize);

	window.setApplet(this);

	window.requirePassword = requirePasswords;

	window.lockSettings = lockSettings;

	window.show();

	if (autoConnect)
	    window.connect();

	if (!room.equals(""))
	    if (window.theClient != null)
		{
		    try {
			window.theClient.sendEnterRoom(room, false, "");
		    }
		    catch (IOException e) {
			window.theClient.lostConnection();
			return;
		    }
		}

	return;
    }


    public void stop()
    {
	return;
    }

    public void destroy()
    {
	if (window.connected == true)
	    window.disconnect();
	window.dispose();
	return;
    }
}
