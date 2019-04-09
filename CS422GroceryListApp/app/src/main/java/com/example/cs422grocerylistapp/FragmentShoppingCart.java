package com.example.cs422grocerylistapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

public class FragmentShoppingCart extends Fragment {

    public FragmentShoppingCart(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping_cart,
                container, false);

        final TableLayout boughtTable = (TableLayout) rootView.findViewById(R.id.bought_table);
        final TableLayout unboughtTable = (TableLayout) rootView.findViewById(R.id.unbought_table);

//        data.moveToFirst();
//        do {
//            View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item,null,false);
//            TextView name  = (TextView) tableRow.findViewById(R.id.name);
//            TextView title  = (TextView) tableRow.findViewById(R.id.title);
//
//
//            name.setText(data.getString(1));
//            title.setText(data.getString(2));
//            tableLayout.addView(tableRow);
//
//        } while (data.moveToNext());
//        data.close();

        return rootView;
}

    //    TODO figure out how to switch to the next tab
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}


