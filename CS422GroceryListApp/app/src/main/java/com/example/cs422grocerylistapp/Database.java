package com.example.cs422grocerylistapp;

public class Database {

    MyApplication myApplication = new MyApplication();

    private static final Database database = new Database();

    public static Database getDatabase(){
        return  database;
    }

    public Database() {
    }

}
