package com.example.joem.jsonparsing;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonJson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConnected()){//checks if you're connected, then executes GetDataAsync on json api url
                    Toast.makeText(MainActivity.this, "Internet present", Toast.LENGTH_SHORT).show();
                    new GetDataAsync().execute("http://api.theappsdr.com/json/");
                }else{
                    Toast.makeText(MainActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<Person>> {//sends back ArrayList of Person
        @Override
        protected ArrayList<Person> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<Person> result = new ArrayList<>(); //ArrayList will hold Persons that we've parsed
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String stringName = IOUtils.toString(connection.getInputStream(), "UTF-8");//changed stream to string?

                    //represents root object
                    JSONObject root = new JSONObject(stringName);
                    //stringName is name of parameter
                    //represents next portion of JSON inquiry, in this case an array
                    JSONArray persons = root.getJSONArray("persons");
                    //loop over persons json array
                    for (int i=0; i<persons.length(); i++){
                        JSONObject personJson = persons.getJSONObject(i);//retrieve user json object at i
                        Person person = new Person();
                        person.name = personJson.getString("name");
                        person.id = personJson.getLong("id");
                        person.age = personJson.getInt("age");

                        JSONObject addressJson = personJson.getJSONObject("address");
                        Address address = new Address();
                        address.line1 = addressJson.getString("line1");
                        address.city = addressJson.getString("city");
                        address.state = addressJson.getString("state");
                        address.zip = addressJson.getString("zip");

                        person.address = address;
                        result.add(person); //add person user here
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }
        @Override
        protected void onPostExecute(ArrayList<Person> result) {
            if (result.size() > 0) {
                Log.d("demo", result.toString());
            } else {
                Log.d("demo", "empty result");
            }
        }
    }
}
