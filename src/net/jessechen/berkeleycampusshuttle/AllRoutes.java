package net.jessechen.berkeleycampusshuttle;

import net.jessechen.berkeleycampusshuttle.routes.CENTRAL;
import net.jessechen.berkeleycampusshuttle.routes.HILL;
import net.jessechen.berkeleycampusshuttle.routes.PERIMETER;
import net.jessechen.berkeleycampusshuttle.routes.REVERSE;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class AllRoutes extends Activity {
	
	private ImageButton pButton, rButton, cButton, hButton;
	private Bundle b;
	private Intent intent;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.allroutes);
        
        pButton = (ImageButton) findViewById(R.id.buttonP);
        pButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		intent = new Intent(AllRoutes.this, Route.class);        		
        		b = new Bundle();
        		b.putStringArray("route", PERIMETER.getStops(getApplicationContext()));
        		b.putCharSequence("route_name", PERIMETER.getName());
        		b.putInt("xml", PERIMETER.getXML());
        		intent.putExtras(b);
        		startActivity(intent);
        	}
        });
        
        rButton = (ImageButton) findViewById(R.id.buttonR);
        rButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		intent = new Intent(AllRoutes.this, Route.class);
        		b = new Bundle();
        		b.putStringArray("route", REVERSE.getStops(getApplicationContext()));
        		b.putCharSequence("route_name", REVERSE.getName());
        		b.putInt("xml", REVERSE.getXML());
        		intent.putExtras(b);
        		startActivity(intent);
        	}
        });
        
        cButton = (ImageButton) findViewById(R.id.buttonC);
        cButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		intent = new Intent(AllRoutes.this, Route.class);
        		b = new Bundle();
        		b.putStringArray("route", CENTRAL.getStops(getApplicationContext()));
        		b.putCharSequence("route_name", CENTRAL.getName());
        		b.putInt("xml", CENTRAL.getXML());
        		intent.putExtras(b);
        		startActivity(intent);
        	}
        });
        
        hButton = (ImageButton) findViewById(R.id.buttonH);
        hButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		intent = new Intent(AllRoutes.this, Route.class);
        		b = new Bundle();
        		b.putStringArray("route", HILL.getStops(getApplicationContext()));
        		b.putCharSequence("route_name", HILL.getName());
        		b.putInt("xml", HILL.getXML());
        		intent.putExtras(b);
        		startActivity(intent);
        	}
        });
    }
}