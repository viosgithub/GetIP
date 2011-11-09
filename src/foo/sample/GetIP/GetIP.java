package foo.sample.GetIP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GetIP extends Activity{
    /** Called when the activity is first created. */
	ListView lv;
	Process proc = null;
	EditText etNum = null;
	BufferedReader br = null;
    String outStr = "";
    private int myPid = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    @Override
    public void onStart()
    {
    	super.onStart();
    	List<String> outputList = new ArrayList<String>();
    	myPid = getMyPid();
    	if(myPid == -1)
    	{
    		Log.d("debug","fail to get myPid!!");
    	}
    	else
    	{
    		Log.d("debug","Succsess to get myPid:" + myPid);
    	}
    	showNetInfo(outputList);
    	Log.d("debug","prepaering ListAdapter and ListView,the outputList:" + outputList);
    	ListAdapter la = (ListAdapter) new  ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1,outputList);
    	lv = (ListView)findViewById(R.id.listview);
    	lv.setAdapter(la);
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
    private void getTCP6Info(List<String> output)
    {
    	PackageManager pm = this.getPackageManager();
    	ConnectionData connectionData = new ConnectionData();
    	connectionData.setConnectionType(ConnectionData.TCP6);
    	
    	Log.d("debug","start:getTCP6Info()");
            try {
				 proc = Runtime.getRuntime().exec(
				     "cat /proc/"+myPid+"/net/tcp6");
			} catch (IOException e) {
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
            		  		if(stkCoutner == 1) connectionData.localIP = hexStr2UnsignedInt(stk.nextToken().substring(24));
            		  		else if(stkCoutner == 2) connectionData.localPort = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 3) connectionData.remoteIP = hexStr2UnsignedInt(stk.nextToken().substring(24));
            		  		else if(stkCoutner == 4) connectionData.remotePort = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 5) connectionData.state = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 11) connectionData.uid = Integer.parseInt(stk.nextToken(),10);
            		  		else stk.nextToken();
            		  		stkCoutner++;
            		  	}
            		  	String packageName = pm.getNameForUid(connectionData.uid);
            		  	Log.d("debug","uid=" + connectionData.uid + " packagename:" + packageName);
            		  	output.add("uid=" + connectionData.uid + " packagename:" + packageName);
            		  	try {
							Log.d("debug","App\' s path:"+pm.getApplicationInfo(packageName, 0).publicSourceDir);
							output.add("App\' s path:"+pm.getApplicationInfo(packageName, 0).publicSourceDir);
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
            		  	//connectionData.debugInfo();
            		  	connectionData.debugInfo(output);
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
    public void getTCPInfo(List<String> output)

    {
    	PackageManager pm = this.getPackageManager();
    	ConnectionData connectionData = new ConnectionData();
    	connectionData.setConnectionType(ConnectionData.TCP);
    	
    	Log.d("debug","start:getTCPInfo()");
            try {
				 proc = Runtime.getRuntime().exec(
				     "cat /proc/"+myPid+"/net/tcp");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(proc != null)
			{
				Log.d("debug","proc is succsessfylly worked");
               br = new BufferedReader(new InputStreamReader(proc.getInputStream()), 40056);
               try{
            	   int counter = -1;
               while(true)
               {
            	   Log.d("debug","Loog Counter = " + String.valueOf(counter));
					outStr = br.readLine();
            	  if (outStr != null)
            		  {
            		  			counter++;
            		  	if(counter == 0)
            		  		{
            		  			continue;
            		  		}
            		  	if(outStr.length() == 0)
            		  	{
            		  		continue;
            		  	}
            		  	
            		  	Log.d("debug",outStr);
            		  	StringTokenizer stk = new StringTokenizer(outStr, ": ");
            		  	Log.d("debug","try to tokenize");
            		  	int stkCoutner = 0;
            		  	while(stk.hasMoreTokens())
            		  	{
            		  		if(stkCoutner == 1) connectionData.localIP = hexStr2UnsignedInt(stk.nextToken());
            		  		else if(stkCoutner == 2) connectionData.localPort = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 3) connectionData.remoteIP = hexStr2UnsignedInt(stk.nextToken());
            		  		else if(stkCoutner == 4) connectionData.remotePort = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 5) connectionData.state = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 11) connectionData.uid = Integer.parseInt(stk.nextToken(),10);
            		  		else stk.nextToken();
            		  		stkCoutner++;
            		  	}
            		  	String packageName = pm.getNameForUid(connectionData.uid);
            		  	Log.d("debug","uid=" + connectionData.uid + " packagename:" + packageName);
            		  	output.add("uid=" + connectionData.uid + " packagename:" + packageName);
            		  	try {
							Log.d("debug","App\' s path:"+pm.getApplicationInfo(packageName, 0).publicSourceDir);
							output.add("App\' s path:"+pm.getApplicationInfo(packageName, 0).publicSourceDir);
						} catch (NameNotFoundException e) {
							Log.d("debug","Exception:NameNotFound");
							e.printStackTrace();
						}
            		  	//connectionData.debugInfo()
            		  	connectionData.debugInfo(output);
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
    public void getUDPInfo(List<String> output)
    {
    	PackageManager pm = this.getPackageManager();
    	ConnectionData connectionData = new ConnectionData();
    	connectionData.setConnectionType(ConnectionData.UDP);
    	Log.d("debug","start:getUDPInfo()");
            try {
				 proc = Runtime.getRuntime().exec(
				     "cat /proc/"+myPid+"/net/udp");
			} catch (IOException e) {
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
            		  		if(stkCoutner == 1) connectionData.localIP = hexStr2UnsignedInt(stk.nextToken());
            		  		else if(stkCoutner == 2) connectionData.localPort = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 3) connectionData.remoteIP = hexStr2UnsignedInt(stk.nextToken());
            		  		else if(stkCoutner == 4) connectionData.remotePort = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 5) connectionData.state = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 11) connectionData.uid = Integer.parseInt(stk.nextToken(),10);
            		  		else stk.nextToken();
            		  		stkCoutner++;
            		  	}
            		  	String packageName = pm.getNameForUid(connectionData.uid);
            		  	Log.d("debug","uid=" + connectionData.uid + " packagename:" + packageName);
            		  	output.add("uid=" + connectionData.uid + " packagename:" + packageName);
            		  	try {
							Log.d("debug","App\' s path:"+pm.getApplicationInfo(packageName, 0).publicSourceDir);
							output.add("App\' s path:"+pm.getApplicationInfo(packageName, 0).publicSourceDir);
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
            		  	//connectionData.debugInfo();
            		  	connectionData.debugInfo(output);
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
    void getUDP6Info(List<String> output)
    {
    	PackageManager pm = this.getPackageManager();
    	ConnectionData connectionData = new ConnectionData();
    	connectionData.setConnectionType(ConnectionData.UDP6);
    	
    	Log.d("debug","start:getUDP6Info()");
            try {
				 proc = Runtime.getRuntime().exec(
				     "cat /proc/"+myPid+"/net/udp6");
			} catch (IOException e) {
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
            		  		if(stkCoutner == 1) connectionData.localIP = hexStr2UnsignedInt(stk.nextToken().substring(24));
            		  		else if(stkCoutner == 2) connectionData.localPort = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 3) connectionData.remoteIP = hexStr2UnsignedInt(stk.nextToken().substring(24));
            		  		else if(stkCoutner == 4) connectionData.remotePort = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 5) connectionData.state = Integer.parseInt(stk.nextToken(),16);
            		  		else if(stkCoutner == 11) connectionData.uid = Integer.parseInt(stk.nextToken(),10);
            		  		else stk.nextToken();
            		  		stkCoutner++;
            		  	}
            		  	String packageName = pm.getNameForUid(connectionData.uid);
            		  	Log.d("debug","uid=" + connectionData.uid + " packagename:" + packageName);
            		  	output.add("uid=" + connectionData.uid + " packagename:" + packageName);
            		  	try {
							Log.d("debug","App's path:"+pm.getApplicationInfo(packageName, 0).publicSourceDir);
							output.add("App's path:"+pm.getApplicationInfo(packageName, 0).publicSourceDir);
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
            		  	//connectionData.debugInfo();
            		  	connectionData.debugInfo(output);
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
	public void showNetInfo(List<String> output) {
		getTCP6Info(output);
		getUDP6Info(output);
		getUDPInfo(output);
		getTCPInfo(output);
	}
}