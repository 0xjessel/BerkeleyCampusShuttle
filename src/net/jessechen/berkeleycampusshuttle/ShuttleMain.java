package net.jessechen.berkeleycampusshuttle;

import net.jessechen.berkeleycampusshuttle.myfavorites.MyFavorites;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShuttleMain extends Activity {
	
	private Button allStops, allRoutes, myFavorites;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        
        allStops = (Button) findViewById(R.id.b_allroutes);
        allStops.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(ShuttleMain.this, AllStops.class);
        		startActivity(intent);
        	}
        });
        
        allRoutes = (Button) findViewById(R.id.b_allstops);
        allRoutes.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(ShuttleMain.this, AllRoutes.class);
        		startActivity(intent);
        	}
        });
        
        myFavorites = (Button) findViewById(R.id.b_favorites);
        myFavorites.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent(ShuttleMain.this, MyFavorites.class);
        		startActivity(intent);
        	}
        });      
    }
}