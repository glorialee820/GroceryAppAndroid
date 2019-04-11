package com.example.cs422grocerylistapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import java.util.ArrayList;
import java.util.Locale;

public class FragmentShoppingList extends Fragment {

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
        final TableLayout boughtTable = (TableLayout) rootView.findViewById(R.id.bought_table);
        final TableLayout unboughtTable = (TableLayout) rootView.findViewById(R.id.unbought_table);
        final EditText name_input = (EditText) rootView.findViewById(R.id.name_input);
        final EditText price_input = (EditText) rootView.findViewById(R.id.price_input);
        final EditText quantity_input = (EditText) rootView.findViewById(R.id.quantity_input);
        final EditText priority_input = (EditText) rootView.findViewById(R.id.priority_input);
        final EditText budget_input = (EditText) rootView.findViewById(R.id.budget_input);
        final TableRow shoppingList_row = (TableRow) rootView.findViewById(R.id.display_shoppinglist_row);
        final TableRow shoppingUnbought_row = (TableRow) rootView.findViewById(R.id.display_unbought_row);
        final TableRow shoppingBought_row = (TableRow) rootView.findViewById(R.id.display_bought_row);
        final TabLayout tabs = (TabLayout) ((MainActivity) getActivity()).findViewById(R.id.tab_layout);

        /*
        delete shopping_list table before it runs
         */
        SQLiteShoppingCartService sqLiteShoppingCartService = new SQLiteShoppingCartService(getActivity());
        SQLiteDatabase sqLiteDatabase = sqLiteShoppingCartService.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM shopping_list");
        sqLiteDatabase.execSQL("DELETE FROM unbought_items");
        sqLiteDatabase.execSQL("DELETE FROM bought_items");
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
                item_price.setText(price_input.getText().toString());

                //change the price to currency format (couldn't use - broke code)
//                Locale locale = Locale.US;
//                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
//                item_price.setText(numberFormat.format(Double.parseDouble(price_input.getText().toString())));

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


                SQLiteShoppingCartService sqLiteShoppingCartService = SQLiteShoppingCartService.getInstance(getContext());
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

                SQLiteShoppingCartService sqLiteShoppingCartService = SQLiteShoppingCartService.getInstance(getContext());
                SQLiteDatabase sqLiteDatabase = sqLiteShoppingCartService.getWritableDatabase();

                String[] columnNames = {"item_name, item_price, item_quantity, item_priority"};

                Cursor shoppingListCursor = sqLiteDatabase.query("shopping_list", columnNames, null, null, null, null, null);

                //adding the contents of the shopping_list in the database into an array list
                while (shoppingListCursor.moveToNext()) {
                    String name = shoppingListCursor.getString(shoppingListCursor.getColumnIndexOrThrow("item_name"));
                    double price = shoppingListCursor.getDouble(shoppingListCursor.getColumnIndexOrThrow("item_price"));
                    int quantity = shoppingListCursor.getInt(shoppingListCursor.getColumnIndexOrThrow("item_quantity"));
                    int priority = shoppingListCursor.getInt(shoppingListCursor.getColumnIndexOrThrow("item_priority"));
                    shoppingItemArrayList.add(new ShoppingItem(name, price, quantity, priority));

                    System.out.println("shopping item array list");
                    System.out.println(shoppingItemArrayList);
                }
                shoppingListCursor.close();

                //going through shopping array list and differentiating between bought and unbought
                ShoppingBudget shoppingBudget = new ShoppingBudget(Double.parseDouble(budget_input.getText().toString()), shoppingItemArrayList);
                ArrayList<BudgetItem> unboughtList = shoppingBudget.getMyShoppingListUnbought();
                ArrayList<BudgetItem> boughtList = shoppingBudget.getMyShoppingListBought();

                budget_input.getText().clear();

                //put unbought items into database
                //TODO Figure out what to do if all items are bought

                ContentValues unboughtListContentValues = new ContentValues();

                for (int i = 0; i < unboughtList.size(); i++) {
                    BudgetItem budgetItem = unboughtList.get(i);
                    unboughtListContentValues.put("item_name", budgetItem.getName());
                    unboughtListContentValues.put("item_price", budgetItem.getPrice());
                    unboughtListContentValues.put("item_quantity", budgetItem.getQuantity());
                    unboughtListContentValues.put("item_cost", budgetItem.getCost());

//                    System.out.println("unbought items");
//                    System.out.println(budgetItem.getName());
//                    System.out.println(budgetItem.getPrice());
//                    System.out.println(budgetItem.getQuantity());
//                    System.out.println(budgetItem.getCost());
                }

                sqLiteDatabase.insert("unbought_items", null, unboughtListContentValues);

                ContentValues boughtListContentValues = new ContentValues();

                for (int i = 0; i < boughtList.size(); i++) {
                    BudgetItem budgetItem = boughtList.get(i);
                    boughtListContentValues.put("item_name", budgetItem.getName());
                    boughtListContentValues.put("item_price", budgetItem.getPrice());
                    boughtListContentValues.put("item_quantity", budgetItem.getQuantity());
                    boughtListContentValues.put("item_cost", budgetItem.getCost());

//                    System.out.println("bought items");
//                    System.out.println(budgetItem.getName());
//                    System.out.println(budgetItem.getPrice());
//                    System.out.println(budgetItem.getQuantity());
//                    System.out.println(budgetItem.getCost());
                }

                sqLiteDatabase.insert("bought_items", null, boughtListContentValues);

            }
        });

        /*
        When clicking the done button, switches to the ShoppingCart Tab (display the SQLite data)
         */

        doneButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                tabs.getTabAt(1).select();
//
//                SQLiteShoppingCartService sqLiteShoppingCartService = SQLiteShoppingCartService.getInstance(getContext());
//                SQLiteDatabase sqLiteDatabase = sqLiteShoppingCartService.getReadableDatabase();
//
//                int boughtRowId = 0;
//                int unboughtRowId = 0;
//
//                try {
//                    //display bought table from database in bought table in shopping cart tab
//
//                    String selectBoughtQuery = "SELECT * FROM bought_items";
//                    Cursor boughtCursor = sqLiteDatabase.rawQuery(selectBoughtQuery, null, null);
//                    if (boughtCursor.getCount() > 0) {
//                        while (boughtCursor.moveToNext()) {
//                            String bought_name = boughtCursor.getString(boughtCursor.getColumnIndexOrThrow("item_name"));
//                            double bought_price = boughtCursor.getDouble(boughtCursor.getColumnIndexOrThrow("item_price"));
//                            int bought_quantity = boughtCursor.getInt(boughtCursor.getColumnIndexOrThrow("item_quantity"));
//                            double bought_cost = boughtCursor.getInt(boughtCursor.getColumnIndexOrThrow("item_cost"));
//
//                            TableRow boughtList_row = new TableRow(getActivity().getApplicationContext());
//                            boughtList_row.setId(boughtRowId++);
//
//                            TextView item_bought_name = new TextView(getActivity().getApplicationContext());
//                            item_bought_name.setText(bought_name);
//
//                            System.out.println(bought_name);
//
//                            boughtTable.addView(item_bought_name);
//
//                            TextView item_bought_price = new TextView(getActivity().getApplicationContext());
//                            item_bought_price.setText(Double.toString(bought_price));
//
//                            boughtTable.addView(item_bought_price);
//
//                            TextView item_bought_quantity = new TextView(getActivity().getApplicationContext());
//                            item_bought_quantity.setText(Integer.toString(bought_quantity));
//
//                            boughtTable.addView(item_bought_quantity);
//
//                            TextView item_bought_cost = new TextView(getActivity().getApplicationContext());
//                            item_bought_cost.setText(Double.toString(bought_cost));
//
//                            boughtTable.addView(item_bought_cost);
//
//                            boughtTable.addView(boughtList_row, new TableLayout.LayoutParams(
//                                    ViewGroup.LayoutParams.FILL_PARENT,
//                                    ViewGroup.LayoutParams.WRAP_CONTENT));
//
//                            //display unbought table from database in unbought table in shopping cart tab
//
//                            String selectUnboughtQuery = "SELECT * FROM unbought_items";
//                            Cursor unboughtCursor = sqLiteDatabase.rawQuery(selectUnboughtQuery, null, null);
//                            if (unboughtCursor.getCount() > 0) {
//                                while (boughtCursor.moveToNext()) {
//                                    String unbought_name = unboughtCursor.getString(boughtCursor.getColumnIndexOrThrow("item_name"));
//                                    double unbought_price = unboughtCursor.getDouble(boughtCursor.getColumnIndexOrThrow("item_price"));
//                                    int unbought_quantity = unboughtCursor.getInt(boughtCursor.getColumnIndexOrThrow("item_quantity"));
//                                    double unbought_cost = unboughtCursor.getInt(boughtCursor.getColumnIndexOrThrow("item_cost"));
//
//                                    TableRow unboughtList_row = new TableRow(getActivity().getApplicationContext());
//                                    unboughtList_row.setId(unboughtRowId++);
//
//                                    TextView item_unbought_name = new TextView(getActivity().getApplicationContext());
//                                    item_bought_name.setText(unbought_name);
//
//                                    unboughtTable.addView(item_unbought_name);
//
//                                    TextView item_unbought_price = new TextView(getActivity().getApplicationContext());
//                                    item_bought_price.setText(Double.toString(unbought_price));
//
//                                    unboughtTable.addView(item_unbought_price);
//
//                                    TextView item_unbought_quantity = new TextView(getActivity().getApplicationContext());
//                                    item_bought_quantity.setText(Integer.toString(unbought_quantity));
//
//                                    unboughtTable.addView(item_unbought_quantity);
//
//                                    TextView item_unbought_cost = new TextView(getActivity().getApplicationContext());
//                                    item_bought_cost.setText(Double.toString(unbought_cost));
//
//                                    unboughtTable.addView(item_unbought_cost);
//
//                                    unboughtTable.addView(unboughtList_row, new TableLayout.LayoutParams(
//                                            ViewGroup.LayoutParams.FILL_PARENT,
//                                            ViewGroup.LayoutParams.WRAP_CONTENT));
//                                }
//                            }
//                        }
//                    }
//
//                } catch (SQLException sqLiteException) {
//                    sqLiteException.printStackTrace();
//                } finally {
//                    sqLiteDatabase.endTransaction();
//                    sqLiteDatabase.close();
//                }
//
//
            }
        });


        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}