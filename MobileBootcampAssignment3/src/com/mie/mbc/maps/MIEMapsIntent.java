package com.mie.mbc.maps;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.mie.mbc.maps.entities.CrossReference;

public class MIEMapsIntent implements OnItemClickListener {
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Context context = parent.getContext();
        Intent intent = new Intent(context, MIEMapsActivity.class);
        CrossReference reference = (CrossReference) parent.getAdapter().getItem(position);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("location", reference);
        context.startActivity(intent);
    }

}
