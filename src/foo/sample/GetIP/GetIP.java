package foo.sample.GetIP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
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
    private int pid = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.button1).setOnClickListener(this);
        etNum = (EditText)findViewById(R.id.editText1);
    }
    @Override
    public void onStart()
    {
    	super.onStart();
    	pid = -1;
    	String myPackageName = getApplicationContext().getPackageName();
    	Log.d("debug","myPackage name is:"+myPackageName);
    	ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
    	List<RunningAppProcessInfo> runningApp = am.getRunningAppProcesses();
    	for(RunningAppProcessInfo app:runningApp)
    	{
    		if(app.processName.equals(myPackageName))
    		{
    			pid = app.pid;
    		}
    	}
    	
    	
    	
    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		/*
		int i = Integer.valueOf(etNum.getText().toString());
				Log.d("debug","try to get proccess" + i);
            try {
				 proc = Runtime.getRuntime().exec(
				     "/proc/" + Integer.toString(i)+ "/net/tcp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(proc != null)
			{
				Log.d("debug",proc.getOutputStream().toString());
			}
			*/
			getApplicationInfo().
			g
            try {
            	int i = Integer.valueOf(etNum.getText().toString());
				Log.d("debug","try to get proccess" + i);
				 proc = Runtime.getRuntime().exec(
				     "cat /proc/"+i+"/net/tcp6");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(proc != null)
			{
               br = new BufferedReader(new InputStreamReader(proc.getInputStream()), 4056);
               try{
               while(true)
               {
					outStr = br.readLine();
            	  if (outStr != null && outStr.length() != 0)
            		  {
            		  	Log.d("debug","line:"+outStr);
            		  }
            	  else
            	  {
            		  break;
            	  }
               }
               }
               catch(IOException e)
               {
            	  e.printStackTrace(); 
               }
			}
        	
	}
}