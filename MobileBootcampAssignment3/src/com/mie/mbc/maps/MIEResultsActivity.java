package com.mie.mbc.maps;

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

public class MIEResultsActivity extends ListActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.results_list);
        populateSearchResults();
        
        getListView().setOnItemClickListener(new MIEMapsIntent());

	}


	private void populateSearchResults() {
		ListView listView = (ListView) findViewById(android.R.id.list);

		MapsDataSource mapsDS = new MapsDataSource(this);
		String searchValue = getIntent().getExtras().getString("SearchValue");
        System.err.println(searchValue);
		
		CrossReference[] results = mapsDS.search(searchValue);
		
		ArrayAdapter<CrossReference> adapter = 
				new ArrayAdapter<CrossReference>(this, android.R.layout.simple_list_item_1, android.R.id.text1, results);
		listView.setAdapter(adapter);
		
		toggleEmptyView(R.id.no_results);
		
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

}
