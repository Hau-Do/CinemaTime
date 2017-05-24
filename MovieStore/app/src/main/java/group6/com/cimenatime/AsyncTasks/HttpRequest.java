package group6.com.cimenatime.AsyncTasks;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by HauDT on 05/6/2017.
 */

public class HttpRequest {

    private static final String TAG = HttpRequest.class.getSimpleName();


    public HttpRequest(){

    }

    public String makeHttpRequest(String reqUrl){
        String response = null;

        try{

            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream in = new BufferedInputStream( conn.getInputStream() );
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }

        return response;
    }


    /**
     * convert Stream into String of link
     * @param is
     * @return
     */
    private String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader( new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try{

            while ( (line = reader.readLine()) != null ){
                sb.append(line).append('\n');
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
