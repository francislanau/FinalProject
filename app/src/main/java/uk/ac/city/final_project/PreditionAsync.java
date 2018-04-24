package uk.ac.city.final_project;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * Created by user on 02/03/2018.
 */

public class PreditionAsync extends AsyncTask<ArrayList<String>,Void,ArrayList<Integer>>{
    ArrayList <String> list;

    @Override
    protected ArrayList<Integer> doInBackground(ArrayList... arrayLists) {
        list = arrayLists[0];
        ArrayList<Integer> returnInt = null;
        try {
            returnInt = connect();
            if (returnInt == null){
                returnInt.add(0);
                returnInt.add(0);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnInt;
    }

    private ArrayList<Integer> connect() throws IOException {
        ArrayList<Integer> returnInt = null;
        String link = "http://cycleapp.gwiddle.co.uk/index.php";
        URL url = new URL(link);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("station", list.get(1).substring(11,list.get(1).length())); // All parameters, also easy
        params.put("sec", list.get(0));
        StringBuilder postData = new StringBuilder();
        // POST as urlencoded is basically key-value pairs, as with GET
        // This creates key=value&key=value&... pairs
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0){
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        connection.setDoOutput(true);
        connection.getOutputStream().write(postDataBytes);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
        StringBuilder outPutSb = new StringBuilder();
        for (int i; (i = bufferedReader.read())>=0;){
            outPutSb.append(Character.toString((char) i));
        }
        if(outPutSb.length()!=0) {
            String[] stringSplit = outPutSb.toString().split(",");
            returnInt = new ArrayList<>();
            returnInt.add(Integer.valueOf(stringSplit[0]));
            returnInt.add(Integer.valueOf(stringSplit[1]));
        }
        return returnInt;
    }
}


