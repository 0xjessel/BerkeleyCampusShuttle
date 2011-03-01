package net.jessechen.berkeleycampusshuttle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Route extends Activity {
	
    private CharSequence routeName;
    private String [] busStops;
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
        routeName = b.getCharSequence("route_name");

        //making a random comment
        
        title = (TextView) findViewById(R.id.route_title);
        title.setText("Stops for " + routeName);
        
        busStops = b.getStringArray("route");
        java.util.Arrays.sort(busStops);

        lv = (ListView) findViewById(R.id.list);
        lvAdapter = new ArrayAdapter<String>(this, R.layout.list_item, busStops);
        lv.setAdapter(lvAdapter);
        
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
}
