/*******************************************************************************
 * Authors:
 *     Jesse Chen <contact@jessechen.net>
 * 
 * Copyright (c) 2011 Jesse Chen.
 * 
 * Berkeley Campus Shuttle is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Berkeley Campus Shuttle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Berkeley Campus Shuttle.  If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/
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

/**
 * Route is the activity shown after the user has chosen a specific route they
 * wanted to see. The activity displays all stops that the route serves for the
 * user to pick. When the user picks a specific stop, it will start the Stop
 * activity.
 * 
 * @author Jesse Chen
 * 
 */
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
		menu.add(R.string.addtofav);
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
		FileHandler
				.writeToFile(getApplicationContext(), routeName + "," + stop);
		return true;
	}
}
