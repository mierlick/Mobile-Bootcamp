package com.matt.bootcamp.assignment1;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.matt.bootcamp.assignment1.sqlite.RunDateTime;
import com.matt.bootcamp.assignment1.sqlite.RunDateTimeDataSource;
import com.matt.bootcamp.assignment1.transistions.FlipView;

public class HelloWorldWithStats extends Activity {
	
	private RunDateTimeDataSource datasource;
	private ViewFlipper flipper;
	
	
    /** 
     * Called when the activity is first created.
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Date currentDate = Calendar.getInstance().getTime();
        System.out.println(currentDate);
        datasource = new RunDateTimeDataSource(this);
		datasource.open();
		
		datasource.createRunDateTime(currentDate);
		
		flipper = (ViewFlipper) findViewById(R.id.flipper);
    }
    
    
    /**
     * On Click activity to show the run stats screen
     * @param view
     */
    public void showStats (View view) {
    	flipper.setInAnimation(FlipView.inFromRightAnimation());
        flipper.setOutAnimation(FlipView.outToLeftAnimation());
        flipper.showNext();
    	populateStats();
    }
    
    
    @Override
    public void onBackPressed() {
    	View currentView = flipper.getCurrentView();
    	String view = (String) currentView.getContentDescription();
    	if (view.equals("Stats Page")) {
    		flipper.setInAnimation(FlipView.inFromLeftAnimation());
            flipper.setOutAnimation(FlipView.outToRightAnimation());
            flipper.showNext();
    	}
    	
    }
    
    
    /**
     * Populate the stats to display
     */
    private void populateStats() {
    	ListView listView = (ListView) findViewById(R.id.statsListView);

		ArrayAdapter<RunDateTime> adapter = getArrayAdapter();

		// Assign adapter to ListView
		listView.setAdapter(adapter);
    }
    
    /**
     * 
     * @return ArrayAdapter contains run dates and times
     */
    private ArrayAdapter<RunDateTime> getArrayAdapter () {
    	List<RunDateTime> runDateTimes = datasource.getAllRunDateTimes();
    	RunDateTime runDateTimeArray[] = new RunDateTime[runDateTimes.size()];
    	for (int i = 0; i < runDateTimes.size(); i++) {
    		runDateTimeArray[i] = runDateTimes.get(i);
		}
    	
    	ArrayAdapter<RunDateTime> adapter = new ArrayAdapter<RunDateTime>(this, android.R.layout.simple_list_item_1, android.R.id.text1, runDateTimeArray);
    	return adapter;
    }
    
    
    
    

    
    //Exit, Pause, Resume and Destroy Methods

	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
    	datasource.close();
		super.onDestroy();
	}
	
	/**
	 * Exit the application gracefully
	 * On click activity for the exit button
	 * @param view
	 */
	public void exit (View view) {
    	System.exit(0);
    }
    
}