package net.jessechen.berkeleycampusshuttle;

import net.jessechen.berkeleycampusshuttle.myfavorites.FileHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Route extends Activity {
	
    private String routeName, stop;
    private String[] busStops;
    private Bundle b, c;
    private TextView title;
    private ListView lv;
    private ArrayAdapter<String> lvAdapter;
    private Intent intent;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.route);
        
        b = getIntent().getExtras();
        routeName = (String) b.getCharSequence("route_name");
        
        title = (TextView) findViewById(R.id.t_route);
        title.setText("Stops for " + routeName);
        
        busStops = b.getStringArray("route");
        java.util.Arrays.sort(busStops);

        lv = (ListView) findViewById(R.id.l_stops);
        lvAdapter = new ArrayAdapter<String>(this, R.layout.list_item, busStops);
        lv.setAdapter(lvAdapter);
        registerForContextMenu(lv);
        lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intent = new Intent(Route.this, Stop.class);

				c = new Bundle();
				c.putCharSequence("stop", ((TextView) view).getText());
				c.putCharSequence("route", routeName);
				c.putInt("xml", b.getInt("xml"));
				intent.putExtras(c);

				startActivity(intent);
			}
		});
	}
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add("Add to Favorites");
	}
    
    @Override
    public boolean onMenuItemSelected(int featureID, MenuItem item) {
    	AdapterView.AdapterContextMenuInfo info;
    	try {
    		info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    	} catch (ClassCastException e) {
    		return false;
    	}
    	stop = (String) lv.getItemAtPosition(info.position);
    	FileHandler.writeToFile(getApplicationContext(), routeName + "," + stop);
    	return true;
    }
}