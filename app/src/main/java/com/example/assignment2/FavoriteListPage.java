package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoriteListPage extends AppCompatActivity {

    String searchResults;
    TextView list;

    Button saveListButton;
    Button seeListButton;
    Button sendListButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list_page);

        saveListButton = findViewById(R.id.saveListBtn);
        seeListButton = findViewById(R.id.seeListBtn);
        sendListButton = findViewById(R.id.sendListBtn);

        list = findViewById(R.id.receiveFavoriteList);
        Intent result = getIntent();
        searchResults = result.getStringExtra("sent_message");
        list.append(searchResults);
    }


    //method used to save the favorite list in the database
    public void SaveList (View view)
    {
        SwapiDatabase dbHelper = new SwapiDatabase(this);
        boolean addFavoriteList = dbHelper.addFavoriteList(searchResults);

         if(addFavoriteList)
        {
            Toast.makeText(this, "Data added!", Toast.LENGTH_LONG).show();
            list.setText(null);
        }

          if (searchResults.isEmpty())
         {
             Toast.makeText(this, "Data NOT added!", Toast.LENGTH_LONG).show();
         }
    }


    //method used to see favorite list in the database
    public void SeeList (View view)
    {
        Intent intent = new Intent(this, SavedDataActivity.class);
        startActivity(intent);
        list.setText(null);
    }


    //method used to send favorite list through email or message
    public void SendList(View view)
    {
        Intent ind = new Intent(Intent.ACTION_SEND);
        String send = list.toString();
        ind.setType("text/plain");
        ind.putExtra(Intent.EXTRA_TEXT, send);
        startActivity(ind);
    }
}
