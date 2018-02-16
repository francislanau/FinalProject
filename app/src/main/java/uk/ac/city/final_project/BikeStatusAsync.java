package uk.ac.city.final_project;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by user on 11/02/2018.
 */

public class BikeStatusAsync extends AsyncTask<String,Void,ArrayList<String>>{
    private String bikepointid;
    ArrayList<String> bikePointStatus = new ArrayList<>();
    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        bikepointid = strings[0];
        try {
            getValuesFromJSON(getDistance());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bikePointStatus;
    }

    private String getDistance()  {
        String jsonResult = null;
        try {
            URL url = new URL("https://api.tfl.gov.uk/BikePoint/"+bikepointid+"?" +
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

    private void getValuesFromJSON(String jsonResult) throws JSONException {
        Integer integer= null;
        try{
            JSONObject jsonObjectBikeNo = new JSONObject(jsonResult);
            bikePointStatus.add(jsonObjectBikeNo.getString("commonName"));
            JSONArray jsonArray = jsonObjectBikeNo.getJSONArray("additionalProperties");
            jsonObjectBikeNo = jsonArray.getJSONObject(6);
            String bikeNo = jsonObjectBikeNo.getString("value");
            JSONObject jsonObjectEmptyDock = jsonArray.getJSONObject(7);
            String  emptyDockNo= jsonObjectEmptyDock.getString("value");
            bikePointStatus.add(bikeNo);
            bikePointStatus.add(emptyDockNo);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
