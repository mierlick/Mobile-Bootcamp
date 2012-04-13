package com.mie.mbc.maps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mie.mbc.maps.db.MapsDataSource;

public class MIEMBC3Activity extends Activity {
	
	private MapsDataSource mapsDataSource;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapsDataSource = new MapsDataSource(this);
        mapsDataSource.open();
    }
    
    
    @Override
	protected void onDestroy() {
		super.onDestroy();

		if (mapsDataSource != null) {
			mapsDataSource.close();
			mapsDataSource = null;
		}
	}

    public void search (View view) {
    	EditText searchEditText = (EditText) findViewById(R.id.searchInput);
    	String searchInput = searchEditText.getText().toString();
    	
    	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    	
    	Intent intent = new Intent(getBaseContext(), MIEResultsActivity.class);
    	intent.putExtra("SearchValue", searchInput);
		startActivityForResult(intent,0);
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
    

}