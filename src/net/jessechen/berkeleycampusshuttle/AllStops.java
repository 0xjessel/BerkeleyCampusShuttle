package net.jessechen.berkeleycampusshuttle;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * AllStops is the activity that displays all the stops that the Berkeley Campus
 * Shuttle provides. When user selects a stop, it will show them all routes that
 * stops there and their predictions to the specified stop.
 * 
 * @author Jesse Chen
 * 
 */
public class AllStops extends ListActivity {

	private String[] ALL_STOPS;
	private ListView lv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ALL_STOPS = getResources().getStringArray(R.array.all_stops);
		java.util.Arrays.sort(ALL_STOPS);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				ALL_STOPS));

		lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
