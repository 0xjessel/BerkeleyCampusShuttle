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
package net.jessechen.berkeleycampusshuttle.myfavorites;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import net.jessechen.berkeleycampusshuttle.R;
import net.jessechen.berkeleycampusshuttle.Stop;
import net.jessechen.berkeleycampusshuttle.routes.CENTRAL;
import net.jessechen.berkeleycampusshuttle.routes.HILL;
import net.jessechen.berkeleycampusshuttle.routes.PERIMETER;
import net.jessechen.berkeleycampusshuttle.routes.REVERSE;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * MyFavorites is the activity that display favorite stops that the user has
 * saved. When the user adds a stop to their favorites, it is displayed here
 * where they can quickly jump to the predictions for the given stop
 * 
 * @author Jesse Chen
 * 
 */
public class MyFavorites extends Activity {

	private String[] favorites;
	private Intent intent;
	private Bundle c;
	private ListView lv;
	private ArrayAdapter<String> lvAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.myfavorites);

		lv = (ListView) findViewById(R.id.l_favorites);
		try {
			favorites = FileHandler.readFileWrapper(this);

			lvAdapter = new mAdapter(this, R.layout.favlist_item,
					new ArrayList<String>(Arrays.asList(favorites)));
			if (lvAdapter.getCount() == 0) {
				setOnEmpty();
			}
			lv.setAdapter(lvAdapter);
		} catch (FileNotFoundException e) {
			setOnEmpty();
		}
		registerForContextMenu(lv);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intent = new Intent(MyFavorites.this, Stop.class);

				String[] descrip = ((ImageView) view
						.findViewById(R.id.favlistpic)).getContentDescription()
						.toString().split(",");

				c = new Bundle();
				c.putCharSequence("stop", ((TextView) view
						.findViewById(R.id.favlisttext)).getText());
				c.putCharSequence("route", descrip[0]);
				c.putInt("xml", Integer.parseInt(descrip[1]));
				intent.putExtras(c);

				startActivity(intent);
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add("Remove from Favorites");
	}

	@Override
	public boolean onMenuItemSelected(int featureID, MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		} catch (ClassCastException e) {
			return false;
		}
		String stop = (String) lv.getItemAtPosition(info.position);
		FileHandler.deleteLine(getApplicationContext(), stop);
		lvAdapter.remove(stop);
		lvAdapter.notifyDataSetChanged();
		if (lvAdapter.getCount() == 0) {
			setOnEmpty();
		}
		return true;
	}

	/*
	 * If you remove a stop from Favorites, pressing back afterwards should
	 * update the lvAdapter by having that stop no longer be in Favorites.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() { // TODO: doesn't work
		super.onResume();
		lvAdapter.notifyDataSetChanged();
		if (lvAdapter.getCount() == 0) {
			setOnEmpty();
		}
	}

	/**
	 * When the user has not added any favorites yet, we need to show a TextView
	 * that notifies them that they have no favorites yet and describe how to
	 * add favorites.
	 * 
	 */
	private void setOnEmpty() { // show empty favorites dialog
		TextView empty = (TextView) findViewById(R.id.emptyfavlist);
		empty.setVisibility(View.VISIBLE);
		lv.setEmptyView(empty);
	}

	/**
	 * Custom ArrayAdapter that is used to show the route icon as well as the
	 * stop.
	 * 
	 * The current implementation uses a hack that embeds the xml ID of the
	 * corresponding route and the name of that route in the ImageView content
	 * description. Bad practice because eyes-free users using a screenreader
	 * uses the content description field to figure out what a button, or image
	 * do/is.
	 * 
	 * @author Jesse Chen
	 * 
	 */
	private class mAdapter extends ArrayAdapter<String> {
		private ArrayList<String> items;

		public mAdapter(Context context, int textViewResourceId,
				ArrayList<String> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.favlist_item, null);
			}
			final String l = items.get(position);
			if (l != null) {
				String[] item = l.split(",");
				TextView t = (TextView) v.findViewById(R.id.favlisttext);
				ImageView img = (ImageView) v.findViewById(R.id.favlistpic);

				t.setText(item[1]);

				if (item[0].equals(PERIMETER.getName())) {
					img.setImageResource(R.drawable.p);
					img.setContentDescription(PERIMETER.getName() + ","
							+ Integer.toString(PERIMETER.getXML()));
				} else if (item[0].equals(REVERSE.getName())) {
					img.setImageResource(R.drawable.r);
					img.setContentDescription(REVERSE.getName() + ","
							+ Integer.toString(REVERSE.getXML()));
				} else if (item[0].equals(CENTRAL.getName())) {
					img.setImageResource(R.drawable.c);
					img.setContentDescription(CENTRAL.getName() + ","
							+ Integer.toString(CENTRAL.getXML()));
				} else if (item[0].equals(HILL.getName())) {
					img.setImageResource(R.drawable.h);
					img.setContentDescription(HILL.getName() + ","
							+ Integer.toString(HILL.getXML()));
				}
			}
			return v;
		}
	}
}
