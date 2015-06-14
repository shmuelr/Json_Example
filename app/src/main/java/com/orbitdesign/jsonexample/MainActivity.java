package com.orbitdesign.jsonexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.orbitdesign.jsonexample.adapters.DefinitionAdapter;
import com.orbitdesign.jsonexample.models.Definition;
import com.orbitdesign.jsonexample.models.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String JSON_EXAMPLE = " {\"firstName\":\"John\",\"lastName\":\"Smith\",\"age\":15}";

    private RecyclerView recyclerView;
    private DefinitionAdapter definitionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecyclerView();

        // Use this to simply load the JSON from the saved text file.
        //loadListFromFile();

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
            JSONArray myArray = myJsonObject.getJSONArray("definitions");
            JSONObject item;
            for (int i = 0 ; i <myArray.length(); i++){


                item = myArray.getJSONObject(i);


                definitionList.add(new Definition(item.getString("text"), item.getString("attribution")) );
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
}
