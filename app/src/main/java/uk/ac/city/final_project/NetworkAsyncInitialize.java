package uk.ac.city.final_project;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by franc on 08/02/2018.
 */

public class NetworkAsyncInitialize extends AsyncTask<ClusterManager<BikePointMarker>, Void, ArrayList<LatLng>>{
    private ClusterManager<BikePointMarker> bikePointMarkerClusterManager;
    private ArrayList<String> markerName = new ArrayList<>();
    @Override
    protected ArrayList<LatLng> doInBackground(ClusterManager<BikePointMarker>... bikePointMarkers) {
        bikePointMarkerClusterManager = bikePointMarkers[0];
        return getValuesFromJSON(bikePointSearch());
    }

    private String bikePointSearch(){
        String jsonResult = null;
        try{
            URL url = new URL("https://api.tfl.gov.uk/BikePoint?" +
                    "app_id=d83ad9f0&app_key=4c68c30ed18697c205392b12be12e099");
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
            jsonResult = scanner.hasNext() ? scanner.next(): "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    private ArrayList<LatLng> getValuesFromJSON(String jsonResult){
        ArrayList<LatLng> latLongList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject("{\"results\" :"+ jsonResult+"}");
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject result = jsonArray.getJSONObject(i);
                latLongList.add(new LatLng(result.getDouble("lat"), result.getDouble("lon")));
                markerName.add(result.getString("id"));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return latLongList;
    }

    @Override
    protected void onPostExecute(ArrayList<LatLng> latLngs){
        for(int i = 0; i<latLngs.size(); i++){
            bikePointMarkerClusterManager.addItem(new BikePointMarker(latLngs.get(i),markerName.get(i)));
        }
    }
}
