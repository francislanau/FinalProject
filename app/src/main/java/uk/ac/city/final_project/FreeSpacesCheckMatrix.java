package uk.ac.city.final_project;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by franc on 10/02/2018.
 */

public class FreeSpacesCheckMatrix extends AsyncTask<Void,Void,Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    private String getBikePointSpaces()  {
        String jsonResult = null;
        try {
            URL url = new URL(" ");
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
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
