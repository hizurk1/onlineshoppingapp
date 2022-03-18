package com.android.onlineshoppingapp;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection conn;
    String username, password, ip, port, database;

    public Connection connectionClass() {
        ip = "";
        database = "";
        username = "";
        password = "";
        port = "";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionString = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionString = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + database + ";user=" + username + ";password=" + password + ";";
            connection = DriverManager.getConnection(connectionString);
        } catch (Exception err) {
            Log.e("Error", err.getMessage());
        }
        return connection;
    }
}
