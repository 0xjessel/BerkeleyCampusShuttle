package net.jessechen.ucberkeleyshuttle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShuttleMain extends Activity {
	
	private Button myButton, myButton2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        
        myButton = (Button) findViewById(R.id.my_button);
        myButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(ShuttleMain.this, AllStops.class);
        		startActivity(intent);
        	}
        });
        
        myButton2 = (Button) findViewById(R.id.my_button2);
        myButton2.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(ShuttleMain.this, AllRoutes.class);
        		startActivity(intent);
        	}
        });
    }
}