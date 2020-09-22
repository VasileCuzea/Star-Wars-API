package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog pDialog;
     String requestUrl = "https://swapi.py4e.com/api/people/";

     SwapiDatabase db = new SwapiDatabase(this);

     Button searchDbButton;
     Button addListButton;
     Button clearListButton;
     Button yourSavedLists;

     AutoCompleteTextView searchText;
     ListView resultRequest;
     RequestQueue requestQueue;

     ArrayList<String> swapiList = new ArrayList<>();
     ArrayList<String> singleValueList = new ArrayList<>();

     String name, height, mass, hairColor, skinColor, birthYear, gender, eyeColor;
     String searchPeople;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pDialog = new ProgressDialog(this);

        addListButton =  findViewById(R.id.addListBtn);
        addListButton.setOnClickListener(this);

        clearListButton = findViewById(R.id.clearBtn);
        clearListButton.setOnClickListener(this);

        searchDbButton = findViewById(R.id.searchDbBtn);
        searchDbButton.setOnClickListener(this);

        yourSavedLists = findViewById(R.id.savedLists);
        yourSavedLists.setOnClickListener(this);

        resultRequest = findViewById(R.id.results);
        searchText = findViewById(R.id.searchTxt);

        requestQueue = Volley.newRequestQueue(this);

        //creating predictive text in the search field
        String[] swapiPeople = new String[] {
                "Luke Skywalker", "C-3PO", "R2-D2", "Darth Vader", "Leia Organa", "Owen Lars", "Beru Whitesun lars",
                "R5-D4", "Biggs Darklighter", "Obi-Wan Kenobi", ""
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, swapiPeople);

        searchText.setAdapter(adapter);
    }


            //button used to search for the API / creating a JSONObject request
            public void Search (View view) {
                pDialog.setMessage("Searching...");
                pDialog.show();
                final ListAdapter searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, swapiList);

                JsonObjectRequest jasonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    JSONArray jsonArray = response.getJSONArray("results");

                                    for (int i = 0; i < jsonArray.length(); i++){
                                        JSONObject results = jsonArray.getJSONObject(i);

                                        name = results.getString("name");
                                        height = results.getString("height");
                                        mass = results.getString("mass");
                                        hairColor = results.getString("hair_color");
                                        skinColor = results.getString("skin_color");
                                        eyeColor = results.getString("eye_color");
                                        birthYear = results.getString("birth_year");
                                        gender = results.getString("gender");

                                        //with the following line of code, the database can be updated (the database already contains the API)
                                        //db.addName(name, height, mass, hairColor, skinColor, eyeColor, birthYear, gender);

                                        swapiList.add("Name: " + name);
                                        swapiList.add("Height: " + height);
                                        swapiList.add("Mass: " + mass);
                                        swapiList.add("Hair color: " + hairColor);
                                        swapiList.add("Skin color: " + skinColor);
                                        swapiList.add("Eye color: " + eyeColor);
                                        swapiList.add("Birth year: " + birthYear);
                                        swapiList.add("Gender: " + gender + "\n" + "\n");
                                        resultRequest.setAdapter(searchAdapter);
                                    }
                                    pDialog.hide();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.hide();
                            }
                        });
                requestQueue.add(jasonObjectRequest);
            }


    protected void onPause() {
        super.onPause();
        pDialog.dismiss();
    }


    //button used to search the database for a specific movie character
    public void onClick(View v) {

        String dbResults = singleValueList.toString();

        if (v.getId() == R.id.searchDbBtn)
       {
           searchPeople = searchText.getText().toString().trim();

           SQLiteDatabase databaseQuery = db.getReadableDatabase();
           Cursor data = databaseQuery.rawQuery("SELECT * FROM SWAPI WHERE NAME LIKE '" + searchPeople + "'", null);

           if (data.getCount() == 0)
           {
               Toast.makeText(MainActivity.this, "No record found!", Toast.LENGTH_LONG).show();
           }

           else
           {
               while(data.moveToNext())
               {
                   singleValueList.add("Name: " + data.getString(1));
                   singleValueList.add("Height: " +data.getString(2));
                   singleValueList.add("Mass: " +data.getString(3));
                   singleValueList.add("Hair color: " +data.getString(4));
                   singleValueList.add("Skin color: " +data.getString(5));
                   singleValueList.add("Eye color: " +data.getString(6));
                   singleValueList.add("Birth year: " +data.getString(7));
                   singleValueList.add("Gender: " +data.getString(8));
                   singleValueList.add( "\n" + "\n");

                   ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, singleValueList);
                   resultRequest.setAdapter(listAdapter);
               }
           }
       }


        //button used to add current list to favorite list
        else if (v.getId() == R.id.addListBtn)
        {
            if (singleValueList.isEmpty())
            {
                Toast.makeText(this, "Please search the database to add items to your list!", Toast.LENGTH_LONG).show();
            }

            else {
                Intent save = new Intent(this, FavoriteListPage.class);
                save.putExtra("sent_message", dbResults);
                startActivity(save);
            }
        }


        //button used to see saved list
        else if (v.getId() == R.id.savedLists)
        {
            Intent yourLists = new Intent(this,SavedDataActivity.class);
            startActivity(yourLists);
        }


        //button used to clear the list
        else if (v.getId() == R.id.clearBtn)
        {
            swapiList.clear();
            ListAdapter searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, swapiList);
            resultRequest.setAdapter(searchAdapter);

            singleValueList.clear();
            ListAdapter searchDbAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, singleValueList);
            resultRequest.setAdapter(searchDbAdapter);
        }
    }
}
