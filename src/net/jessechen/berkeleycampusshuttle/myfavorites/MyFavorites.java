package net.jessechen.berkeleycampusshuttle.myfavorites;

import net.jessechen.berkeleycampusshuttle.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyFavorites extends Activity {
	
	private String tempFavorites;
	private String[] favorites;
	private ListView lv;
	private ArrayAdapter<String> lvAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.myfavorites);
        tempFavorites = FileHandler.readFile(this);
		if (tempFavorites != null) { // check file exist & at least one favorite
			favorites = tempFavorites.split("\\n");

			lv = (ListView) findViewById(R.id.l_favorites);
			lvAdapter = new ArrayAdapter<String>(this, R.layout.list_item,
					favorites);
			lv.setAdapter(lvAdapter);
		} else {
			
		}
    }
}
