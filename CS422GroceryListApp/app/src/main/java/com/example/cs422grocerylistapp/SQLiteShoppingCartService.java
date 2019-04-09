package com.example.cs422grocerylistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteShoppingCartService extends SQLiteOpenHelper {


    public SQLiteShoppingCartService(Context context) {
        super(context, "ShoppingCart.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS unbought_items" +
                "(item_name TEXT NOT NULL," +
                "item_price TEXT NOT NULL," +
                "item_quantity TEXT NOT NULL," +
                "item_priority TEXT NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS bought_items" +
                "(item_name TEXT NOT NULL," +
                "item_price TEXT NOT NULL," +
                "item_quantity TEXT NOT NULL," +
                "item_priority TEXT NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS shopping_list" +
                "(item_name TEXT NOT NULL," +
                "item_price TEXT NOT NULL," +
                "item_quantity TEXT NOT NULL," +
                "item_priority TEXT NOT NULL);");
//        sqLiteDatabase.execSQL("DELETE FROM shopping_list");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS unbought_items");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS bought_items");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS shopping_list");
        onCreate(sqLiteDatabase);
    }

}
