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


    //
//        try{
//            String selectBoughtQuery = "SELECT * FROM bought_items";
//            Cursor boughtCursor = sqLiteDatabase.rawQuery(selectBoughtQuery,null,null);
//            if(boughtCursor.getCount()>0){
//                while(boughtCursor.moveToNext()) {
//                    String name = boughtCursor.getString(boughtCursor.getColumnIndexOrThrow("item_name"));
//                    double price = boughtCursor.getDouble(boughtCursor.getColumnIndexOrThrow("item_price"));
//                    int quantity = boughtCursor.getInt(boughtCursor.getColumnIndexOrThrow("item_quantity"));
//                    double cost = boughtCursor.getInt(boughtCursor.getColumnIndexOrThrow("item_cost"));
//
//                    TableRow boughtList_row = new TableRow(getActivity().getApplicationContext());
//                    boughtList_row.setId(rowId++);
//
//                    TextView item_bought_name = new TextView(getActivity().getApplicationContext());
//                    item_bought_name.setText(name);
//
//                    boughtTable.addView(item_bought_name);
//
//                    TextView item_bought_price = new TextView(getActivity().getApplicationContext());
//                    item_bought_price.setText(Double.toString(price));
//
//                    boughtTable.addView(item_bought_price);
//
//                    TextView item_bought_quantity = new TextView(getActivity().getApplicationContext());
//                    item_bought_quantity.setText(Integer.toString(quantity));
//
//                    boughtTable.addView(item_bought_quantity);
//
//                    TextView item_bought_cost = new TextView(getActivity().getApplicationContext());
//                    item_bought_cost.setText(Double.toString(cost));
//
//                    boughtTable.addView(item_bought_cost);
//
//                    boughtTable.addView(boughtList_row, new TableLayout.LayoutParams(
//                            ViewGroup.LayoutParams.FILL_PARENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT));
//                }
//
//
//
//            }
//
//        }catch (SQLException sqLiteException){
//            sqLiteException.printStackTrace();
//        }
//
//        finally{
//            sqLiteDatabase.endTransaction();
//            sqLiteDatabase.close();
//        }

}
