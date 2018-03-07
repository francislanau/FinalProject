package uk.ac.city.final_project;

import android.os.AsyncTask;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by user on 02/03/2018.
 */

public class PreditionAsync extends AsyncTask<ArrayList,Void,ArrayList<Integer>> {
    ArrayList list;
    @Override
    protected ArrayList doInBackground(ArrayList...arrayLists) {
        ArrayList list = arrayLists[0];
        connectionClass();
        return null;
    }

    private Connection connectionClass(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitDiskReads().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://cycledata.c65vxnzgxgck.eu-west-2.rds.amazonaws.com:1433;databaseName=Data;integratedSecurity=true; ";
            connection = DriverManager.getConnection(connectionURL);
            connection.setReadOnly(true);
            String q = "Select Count(*) from Data where StartStation Id = " + list.get(0) + "and (Start Date <="
                    +list.get(1)+ " and Start Date > GETDATE()" ;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(q);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
