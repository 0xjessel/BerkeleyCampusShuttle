package net.jessechen.ucberkeleyshuttle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AllRoutes extends Activity {
	
	private Button pButton, rButton, cButton, hButton;
	private Bundle b;
	private Intent intent;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.allroutes);
        
        pButton = (Button) findViewById(R.id.buttonP);
        pButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		intent = new Intent(AllRoutes.this, Route.class);        		
        		b = new Bundle();
        		b.putStringArray("route", getResources().getStringArray(R.array.p_stops));
        		b.putCharSequence("route_name", "Perimeter");
        		b.putInt("xml", R.raw.ptimes);
        		intent.putExtras(b);
        		startActivity(intent);
        	}
        });
        
        rButton = (Button) findViewById(R.id.buttonR);
        rButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		intent = new Intent(AllRoutes.this, Route.class);
        		b = new Bundle();
        		b.putStringArray("route", getResources().getStringArray(R.array.r_stops));
        		b.putCharSequence("route_name", "Reverse");
        		b.putInt("xml", R.raw.rtimes);
        		intent.putExtras(b);
        		startActivity(intent);
        	}
        });
        
        cButton = (Button) findViewById(R.id.buttonC);
        cButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		intent = new Intent(AllRoutes.this, Route.class);
        		b = new Bundle();
        		b.putStringArray("route", getResources().getStringArray(R.array.c_stops));
        		b.putCharSequence("route_name", "Central Campus Southside");
        		b.putInt("xml", R.raw.ctimes);
        		intent.putExtras(b);
        		startActivity(intent);
        	}
        });
        
        hButton = (Button) findViewById(R.id.buttonH);
        hButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		intent = new Intent(AllRoutes.this, Route.class);
        		b = new Bundle();
        		b.putStringArray("route", getResources().getStringArray(R.array.h_stops));
        		b.putCharSequence("route_name", "Hill");
        		b.putInt("xml", R.raw.htimes);
        		intent.putExtras(b);
        		startActivity(intent);
        	}
        });
    }
}