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

import net.jessechen.berkeleycampusshuttle.myfavorites.MyFavorites;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * ShuttleMain is the first activity the user sees when they start the app.
 * Currently, it has three buttons, one for all the stops, one for all the
 * routes, and one for favorite stops that the user has saved.
 * 
 * @author Jesse Chen
 * 
 */
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
