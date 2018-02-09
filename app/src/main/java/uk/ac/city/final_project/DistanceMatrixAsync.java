package uk.ac.city.final_project;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by franc on 09/02/2018.
 */

public class DistanceMatrixAsync extends AsyncTask<LatLng,Void,Integer> {
    private LatLng origin;
    private LatLng destination;
    @Override
    protected Integer doInBackground(LatLng... latLngs) {
        origin= latLngs[0];
        destination = latLngs[1];
        Integer retInt = 0;
        try {
            retInt = getValuesFromJSON(getDistance());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retInt;
    }

    private String getDistance()  {
        String jsonResult = null;
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix" +
                    "/json?origins="+origin.latitude+","+ origin.longitude
                    +"&destinations="+destination.latitude+","+destination.longitude
                    +"&mode=walking&key=AIzaSyAD9fRYX1gHSdqDlni-CC_u_RU9f54lMqE");
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
            jsonResult = scanner.hasNext() ? scanner.next(): "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    private Integer getValuesFromJSON(String jsonResult) throws JSONException {
        Integer integer= null;
        try{
            JSONObject jsonObject = new JSONObject(jsonResult);
            JSONArray jsonArray = jsonObject.getJSONArray("rows");
            jsonObject = jsonArray.getJSONObject(0);
            jsonArray = jsonObject.getJSONArray("elements");
            jsonObject = jsonArray.getJSONObject(0);
            JSONObject result = jsonObject.getJSONObject("duration");
            integer = result.getInt("value");        }
        catch (Exception e){
            e.printStackTrace();
        }
        return integer;
    }
}
