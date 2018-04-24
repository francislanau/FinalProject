package uk.ac.city.final_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by franc on 07/03/2018.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View view;
    private Context context;

    public CustomInfoWindowAdapter(Context context){
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.cusom_info_window,null);
    }

    private void renderWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView titleTextView = view.findViewById(R.id.title);
        if(!title.equals("")){
            titleTextView.setText(title);
        }

        String snippet = marker.getSnippet();
        TextView snippetTextView = view.findViewById(R.id.snippet);
        if(!snippet.equals("")){
            snippetTextView.setText(snippet);
        }
    }
    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, view);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, view);
        return view;
    }
}
