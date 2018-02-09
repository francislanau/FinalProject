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
    public BikePointMarker(LatLng latLng){
        mPosition = latLng;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }


}
