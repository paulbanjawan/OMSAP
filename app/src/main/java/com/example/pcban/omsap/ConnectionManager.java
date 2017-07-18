package com.example.pcban.omsap;

/**
 * Created by pcban on 24 May 2017.
 */


import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by customized on 5/15/2017.
 */

public class ConnectionManager {
    public ConnectionManager() {
        super();
    }

    public boolean CheckUrlConnection(String url)
    {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");

            con.setConnectTimeout(30000); //set timeout to 5 seconds

            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (java.net.SocketTimeoutException e) {
            return false;
        } catch (java.io.IOException e) {
            return false;
        }
        /*try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            if(connection.getSt)
            return true;
        } catch (Exception e) {
            Log.d(e.getMessage(), "");
            return false;
        }*/
    }
}
