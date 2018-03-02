package uk.ac.city.final_project;

import android.os.AsyncTask;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by user on 02/03/2018.
 */

public class PreditionAsync extends AsyncTask<Void,Void,Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    private Connection connectionClasss(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitDiskReads().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "";
            connection = DriverManager.getConnection(connectionURL);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
