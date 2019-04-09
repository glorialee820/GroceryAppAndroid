package com.example.cs422grocerylistapp;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    PagerAdapter(FragmentManager fragmentManager , int  numOfTabs) {

        super(fragmentManager);
        this.numOfTabs= numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FragmentShoppingList();
            case 1:
                return new FragmentShoppingCart();
            default:
                return null;

        }
    }
    @Override
    public int getCount(){return  numOfTabs;}


}

