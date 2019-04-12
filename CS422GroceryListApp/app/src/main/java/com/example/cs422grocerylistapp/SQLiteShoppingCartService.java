package com.example.cs422grocerylistapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteShoppingCartService extends SQLiteOpenHelper {

    private static SQLiteShoppingCartService instance;

    public SQLiteShoppingCartService(Context context) {
        super(context, "ShoppingCart.db", null, 1);
    }

    public static synchronized SQLiteShoppingCartService getInstance(Context context) {

        if (instance == null) {
            instance = new SQLiteShoppingCartService(context.getApplicationContext());
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS unbought_items" +
                "(item_name TEXT NOT NULL," +
                "item_price TEXT NOT NULL," +
                "item_quantity TEXT NOT NULL," +
                "item_cost TEXT NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS bought_items" +
                "(item_name TEXT NOT NULL," +
                "item_price TEXT NOT NULL," +
                "item_quantity TEXT NOT NULL," +
                "item_cost TEXT NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS shopping_list" +
                "(item_name TEXT NOT NULL," +
                "item_price TEXT NOT NULL," +
                "item_quantity TEXT NOT NULL," +
                "item_priority TEXT NOT NULL);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS unbought_items");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS bought_items");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS shopping_list");
        onCreate(sqLiteDatabase);
    }


}
