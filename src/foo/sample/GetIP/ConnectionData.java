package foo.sample.GetIP;

import android.text.format.Formatter;
import android.util.Log;

public class ConnectionData {
	private static final String[] STATES= {"ZERO error","SYN_SENT","SYN_RECV","FIN_EAIT1",
		"FIN_WAIT2","TIME_WAIT","CLOSE","CLOSE_WAIT","LAST_ACK","LISTEN","CLOSING"};
	public static final int TCP = 0;
	public static final int TCP6 = 1;
	public static final int UDP = 2;
	public static final int UDP6 = 3;
	public int connectionType,localIP,localPort,remoteIP,remotePort,state,uid;
	
	public ConnectionData() {
		// TODO Auto-generated constructor stub
		connectionType = localIP = localPort = remoteIP = remotePort = state = uid = -1;
	}
	
	
	public String getState()
	{
		if(state >= 1 && state <=11)
		{
			return STATES[state];
		}
		else
		{
			return "?";
		}
	}
	public void debugInfo()
	{
		Log.d("debug","start:debugInfo()");
		Log.d("debug","UID:"+uid);
		Log.d("debug","stat:"+getState());
		Log.d("debug","LocalIP:"+Formatter.formatIpAddress(localIP)+":"+localPort);
		Log.d("debug","remoteIP:"+Formatter.formatIpAddress(remoteIP)+":"+remotePort);
	}

}