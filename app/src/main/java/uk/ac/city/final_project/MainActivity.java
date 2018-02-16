package uk.ac.city.final_project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLng;

@SuppressWarnings("ALL")
public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient locationProvider;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Button locationButton;
    private LocationCallback locationCallback;
    private PlaceAutocompleteFragment autocompleteFragment;
    private LatLngBounds bounds;
    private MarkerOptions destination;
    private ClusterManager<BikePointMarker> bikePoints;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                addDestinationMarker(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.

            }
        });
        mapFragment.getMapAsync(this);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location :locationResult.getLocations()) {
                    movePoint();
                }
            }
        };
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        locationProvider = new FusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void movePoint(){
        locationProvider.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                } else {

                }
            }
        });
    }
    public void addDestinationMarker(LatLng latLng){
        mMap.clear();
        destination = new MarkerOptions().position(latLng);
        GoogleMap.OnMarkerClickListener onClickListener = new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        };
        mMap.addMarker(destination);

        mMap.setMinZoomPreference(10);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        LatLng trafalgar= new LatLng(51.508056,-0.128056);
        mMap.moveCamera(newLatLng(trafalgar));
        mMap.setMinZoomPreference(10);
        bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("UK").build();
        autocompleteFragment.setFilter(typeFilter);
        bikePoints = new ClusterManager<BikePointMarker>(this, mMap){
            @Override
            public boolean onMarkerClick(Marker marker){
                try {
                    GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                    gc.add(Calendar.SECOND,
                            (new DistanceMatrixAsync().execute(new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude()), marker.getPosition()).get()));
                    ArrayList<String> results = new BikeStatusAsync().execute(marker.getTitle()).get();
                    marker.setTitle(results.get(0));
                    marker.setSnippet("ETA " + gc.getTime()+"\n" +
                            "Available "+ results.get(1)+ " Free Spaces " + results.get(2));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return false;

            }
        };
        mMap.setOnCameraIdleListener(bikePoints);
        mMap.setOnMarkerClickListener(bikePoints);
        try {
            ArrayList<LatLng> results = new NetworkAsyncInitialize().execute(bikePoints).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }



    private void startLocationUpdates() {
        requestLocationUpdates();
    }

    private LatLng getCurrentLocation(){
        final LatLng[] currentLatLng = new LatLng[1];
        locationProvider.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLatLng[0] = new LatLng(location.getLatitude(), location.getLongitude());
                }
                else{
                    currentLatLng[0] = new LatLng(51.508056,-0.128056);
                }
            }
        });
        return currentLatLng[0];
    }
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        autocompleteFragment.setBoundsBias(new LatLngBounds(new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude()),new LatLng(51.508056,-0.128056)));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()){
            requestLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationProvider.removeLocationUpdates(new LocationCallback());
        googleApiClient.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
        movePoint();

    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        locationProvider.requestLocationUpdates(locationRequest,locationCallback,null );
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Context context = getApplicationContext();
        CharSequence text = "Connection Failed!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


}
