package uk.ac.city.final_project;

import android.os.AsyncTask;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            connectionURL = "jdbc:jtds:sqlserver://cycledata.c65vxnzgxgck.eu-west-2.rds.amazonaws.com,1433:database=CycleData;user=Francis;password=Felipeformula1";
            connection = DriverManager.getConnection(connectionURL);
            connection.setReadOnly(true);
            String q = "";
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
