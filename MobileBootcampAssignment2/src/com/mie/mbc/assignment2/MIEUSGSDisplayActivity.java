package com.mie.mbc.assignment2;

import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.CursorAdapter;

import com.mie.mbc.assignment2.Adapters.ManagedAdapter;

public class MIEUSGSDisplayActivity extends ListActivity {

	private static final String FEED_URI = "http://earthquake.usgs.gov/earthquakes/shakemap/rss.xml";

	private CursorAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		loadAdapter(false);

		getListView().setOnItemClickListener(new UrlIntentListener());

		getListView().setOnCreateContextMenuListener(
				new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						MenuInflater inflater = getMenuInflater();
						inflater.inflate(R.menu.context, menu);
					}

				});
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete:
			if (adapter instanceof ManagedAdapter) {
				((ManagedAdapter) adapter).remove(info.position);
				loadAdapter(false);
				return (true);
			}

		}
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exit:
			exit();
			break;
		case R.id.refresh:
			loadAdapter(true);
			break;
		}
		return true;
	}

	private void loadAdapter(boolean reset) {
		adapter = Adapters.loadCursorAdapter(this, R.xml.quake_rss_feed,
				"content://xmldocument/?url=" + Uri.encode(FEED_URI)
						+ "&reset=" + reset);
		setListAdapter(adapter);
	}

	private void exit() {
		System.exit(0);
	}

	@Override
	public void onBackPressed() {
		// Do Nothing
	}
}