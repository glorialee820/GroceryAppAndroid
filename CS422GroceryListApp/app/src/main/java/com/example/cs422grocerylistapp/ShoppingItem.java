package com.example.cs422grocerylistapp;

public class ShoppingItem implements Comparable{

    private String name;
    private double price;
    private int quantity;
    private int priority;

    public ShoppingItem(String n, double p, int q, int pr){
        name = n;
        price = p;
        quantity = q;
        priority = pr;
    }


    public int getPriority() {

        return priority;
    }

    public String getName() {

        return name;
    }

    public int getQuantity() {

        return quantity;
    }

    public double getPrice() {
        return price;
    }


    public double getCost(){
        return getPrice() * getQuantity();
    }

    @Override
    public int compareTo(Object o1) {
        return priority - ((ShoppingItem)o1).priority;
    }

}
