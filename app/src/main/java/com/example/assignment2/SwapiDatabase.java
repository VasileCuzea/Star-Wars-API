package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SwapiDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "swapiDb";
    public static final int DATABASE_VERSION = 1;

    public SwapiDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {


        //method used to create the table and its columns
        String sql = "CREATE TABLE SWAPI ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "HEIGHT TEXT, "
                + "MASS TEXT, "
                + "HAIRCOLOR TEXT, "
                + "SKINCOLOR TEXT, "
                + "EYECOLOR TEXT, "
                + "BIRTHYEAR TEXT, "
                + "GENDER TEXT);";
        db.execSQL(sql);


        //method used to create the "Favorite List table"
        String favoriteList = "CREATE TABLE FAVORITE_LIST_TABLE ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT);";
        db.execSQL(favoriteList);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    //method used in Main Activity to add data to the database
    public boolean addName(String name, String height, String mass, String hairColor, String skinColor, String eyeColor,
                           String birthYear, String gender)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues swapiValues = new ContentValues();

        swapiValues.put("Name", name);
        swapiValues.put("Height", height);
        swapiValues.put("Mass", mass);
        swapiValues.put("HairColor", hairColor);
        swapiValues.put("SkinColor", skinColor);
        swapiValues.put("EyeColor", eyeColor);
        swapiValues.put("BirthYear", birthYear);
        swapiValues.put("Gender", gender);

        return db.insert("SWAPI", null, swapiValues)  != -1;
    }


    //method used to add Favorite list to the database
    public boolean addFavoriteList(String allPeople)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues swapiValues = new ContentValues();

        swapiValues.put("NAME", allPeople);
        return db.insert("FAVORITE_LIST_TABLE", null, swapiValues)  != -1;
    }


    //method used to retrieve favorite list
    public Cursor getFavoriteList()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM FAVORITE_LIST_TABLE", null);
        return data;
    }


    //method used to delete data from list
    public void deleteList(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM  FAVORITE_LIST_TABLE");
    }
}
