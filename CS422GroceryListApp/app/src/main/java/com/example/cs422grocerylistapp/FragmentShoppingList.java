package com.example.cs422grocerylistapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FragmentShoppingList extends Fragment{

    MyApplication myApplication = new MyApplication();

    public FragmentShoppingList() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shopping_list,
                container, false);

        Button addButton = (Button) rootView.findViewById(R.id.add_button);
        Button setBudget = (Button) rootView.findViewById(R.id.budget_button);
        Button doneButton = (Button) rootView.findViewById(R.id.button_done);
        final TableLayout shoppingListTable = (TableLayout) rootView.findViewById(R.id.shoppingList_table);
        final TableLayout shoppingCartUnboughtTable = (TableLayout) rootView.findViewById(R.id.unbought_table);
        final TableLayout shoppingCartBoughtTable = (TableLayout) rootView.findViewById(R.id.bought_table);
        final EditText name_input = (EditText) rootView.findViewById(R.id.name_input);
        final EditText price_input = (EditText) rootView.findViewById(R.id.price_input);
        final EditText quantity_input = (EditText) rootView.findViewById(R.id.quantity_input);
        final EditText priority_input = (EditText) rootView.findViewById(R.id.priority_input);
        final EditText budget_input = (EditText) rootView.findViewById(R.id.budget_input);
        final TableRow shoppingList_row = (TableRow) rootView.findViewById(R.id.display_shoppinglist_row);
        final TableRow shoppingUnbought_row = (TableRow) rootView.findViewById(R.id.display_unbought_row);
        final TableRow shoppingBought_row = (TableRow) rootView.findViewById(R.id.display_bought_row);
        final TabLayout tabs = (TabLayout)((MainActivity)getActivity()).findViewById(R.id.tab_layout);

        /*
        delete shopping_list table before it runs
         */
        SQLiteShoppingCartService sqLiteShoppingCartService = new SQLiteShoppingCartService(getActivity());
        SQLiteDatabase sqLiteDatabase = sqLiteShoppingCartService.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM shopping_list");
        /*
        When clicking the add button, item name, price, quantity, and priority is placed into the list
         */
        addButton.setOnClickListener(new View.OnClickListener() {
            int rowId = 0;

            @Override
            public void onClick(View view) {

                TableRow shoppingList_row = new TableRow(getActivity().getApplicationContext());
                shoppingList_row.setId(rowId++);

                TextView item_name = new TextView(getActivity().getApplicationContext());
                item_name.setText(name_input.getText().toString());

                shoppingList_row.addView(item_name);

                TextView item_price = new TextView(getActivity().getApplicationContext());

                //change the price to currency format
                Locale locale = Locale.US;
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                item_price.setText(numberFormat.format(Double.parseDouble(price_input.getText().toString())));

                shoppingList_row.addView(item_price);

                TextView item_quantity = new TextView(getActivity().getApplicationContext());
                item_quantity.setText(quantity_input.getText().toString());

                shoppingList_row.addView(item_quantity);

                TextView item_priority = new TextView(getActivity().getApplicationContext());
                item_priority.setText(priority_input.getText().toString());

                shoppingList_row.addView(item_priority);

                shoppingListTable.addView(shoppingList_row, new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                SQLiteShoppingCartService sqLiteShoppingCartService = new SQLiteShoppingCartService(getActivity());
                SQLiteDatabase sqLiteDatabase = sqLiteShoppingCartService.getWritableDatabase();

                ContentValues shoppingListContentValues = new ContentValues();

                //putting table into shopping list table in the database
                shoppingListContentValues.put("item_name", String.valueOf(item_name.getText()));
                shoppingListContentValues.put("item_price", String.valueOf(item_price.getText()));
                shoppingListContentValues.put("item_quantity", String.valueOf(item_quantity.getText()));
                shoppingListContentValues.put("item_priority", String.valueOf(item_priority.getText()));

                sqLiteDatabase.insert("shopping_list", null, shoppingListContentValues);

                name_input.getText().clear();
                price_input.getText().clear();
                quantity_input.getText().clear();
                priority_input.getText().clear();

            }
        });

        /*
        When clicking the add initialBudget button, shoppingBudget.buyItems method is executed (shopping list
        differentiated between bought and unbought depending on the initialBudget on ShoppingCart Tab
        Then the unbought list is saved into a database
         */


        setBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView shopping_budget = new TextView(getActivity().getApplicationContext());
                shopping_budget.setText(budget_input.getText().toString());


                ArrayList<ShoppingItem> shoppingItemArrayList = new ArrayList<>();
//                ArrayList<BudgetItem> unboughtList = new ArrayList<>();
//                ArrayList<BudgetItem> boughtList = new ArrayList<>();

                SQLiteShoppingCartService sqLiteShoppingCartService = new SQLiteShoppingCartService(getActivity());
                SQLiteDatabase sqLiteDatabase = sqLiteShoppingCartService.getWritableDatabase();

                String[] columnNames = {"item_name, item_price, item_quantity, item_priority"};

                Cursor shoppingListCursor = sqLiteDatabase.query("shopping_list", columnNames, null, null, null, null, null);

                //adding the contents of the shopping_list in the database into an array list
                while (shoppingListCursor.moveToNext()) {
                    String name = shoppingListCursor.getString(shoppingListCursor.getColumnIndexOrThrow("item_name"));
                    double price = shoppingListCursor.getDouble(shoppingListCursor.getColumnIndexOrThrow("item_price"));
                    int quantity = shoppingListCursor.getInt(shoppingListCursor.getColumnIndexOrThrow("item_quantity"));
                    int priority = shoppingListCursor.getInt(shoppingListCursor.getColumnIndexOrThrow("item_priority"));
                    shoppingItemArrayList.add(new ShoppingItem(name, price,quantity,priority));
                }
                shoppingListCursor.close();

                //going through shopping array list and differentiating between bought and unbought
                ShoppingBudget shoppingBudget = new ShoppingBudget(Double.parseDouble(budget_input.getText().toString()), shoppingItemArrayList);
                shoppingBudget.getMyShoppingListBought();
                shoppingBudget.getMyShoppingListUnbought();

                budget_input.getText().clear();

                //put myShoppingListUnbought into SQLite Database

                JSONObject jsonObjectUnbought = new JSONObject();
                try {
                    jsonObjectUnbought.put("myShoppingListUnbought", new JSONArray(shoppingBudget.getMyShoppingListUnbought()));
                    String unboughtString = jsonObjectUnbought.toString();
                    String [] unboughtStringArray = unboughtString.split(",");

                    Cursor unboughtListCursor = sqLiteDatabase.query("unbought_list", unboughtStringArray, null,null,null,null,null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        });

        /*
        When clicking the done button, switches to the ShoppingCart Tab (display the SQLite data)
         */

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabs.getTabAt(1).select();
//
////                SQLiteShoppingCartService sqLiteShoppingCartService = new SQLiteShoppingCartService(getActivity().getApplicationContext());
//                SQLiteShoppingCartService sqLiteShoppingCartService = new SQLiteShoppingCartService(getActivity());
//                SQLiteDatabase sqLiteDatabase = sqLiteShoppingCartService.getReadableDatabase();
//                sqLiteDatabase = sqLiteShoppingCartService.getReadableDatabase();
//                sqLiteDatabase.beginTransaction();
//
////wrap in an if statement
//                try {
//                    String selectUnboughtQuery = "SELECT * FROM unbought_items";
//                    Cursor cursor = sqLiteDatabase.rawQuery(selectUnboughtQuery, null);
//
//                    if (cursor.getCount() > 0) {
//                        String item_name = cursor.getString(cursor.getColumnIndex("item_name"));
//                        double item_price = cursor.getDouble(cursor.getColumnIndexOrThrow("item_price"));
//                        int item_quantity = cursor.getInt(cursor.getColumnIndexOrThrow("item_quantity"));
//                        int item_priority = cursor.getInt(cursor.getColumnIndexOrThrow("item_priority"));
//
//                        shoppingUnbought_row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
//                                TableLayout.LayoutParams.WRAP_CONTENT));
//                    }
//
//                    shoppingCartUnboughtTable.addView(shoppingUnbought_row);
//
//                }catch (SQLException sqlexception){
//                    sqlexception.printStackTrace();
//                }
//
////wrap in an if statement
//                try {
//                    String selectBoughtQuery = "SELECT * FROM bought_items";
//                    Cursor cursor = sqLiteDatabase.rawQuery(selectBoughtQuery, null);
//
//                    if (cursor.getCount() > 0) {
//                        String item_name = cursor.getString(cursor.getColumnIndex("item_name"));
//                        double item_price = cursor.getDouble(cursor.getColumnIndexOrThrow("item_price"));
//                        int item_quantity = cursor.getInt(cursor.getColumnIndexOrThrow("item_quantity"));
//                        int item_priority = cursor.getInt(cursor.getColumnIndexOrThrow("item_priority"));
//
//                        shoppingBought_row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
//                                TableLayout.LayoutParams.WRAP_CONTENT));
//                    }
//
//                    shoppingCartBoughtTable.addView(shoppingBought_row);
//
//                }catch (SQLException sqlexception){
//                    sqlexception.printStackTrace();
//                }
//
//                finally{
//                    sqLiteDatabase.endTransaction();
//                    sqLiteDatabase.close();
//                }

            }
        });


        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}

