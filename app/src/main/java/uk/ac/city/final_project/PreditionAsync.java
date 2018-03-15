package uk.ac.city.final_project;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by user on 02/03/2018.
 */

public class PreditionAsync extends AsyncTask<ArrayList<String>,Void,Integer> {
    ArrayList <String> list;

    @Override
    protected Integer doInBackground(ArrayList... arrayLists) {
        list = arrayLists[0];
        Integer returnInt = 0;
        try {
            returnInt = connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnInt;
    }

    private Integer connect() throws IOException {

        Integer returnInt = 0;
        String link = "https://cycleapp.000webhostapp.com";
        URL url = new URL(link);
        String data = URLEncoder.encode("station", "UTF-8") +
                "=" + URLEncoder.encode(list.get(0).substring(11, list.get(0).toString().length()), "UTF-8")
                + " " + URLEncoder.encode("endDate" , "UTF-8") + "=" + URLEncoder.encode(list.get(1).substring(0,10), "UTF-8")
                + " " + URLEncoder.encode("endHour","UTF-8") + "=" + URLEncoder.encode(list.get(1).substring(11,13),"UTF-8");
                //+ " " + URLEncoder.encode("endMinute","UTF-8") + "=" + URLEncoder.encode(list.get(1).substring(14,16),"UTF-8");
        URLConnection connection = url.openConnection();

        connection.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(data);
        writer.flush();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        while ((line = bufferedReader.readLine())!=null){
            stringBuilder.append(line);
            break;
        }
        try {
            returnInt = new Integer(stringBuilder.toString());
        }catch (Exception e){

        }

        return returnInt;
    }
}


