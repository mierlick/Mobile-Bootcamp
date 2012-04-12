package com.mie.mbc.maps;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.mie.mbc.maps.entities.CrossReference;


public class MIEMapsActivity extends MapActivity {

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.maps);
	    
	    CrossReference reference = (CrossReference) getIntent().getExtras().getSerializable("location");
	    
	    //Allow for Zoom
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.flag);
	    MIEItemOverlay itemizedoverlay = new MIEItemOverlay(drawable, this, reference);
	    MapController controller = mapView.getController();
	    
	    int lat = (int) (reference.getZipCode().getLatitude() * 1e6);
	    int lon = (int) (reference.getZipCode().getLongitude() * 1e6);
	    
	    GeoPoint point = new GeoPoint(lat, lon);
	    OverlayItem overlayitem = new OverlayItem(point, reference.toString(), reference.toFormattedString());
	    
	    itemizedoverlay.addOverlay(overlayitem);
	    mapOverlays.add(itemizedoverlay);
	    
	    controller.animateTo(point);
	    controller.setZoom(12);
	    
	    
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
