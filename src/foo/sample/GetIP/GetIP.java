package foo.sample.GetIP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class GetIP extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	Process proc = null;
	EditText etNum = null;
	BufferedReader br = null;
    String outStr = "";
    private int myPid = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.button1).setOnClickListener(this);
    }
    @Override
    public void onStart()
    {
    	super.onStart();
    	myPid = getMyPid();
    	if(myPid == -1)
    	{
    		Log.d("debug","fail to get myPid!!");
    	}
    	else
    	{
    		Log.d("debug","Succsess to get myPid:" + myPid);
    	}
    }
    private int getMyPid()
    {
    	int ret = -1;
    	String myPackageName = getApplicationContext().getPackageName();
    	Log.d("debug","myPackage name is:"+myPackageName);
    	ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
    	List<RunningAppProcessInfo> runningApp = am.getRunningAppProcesses();
    	for(RunningAppProcessInfo app:runningApp)
    	{
    		if(app.processName.equals(myPackageName))
    		{
    			ret = app.pid;
    		}
    	}
    	return ret;
    }
    private void getNetInfo()
    {
    	
    	PackageManager pm = this.getPackageManager();
    	ConnectionData connectionData = new ConnectionData();
    	
    	Log.d("debug","start:getNetInfo()");
            try {
            	//int i = Integer.valueOf(etNum.getText().toString());
				//Log.d("debug","try to get proccess" + i);
				 proc = Runtime.getRuntime().exec(
				     "cat /proc/"+myPid+"/net/tcp6");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(proc != null)
			{
				Log.d("debug","proc is succsessfylly worked");
               br = new BufferedReader(new InputStreamReader(proc.getInputStream()), 4056);
               try{
            	   int counter = 0;
               while(true)
               {
					outStr = br.readLine();
            	  if (outStr != null && outStr.length() != 0)
            		  {
            		  	if(counter == 0)
            		  		{
            		  			counter++;
            		  			continue;
            		  		}
            		  	
            		  	Log.d("debug",outStr);
            		  	StringTokenizer stk = new StringTokenizer(outStr, ": ");
            		  	Log.d("debug","try to tokenize");
            		  	int stkCoutner = 0;
            		  	while(stk.hasMoreTokens())
            		  	{
            		  		//if(stkCoutner == 1) connectionData.localIP = Integer.parseInt(stk.nextToken().substring(24),16);
            		  		if(stkCoutner == 1) connectionData.localIP = hexStr2UnsignedInt(stk.nextToken().substring(24));
            		  		else if(stkCoutner == 2) connectionData.localPort = Integer.parseInt(stk.nextToken(),16);
            		  		//else if(stkCoutner == 3) connectionData.remoteIP = Integer.parseInt(stk.nextToken().substring(24),16);
            		  		else if(stkCoutner == 3) connectionData.remoteIP = hexStr2UnsignedInt(stk.nextToken().substring(24));
            		  		else if(stkCoutner == 4) connectionData.remotePort = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 5) connectionData.state = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 11) connectionData.uid = Integer.parseInt(stk.nextToken(),10);
            		  		else stk.nextToken();
            		  		stkCoutner++;
            		  	}
            		  	/*
            		  	for(ApplicationInfo info:installedAppList)
            		  	{
            		  		if(connectionData.uid == info.uid) Log.d("debug","uid=" + info.uid + " packagename:" + info.packageName);
            		  	}
            		  	*/
            		  	Log.d("debug","uid=" + connectionData.uid + " packagename:" + pm.getNameForUid(connectionData.uid));
            		  	connectionData.debugInfo();
            		  }
            	  else
            	  {
            		  break;
            	  }
            	  counter ++;
               }
               }
               catch(IOException e)
               {
            	  e.printStackTrace(); 
               }
			}
    }
    public int hexStr2UnsignedInt(String num)
    {
    	int ret = 0;
    	for(int i=0;i<num.length();i++)
    	{
    		ret *= 16;
    		ret += Character.digit(num.charAt(i),16);
    	}
    	
    	return ret;
    }
	@Override
	public void onClick(View arg0) {
		getNetInfo();
	}
}