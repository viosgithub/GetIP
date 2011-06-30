package foo.sample.GetIP;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class GetIP extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	Process proc = null;
	EditText etNum = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.button1).setOnClickListener(this);
        etNum = (EditText)findViewById(R.id.editText1);
    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
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
        	
	}
}