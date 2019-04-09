package com.example.cs422grocerylistapp;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    TabItem shoppingListTab;
    TabItem shoppingCartTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        shoppingListTab = findViewById(R.id.shoppingList);
        shoppingCartTab = findViewById(R.id.shoppingCart);
        viewPager = findViewById(R.id.viewpager);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        /*
        This will retrieve the unbought items from the database and display it on the shopping list tab
         */
//        SQLiteShoppingCartService sqLiteShoppingCartService = new SQLiteShoppingCartService(this);
//        SQLiteDatabase sqLiteDatabase = sqLiteShoppingCartService.getReadableDatabase();
//        sqLiteDatabase.beginTransaction();
//        String selectUnboughtItems = "SELECT * FROM unbought_items";
//        Cursor cursor = sqLiteDatabase.rawQuery(selectUnboughtItems,null);
//
//        cursor.moveToFirst();
//        do{
//
//
//        }while (cursor.moveToNext());
//        cursor.close();



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


    }
}
