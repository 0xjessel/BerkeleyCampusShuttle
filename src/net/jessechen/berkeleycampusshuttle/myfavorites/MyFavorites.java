package net.jessechen.berkeleycampusshuttle.myfavorites;

import java.io.IOException;
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
import android.widget.Toast;

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
		favorites = FileHandler.readFileWrapper(this);

		lvAdapter = new mAdapter(this, R.layout.favlist_item,
				new ArrayList<String>(Arrays.asList(favorites)));
		if (lvAdapter.getCount() == 0) {
			setOnEmpty();
		}
		lv.setAdapter(lvAdapter);
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
		try {
			FileHandler.deleteLine(getApplicationContext(), stop);
			lvAdapter.remove(stop);
			lvAdapter.notifyDataSetChanged();
			if (lvAdapter.getCount() == 0) {
				setOnEmpty();
			}
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Delete failed",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void setOnEmpty() { // show empty favorites dialog
		TextView empty = (TextView) findViewById(R.id.emptyfavlist);
		empty.setVisibility(View.VISIBLE);
		lv.setEmptyView(empty);
	}

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
