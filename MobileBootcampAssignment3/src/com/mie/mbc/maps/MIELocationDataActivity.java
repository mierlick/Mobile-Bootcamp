package com.mie.mbc.maps;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.mie.mbc.maps.entities.CrossReference;

public class MIELocationDataActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_data);
        
        TextView locationDataTextView = (TextView) findViewById(R.id.locationDataTextView);
        TextView locationInfoTextView = (TextView) findViewById(R.id.locationInfoTextView);
        
        CrossReference reference = (CrossReference) getIntent().getExtras().getSerializable("location");
        
        locationDataTextView.setText(reference.toString());
        locationInfoTextView.setText(reference.toFormattedString());
        
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

}
