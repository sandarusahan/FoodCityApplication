package com.foodcityapp.esa.foodcityapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sanda on 13/11/2017.
 */

public class DownloadURL {

    InputStream inputStream;
    public String readURL(String murl) throws IOException{
        String data = "";

        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(murl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            br.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
