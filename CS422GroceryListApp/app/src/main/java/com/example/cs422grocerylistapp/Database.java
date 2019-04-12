package com.example.cs422grocerylistapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {

    MyApplication myApplication = new MyApplication();

    private static final Database database = new Database();

    public static Database getDatabase(){
        return  database;
    }

    public Database() {
    }

//    public FragmentShoppingList getShoppingCart(){
//        SQLiteShoppingCartService sqLiteShoppingCartService = new SQLiteShoppingCartService(myApplication.getApplicationContext());
//        SQLiteDatabase sqLiteDatabase = sqLiteShoppingCartService.getReadableDatabase();
//
//        String[] columnNames = {"item_name, item_price, item_quantity, item_priority"};
//        Cursor cursor = sqLiteDatabase.query("shopping_list", columnNames, null, null, null , null,null);
//
//        FragmentShoppingList fragmentShoppingList = new FragmentShoppingList();
//        while(cursor.moveToNext()){
//            String item_name = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
//            double item_price = cursor.getDouble(cursor.getColumnIndexOrThrow("item_price"));
//            int item_quantity = cursor.getInt(cursor.getColumnIndexOrThrow("item_quantity"));
//            int item_priority = cursor.getInt(cursor.getColumnIndexOrThrow("item_priority"));
////            fragmentShoppingList.(new ShoppingItem(item_name, item_price, item_quantity, item_priority));
//        }
//
//
//
//    }

//
//    public void addUnboughtItem(FragmentShoppingList fragmentShoppingList){
//        SQLiteDatabase sqLiteShoppingCartService = this.getWritableDatabase();
//        ((SQLiteDatabase) sqLiteShoppingCartService).beginTransaction();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("item_name", fragmentShoppingList.itemName);
//        contentValues.put("item_price", fragmentShoppingList.itemPrice);
//        contentValues.put("item_quantity", fragmentShoppingList.itemQuantity);
//        contentValues.put("item_priority", fragmentShoppingList.itemPriority);
//
//        sqLiteShoppingCartService.insert("unbought_table", null, contentValues);
//        sqLiteShoppingCartService.endTransaction();
//        sqLiteShoppingCartService.close();
}
//
//    public void addBoughtItem(FragmentShoppingList fragmentShoppingList){
//        SQLiteDatabase sqLiteShoppingCartService = this.getWritableDatabase();
//        ((SQLiteDatabase) sqLiteShoppingCartService).beginTransaction();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("item_name", fragmentShoppingList.itemName);
//        contentValues.put("item_price", fragmentShoppingList.itemPrice);
//        contentValues.put("item_quantity", fragmentShoppingList.itemQuantity);
//        contentValues.put("item_priority", fragmentShoppingList.itemPriority);
//
//        sqLiteShoppingCartService.insert("bought_table", null, contentValues);
//        sqLiteShoppingCartService.endTransaction();
//        sqLiteShoppingCartService.close();
//    }
//}