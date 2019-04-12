package com.example.cs422grocerylistapp;

import android.app.ActionBar;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentShoppingCart extends Fragment {

    public FragmentShoppingCart() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping_cart,
                container, false);

        final TableLayout boughtTable = (TableLayout) rootView.findViewById(R.id.bought_table);
        final TableLayout unboughtTable = (TableLayout) rootView.findViewById(R.id.unbought_table);
        final TextView total_cost_calculation = (TextView) rootView.findViewById(R.id.total_cost);
        Button displayButton = (Button) rootView.findViewById(R.id.display_button);

         /*
        When clicking the show purchase button, displays information into the tables
         */

        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteShoppingCartService sqLiteShoppingCartService = SQLiteShoppingCartService.getInstance(getContext());
                SQLiteDatabase sqLiteDatabase = sqLiteShoppingCartService.getReadableDatabase();

                int boughtRowId = 0;
                int unboughtRowId = 0;

                try {
                    //display bought table from database in bought table in shopping cart tab

                    String selectBoughtQuery = "SELECT * FROM bought_items";
                    Cursor boughtCursor = sqLiteDatabase.rawQuery(selectBoughtQuery, null, null);
                    if (boughtCursor.getCount() > 0) {
                        while (boughtCursor.moveToNext()) {
                            String bought_name = boughtCursor.getString(boughtCursor.getColumnIndexOrThrow("item_name"));
                            double bought_price = boughtCursor.getDouble(boughtCursor.getColumnIndexOrThrow("item_price"));
                            int bought_quantity = boughtCursor.getInt(boughtCursor.getColumnIndexOrThrow("item_quantity"));
                            double bought_cost = boughtCursor.getDouble(boughtCursor.getColumnIndexOrThrow("item_cost"));


//                            ArrayList<BudgetItem> boughtArrayList = new ArrayList<>();
//                            boughtArrayList.add(new BudgetItem(bought_name,bought_price,bought_quantity,bought_cost));
//                            System.out.println("bought Array List");
//                            System.out.println(boughtArrayList);


                            TableRow boughtListRow = new TableRow(getActivity().getApplicationContext());
                            boughtListRow.setId(boughtRowId++);

                            String[] boughtColumnNames = {bought_name, Double.toString(bought_price), Integer.toString(bought_quantity), Double.toString(bought_cost)};

                            for (int i = 0; i < boughtColumnNames.length; i++) {
                                TextView boughtListRowText = new TextView(getActivity().getApplicationContext());
                                boughtListRowText.setText(boughtColumnNames[i]);
                                boughtListRowText.setGravity(Gravity.CENTER_HORIZONTAL);
                                boughtListRow.addView(boughtListRowText);
                            }

                            boughtTable.addView(boughtListRow, new TableLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));

                        }

                        boughtCursor.close();

                    }

                    //display unbought table from database in unbought table in shopping cart tab

                    String selectUnboughtQuery = "SELECT * FROM unbought_items";
                    Cursor unboughtCursor = sqLiteDatabase.rawQuery(selectUnboughtQuery, null, null);
                    if (unboughtCursor.getCount() > 0) {
                        while (unboughtCursor.moveToNext()) {
                            String unbought_name = unboughtCursor.getString(unboughtCursor.getColumnIndexOrThrow("item_name"));
                            double unbought_price = unboughtCursor.getDouble(unboughtCursor.getColumnIndexOrThrow("item_price"));
                            int unbought_quantity = unboughtCursor.getInt(unboughtCursor.getColumnIndexOrThrow("item_quantity"));
                            double unbought_cost = unboughtCursor.getDouble(unboughtCursor.getColumnIndexOrThrow("item_cost"));

                            TableRow unboughtListRow = new TableRow(getActivity().getApplicationContext());
                            unboughtListRow.setId(unboughtRowId++);

                            String[] unboughtColumnNames = {unbought_name, Double.toString(unbought_price), Integer.toString(unbought_quantity), Double.toString(unbought_cost)};


                            for (int i = 0; i < unboughtColumnNames.length; i++) {
                                TextView unboughtListRowText = new TextView(getActivity().getApplicationContext());
                                unboughtListRowText.setText(unboughtColumnNames[i]);
                                unboughtListRowText.setGravity(Gravity.CENTER_HORIZONTAL);
                                unboughtListRow.addView(unboughtListRowText);
                            }

                            unboughtTable.addView(unboughtListRow, new TableLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                        }
                        unboughtCursor.close();
                    }

                                            /*
                    Update the database (unbought_items) after it's displayed to make cost be 1 so it's the new priority when displayed
                     */

                    SQLiteStatement updatePriority = sqLiteDatabase.compileStatement("UPDATE unbought_items SET item_cost = 1");
                    updatePriority.execute();

                } catch (SQLException sqLiteException) {
                    sqLiteException.printStackTrace();
                }

                //display the total cost

                double totalCost = 0;
                Cursor costCursor = sqLiteDatabase.rawQuery("SELECT SUM(item_cost) FROM bought_items", null);
                if (costCursor.moveToFirst()) {
                    totalCost = costCursor.getDouble(0);
                }
                while (costCursor.moveToNext()) ;

                total_cost_calculation.setText(Double.toString(totalCost));

                sqLiteDatabase.close();

            }
        });

        return rootView;
    }


    //    TODO figure out how to switch to the next tab
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}


