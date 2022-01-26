package com.belicoffee.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.belicoffee.Model.Drink;
import com.belicoffee.Model.Food;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DATABASE";
    public static final String TABLE_DRINK = "DRINK";
    public static final String TABLE_FOOD = "FOOD";
    public static final String TABLE_CHAT = "CHAT";
    public static final String TABLE_USER = "USER";

    public static final String KEY_ID = "ID";
    public static final String KEY_NAME = "NAME";
    public static final String KEY_PRICE = "PRICE";
    public static final String KEY_IMAGE = "IMAGE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Singleton
    static DatabaseHelper databaseHelper;

    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null)
            databaseHelper = new DatabaseHelper(context);
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_DRINK = "CREATE TABLE " + TABLE_DRINK + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PRICE + " DOUBLE," + KEY_IMAGE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_DRINK);
        String CREATE_TABLE_FOOD = "CREATE TABLE " + TABLE_FOOD + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PRICE + " DOUBLE," + KEY_IMAGE + " BLOB" + ")";
        db.execSQL(CREATE_TABLE_FOOD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRINK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    //add objects to database
    public void insertDrinkDatabase(Drink drink) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, drink.getId());
        values.put(KEY_NAME, drink.getName());
        values.put(KEY_PRICE, drink.getPrice());
        values.put(KEY_IMAGE, drink.getImage());
        db.insert(TABLE_DRINK, null, values);
    }

    //get all objects
    public ArrayList<Drink> loadDrinkDatabase() {
        String query = "Select * FROM " + TABLE_DRINK;
        ArrayList<Drink> drinks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Drink drink = new Drink();
            drink.setId(cursor.getInt(0));
            drink.setName(cursor.getString(1));
            drink.setPrice(cursor.getDouble(2));
            drink.setImage(cursor.getString(3));
            drinks.add(drink);
        }
        cursor.close();
        return drinks;
    }

    //delete all database
    public void deleteDrinkDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRINK, null, null);
    }

    public void insertFoodDatabase(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, food.getFoodID());
        values.put(KEY_NAME, food.getFoodName());
        values.put(KEY_PRICE, food.getFoodPrice());
        values.put(KEY_IMAGE, food.getFoodImage());
        db.insert(TABLE_FOOD, null, values);
    }

    //get all objects
    public ArrayList<Food> loadFoodDatabase() {
        String query = "Select * FROM " + TABLE_FOOD;
        ArrayList<Food> foods = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Food food = new Food();
            food.setFoodID(cursor.getInt(0));
            food.setFoodName(cursor.getString(1));
            food.setFoodPrice(cursor.getDouble(2));
            food.setFoodImage(cursor.getString(3));
            foods.add(food);
        }
        cursor.close();
        return foods;
    }

    //delete all database
    public void deleteFoodDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOOD, null, null);
    }
}
