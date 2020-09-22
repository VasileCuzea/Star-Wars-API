package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SavedDataActivity extends AppCompatActivity implements View.OnClickListener{

    SwapiDatabase db = new SwapiDatabase(this);

    ListView savedList;

    Button deleteButton;
    Button mainPageButton;

    ArrayList<String> theList = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_data);

        savedList = findViewById(R.id.savedDataList);

        deleteButton = findViewById(R.id.deleteBtn);
        deleteButton.setOnClickListener(this);

        mainPageButton = findViewById(R.id.mainPageBtn);
        mainPageButton.setOnClickListener(this);

        Cursor data = db.getFavoriteList();

        if (data.getCount() == 0)
        {
            Toast.makeText(SavedDataActivity.this, "Lists are empty", Toast.LENGTH_LONG).show();
        }

        else
        {
            while(data.moveToNext())
            {
                theList.add(data.getString(1));

                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                savedList.setAdapter(listAdapter);
            }
        }
    }


    public void onClick(View v) {

        //button used to delete lists content
        if (v.getId() == R.id.deleteBtn)
        {
            SwapiDatabase dbDelete = new SwapiDatabase(this);
            dbDelete.deleteList(1);

            theList.clear();
            ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
            savedList.setAdapter(listAdapter);
            Toast.makeText(SavedDataActivity.this, "Lists deleted!", Toast.LENGTH_LONG).show();
        }

        //button used to return to Main Page
        else if ((v.getId() == R.id.mainPageBtn))
            {
                Intent mainPage = new Intent(this,MainActivity.class);
                startActivity(mainPage);
            }
    }
}
