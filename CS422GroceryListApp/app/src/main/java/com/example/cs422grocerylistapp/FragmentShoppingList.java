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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;


public class FragmentShoppingList extends Fragment {

    public FragmentShoppingList() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
        sqLiteDatabase.execSQL("DELETE FROM bought_items");

        /*
        Display the unbought items from the last shopping trip into the shopping list
         */

//        if () {//figure out if there's a way to say if the unbought_item tables is null

            int lastTripRowID = 0;

            String selectLastTrip = "SELECT * FROM unbought_items";
            Cursor lastTripCursor = sqLiteDatabase.rawQuery(selectLastTrip, null, null);
            if (lastTripCursor.getCount() > 0) {
                while (lastTripCursor.moveToNext()) {
                    String lastTrip_name = lastTripCursor.getString(lastTripCursor.getColumnIndexOrThrow("item_name"));
                    double lastTrip_price = lastTripCursor.getDouble(lastTripCursor.getColumnIndexOrThrow("item_price"));
                    int lastTrip_quantity = lastTripCursor.getInt(lastTripCursor.getColumnIndexOrThrow("item_quantity"));
                    int lastTrip_cost_to_priority = lastTripCursor.getInt(lastTripCursor.getColumnIndexOrThrow("item_cost"));

                    TableRow lastTripRow = new TableRow(getActivity().getApplicationContext());
                    lastTripRow.setId(lastTripRowID++);

                    String[] lastTripColumnNames = {lastTrip_name, Double.toString(lastTrip_price), Integer.toString(lastTrip_quantity), Integer.toString(lastTrip_cost_to_priority)};

                    for (int i = 0; i < lastTripColumnNames.length; i++) {
                        TextView lastTripRowText = new TextView(getActivity().getApplicationContext());
                        lastTripRowText.setText(lastTripColumnNames[i]);
                        lastTripRowText.setGravity(Gravity.CENTER_HORIZONTAL);
                        lastTripRow.addView(lastTripRowText);
                    }

                    shoppingListTable.addView(lastTripRow, new TableLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                }
            }
//        }

            // end of the attempt at displaying table


            //TODO figure out to display unbought table from the database into the shopping list table


        /*
        When clicking the add button, item name, price, quantity, and priority is placed into the list
         */


            addButton.setOnClickListener(new View.OnClickListener() {
                int rowId = 0;

                @Override
                public void onClick(View view) {

                    try {
                        TableRow shoppingListRow = new TableRow(getActivity().getApplicationContext());
                        shoppingListRow.setId(rowId++);

                        TextView item_name = new TextView(getActivity().getApplicationContext());
                        String nameString = name_input.getText().toString();
                        item_name.setText(name_input.getText().toString());

                        item_name.setGravity(Gravity.CENTER_HORIZONTAL);
                        shoppingListRow.addView(item_name);

//                        for (int i = 0; i < nameString.length(); i++) {
//                            if (nameString.length() != 1 || !Character.isLetter(nameString.charAt(0))) {
//                                Toast.makeText(view.getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
//                            }
//                        }

                        TextView item_price = new TextView(getActivity().getApplicationContext());
                        item_price.setText(String.format("%.2f", Double.parseDouble(price_input.getText().toString())));
                        item_price.setGravity(Gravity.CENTER_HORIZONTAL);
                        shoppingListRow.addView(item_price);


                        TextView item_quantity = new TextView(getActivity().getApplicationContext());
                        item_quantity.setText(quantity_input.getText().toString());

                        item_quantity.setGravity(Gravity.CENTER_HORIZONTAL);
                        shoppingListRow.addView(item_quantity);

                        TextView item_priority = new TextView(getActivity().getApplicationContext());
                        item_priority.setText(priority_input.getText().toString());

                        item_priority.setGravity(Gravity.CENTER_HORIZONTAL);
                        shoppingListRow.addView(item_priority);

                        shoppingListTable.addView(shoppingListRow, new TableLayout.LayoutParams(
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

                    } catch (NumberFormatException nfe) {
                        Toast.makeText(view.getContext(), "Invalid input", Toast.LENGTH_SHORT).show();

                    }

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

                    }
                    shoppingListCursor.close();

//                    System.out.println("shopping list");
//                    System.out.println(shoppingItemArrayList);

                    //going through shopping array list and differentiating between bought and unbought
                    ShoppingBudget shoppingBudget = new ShoppingBudget(Double.parseDouble(budget_input.getText().toString()), shoppingItemArrayList);
                    ArrayList<BudgetItem> unboughtList = shoppingBudget.getMyShoppingListUnbought();
                    ArrayList<BudgetItem> boughtList = shoppingBudget.getMyShoppingListBought();

                    budget_input.getText().clear();

                    ContentValues boughtListContentValues = new ContentValues();

                    for (int i = 0; i < boughtList.size(); i++) {
                        BudgetItem budgetItem = boughtList.get(i);
                        boughtListContentValues.put("item_name", budgetItem.getName());
                        boughtListContentValues.put("item_price", budgetItem.getPrice());
                        boughtListContentValues.put("item_quantity", budgetItem.getQuantity());
                        boughtListContentValues.put("item_cost", budgetItem.getCost());

                        //assumption will always buy something
                        sqLiteDatabase.insert("bought_items", null, boughtListContentValues);
                    }
//
//                    System.out.println("bought list");
//                    System.out.println(boughtList);

                    ContentValues unboughtListContentValues = new ContentValues();

                    for (int i = 0; i < unboughtList.size(); i++) {
                        BudgetItem budgetItem = unboughtList.get(i);
                        unboughtListContentValues.put("item_name", budgetItem.getName());
                        unboughtListContentValues.put("item_price", budgetItem.getPrice());
                        unboughtListContentValues.put("item_quantity", budgetItem.getQuantity());
                        unboughtListContentValues.put("item_cost", budgetItem.getCost());

                        //makes it so it doesn't add to a null table
                        if (!unboughtList.isEmpty()) {
                            sqLiteDatabase.insert("unbought_items", null, unboughtListContentValues);
                        }
                    }


//                    System.out.println("unbought list");
//                    System.out.println(unboughtList);


                    sqLiteDatabase.close();

                }

            });

        /*
        When clicking the done button, switches to the ShoppingCart Tab
         */

            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tabs.getTabAt(1).select();

                }
            });


            return rootView;
        }

        @Override
        public void onActivityCreated (Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);

        }
    }



