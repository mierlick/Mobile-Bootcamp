package com.mie.mbc.maps;

import java.util.concurrent.Callable;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mie.mbc.maps.db.MapsDataSource;
import com.mie.mbc.maps.entities.CrossReference;

public class MIEResultsActivity extends ListActivity implements LongRunningActionCallback<CrossReference[]> {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.results_list);
        
        startLongRunningOperation();
        
        getListView().setOnItemClickListener(new MIEMapsIntent());

	}
	
	@Override
	public void onBackPressed() {
		setResult(Activity.RESULT_OK);
		finish();
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
		}
		return true;
	}
	
	private void exit() {
		System.exit(0);
	}
	
	private void toggleEmptyView(int newViewId){
		ListView list = (ListView) findViewById(android.R.id.list);
		View eView = list.getEmptyView();
		if (eView != null)
			eView.setVisibility(View.GONE);
		View newEView = findViewById(newViewId);
		list.setEmptyView(newEView);
	}
	
	
	
	private LongRunningActionDispatcher<CrossReference[]> dispatcher;

	private void startLongRunningOperation() {
		final MapsDataSource mapsDS = new MapsDataSource(this);
		
		// the first argument is a reference to the current Context, in this
		// case the current Activity. The second argument is a reference to
		// the object implementing the callback method.
		this.dispatcher = new LongRunningActionDispatcher<CrossReference[]>(this, this);
		dispatcher.startLongRunningAction(new Callable<CrossReference[]>() {
			public CrossReference[] call() throws Exception {
				// perform your actions that take a long time
				String searchValue = getIntent().getExtras().getString("SearchValue");
		        System.err.println(searchValue);
				
				return mapsDS.search(searchValue);
			}
		}, "Searching", "Searching for results.");
	}

	// the callback
	public void onLongRunningActionFinished(CrossReference[] result, Exception error) {
		if (error != null) {
			// handle error
			error.printStackTrace();
		} else {
			ListView listView = (ListView) findViewById(android.R.id.list);
			ArrayAdapter<CrossReference> adapter = 
					new ArrayAdapter<CrossReference>(this, android.R.layout.simple_list_item_1, android.R.id.text1, result);
			listView.setAdapter(adapter);
			
			
		}
		toggleEmptyView(R.id.no_results);
	}


}
