package uk.ac.city.final_project;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

/**
 * Created by franc on 08/02/2018.
 */

public class BikePointMarker implements ClusterItem  {
    private final LatLng mPosition;
    private final String title;
    private String snippet;
    public BikePointMarker(LatLng latLng, String name){
        mPosition = latLng;
        this.title = name;
        snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
