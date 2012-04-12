package com.mie.mbc.maps;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.mie.mbc.maps.entities.CrossReference;


public class MIEItemOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private CrossReference reference;

	public MIEItemOverlay(Drawable defaultMarker) {
		super(defaultMarker);
	}
	
	public MIEItemOverlay(Drawable defaultMarker, Context context, CrossReference reference) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		  this.reference = reference;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected boolean onTap(int index) {
//	  OverlayItem item = mOverlays.get(index);
//	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//	  dialog.setTitle(item.getTitle());
//	  dialog.setMessage(item.getSnippet());
//	  dialog.show();
	  
	  Intent intent = new Intent(mContext, MIELocationDataActivity.class);
	  intent.putExtra("location", reference);
	  mContext.startActivity(intent);
	  
	  return true;
	}

}
