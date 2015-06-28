package com.orbitdesign.jsonexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbitdesign.jsonexample.adapters.DefinitionAdapter;
import com.orbitdesign.jsonexample.models.Definition;
import com.orbitdesign.jsonexample.models.ServerResponse;
import com.orbitdesign.jsonexample.models.Student;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String JSON_EXAMPLE = " {\"firstName\":\"John\",\"lastName\":\"Smith\",\"age\":15}";

    private EditText editText;
    private Button button;
    private ProgressBar progressBar;

    private RecyclerView recyclerView;
    private DefinitionAdapter definitionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpGui();


        // Use this to simply load the JSON from the saved text file.
        //loadListFromFile();

    }

    private void setUpGui() {
        setUpRecyclerView();
        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

    }

    public void onClickButton(View view) {

        new AsyncDefinitionsDownloader(new AsyncDefinitionsDownloader.LoadingCallbacks() {
            @Override
            public void onLoad() {
                setLoading(true);
            }

            @Override
            public void onFinishLoading(List<Definition> definitionList) {
                setLoading(false);

                if(!definitionList.isEmpty()){
                    updateRecyclerAdapter(definitionList);
                }else {
                    Toast.makeText(MainActivity.this, "No definition found", Toast.LENGTH_SHORT).show();
                }



            }
        }).execute(editText.getText().toString());

    }

    public void setLoading(boolean isLoading){
        if(isLoading){
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setUpRecyclerView() {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        definitionAdapter = new DefinitionAdapter();

        recyclerView.setAdapter(definitionAdapter);
    }

    private void updateRecyclerAdapter(List<Definition> definitionList){
        definitionAdapter.swapDefinitions(definitionList);
    }

    private void loadListFromFile() {
        JSONObject myJsonObject = testJsonFromFile();

        List<Definition> definitionList = new ArrayList<>();

        try {
            if(myJsonObject.getInt("reponseCode") != 200){
                return;
            }
            JSONArray myArray = myJsonObject.getJSONArray("definitions");
            JSONObject item;
            for (int i = 0 ; i < myArray.length(); i++){

                item = myArray.getJSONObject(i);

                Definition definition = new Definition();
                definition.setText(item.getString("text"));
                definition.setAttribution(item.getString("attribution"));

                definitionList.add(definition);
            }

            for (Definition definition :definitionList){
                Log.d(TAG, definition.toString());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateRecyclerAdapter(definitionList);
    }


    private JSONObject testJsonFromFile(){



        BufferedReader reader = null;
        JSONObject myObject = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getResources().getAssets().open("json_text.txt")));



            String mLine = reader.readLine();
            StringBuilder stringBuilder = new StringBuilder();

            while (mLine != null) {

//                Log.d(TAG, mLine);


                stringBuilder.append(mLine);
                mLine = reader.readLine();
            }

            myObject = new JSONObject(stringBuilder.toString());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return myObject;
    }


    private void testJsonStudent(){
        Student myStudent = new Student();

        myStudent.setFirstName("John");
        myStudent.setLastName("Smith");
        myStudent.setAge(15);


        //JsonWriter myJsonWriter = new JsonWriter();

        try {
            JSONObject myJsonObject = new JSONObject(JSON_EXAMPLE);
            //myJsonObject.put("firstName", "John");
            //myJsonObject.put("lastName", "Smith");
            //myJsonObject.put("age", 15);

            Log.d(TAG, "First Name is "+myJsonObject.optString("firstMyName", "default name"));

            Log.d(TAG, "First Name is "+myJsonObject.getString("firstName"));

            Log.d(TAG, myJsonObject.toString());

        } catch (JSONException e) {
            Log.e(TAG, "Oops: " + e.getMessage());
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    protected static class AsyncDefinitionsDownloader extends AsyncTask<String, Void, List<Definition>>{

        private LoadingCallbacks myLoadingCallbacks;

        public AsyncDefinitionsDownloader(LoadingCallbacks callback){
            myLoadingCallbacks = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Set loading to true
            myLoadingCallbacks.onLoad();
        }

        @Override
        protected List<Definition> doInBackground(String... params) {
            //List<Definition> list = new ArrayList<>();
            ServerResponse response = null;
            try {
                URL url = new URL("https://montanaflynn-dictionary.p.mashape.com/define?word="+params[0]);

                /*HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestProperty("X-Mashape-Key", "lrt4vMehhtmshQgInD3agn7RsZQjp1LgvV5jsnOQ26eRmkUXkj");
                        connection.setRequestProperty("Accept", "application/json");
*/
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("X-Mashape-Key", "lrt4vMehhtmshQgInD3agn7RsZQjp1LgvV5jsnOQ26eRmkUXkj")
                        .addHeader("Accept", "application/json")
                        .build();
                Response okhttpResponse =
                client.newCall(request).execute();

                Log.d(TAG, "Ready to request data");

                ObjectMapper objectMapper = new ObjectMapper();
                response = objectMapper.readValue(okhttpResponse.body().byteStream(), ServerResponse.class);

                Log.d(TAG, "String from objectMapper = " + objectMapper.writeValueAsString(response));


                /*String apiString = readStream(connection.getInputStream());
                //Log.d(TAG, "API returned "+apiString);


                JSONObject myJsonObject = new JSONObject(apiString);


                JSONArray myArray = myJsonObject.getJSONArray("definitions");
                JSONObject item;
                for (int i = 0 ; i <myArray.length(); i++){


                    item = myArray.getJSONObject(i);


                    list.add(new Definition(item.getString("text"), item.getString("attribution")) );
                }*/

            } catch (Exception e) {
                Log.e(TAG, "Error: "+e.getMessage());
            }


            return response.getDefinitions();
        }


        private String readStream(InputStream in) {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return stringBuilder.toString();
        }
            @Override
        protected void onPostExecute(List<Definition> definitionList) {
            super.onPostExecute(definitionList);

            // Update the adapter
            // Set loading to false
            myLoadingCallbacks.onFinishLoading(definitionList);
        }


        public interface LoadingCallbacks{
            public void onLoad();
            public void onFinishLoading(List<Definition> definitionList);
        }

    }


}
