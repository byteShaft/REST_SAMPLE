package com.byteshaft.sample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private String URL = "http://139.59.228.194:8000/api/accounts/me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new InternetTask().execute();
    }

    private class InternetTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                HttpURLConnection connection = openConnectionForUrl(URL);
                JSONObject output = readResponse(connection);
                System.out.println(output.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private HttpURLConnection openConnectionForUrl(String path)
            throws IOException {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty(
                "Authorization", "Token 9b31dc84abc2b76074bae66531ab9c98f2be8ca0");
        return connection;
    }

    private void sendRequestData(HttpURLConnection connection, String body)
            throws IOException {
        byte[] outputInBytes = body.getBytes("UTF-8");
        OutputStream os = connection.getOutputStream();
        os.write(outputInBytes);
        os.close();
    }

    private JSONObject readResponse(HttpURLConnection connection)
            throws IOException, JSONException {
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        return new JSONObject(response.toString());
    }
}
