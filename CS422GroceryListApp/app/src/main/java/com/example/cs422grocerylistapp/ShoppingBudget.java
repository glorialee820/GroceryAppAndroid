package com.example.cs422grocerylistapp;

import java.util.*;
import java.util.ArrayList;

class SortByPriority implements Comparator<ShoppingItem>
{
    public int compare(ShoppingItem a, ShoppingItem b) {
        return a.getPriority() - b.getPriority();
    }

}

public class ShoppingBudget{

    private ArrayList<ShoppingItem> myShoppingList;
    private ArrayList<BudgetItem> myShoppingListBought = new ArrayList<>();
    private ArrayList <BudgetItem> myShoppingListUnbought = new ArrayList<>();
    double initialBudget;
    double totalCost;

    public ShoppingBudget(double b, ArrayList<ShoppingItem> shoppingList) {
        initialBudget = b;
        this.myShoppingList = shoppingList;
        Collections.sort(shoppingList, new SortByPriority());
        buyItems(shoppingList);
    }

    public ArrayList<BudgetItem> getMyShoppingListBought() {
        return myShoppingListBought;
    }

    public ArrayList<BudgetItem> getMyShoppingListUnbought() {
        return myShoppingListUnbought;
    }

    public double getTotalCost(){
        return totalCost;
    }

    private void buyItems(ArrayList<ShoppingItem> buyFromList) {
        int i = 0;

        int MAX_ELEMENT = myShoppingList.size();

        double tempBudget = initialBudget;

        //differentiate the lists of myShoppingListBought and myShoppingListUnbought
        while (i < MAX_ELEMENT) {
            ShoppingItem currentShoppingItem = myShoppingList.get(i);

            if (tempBudget >= myShoppingList.get(i).getCost()) {
                BudgetItem newBoughtItem = new BudgetItem(
                        currentShoppingItem.getName(),
                        currentShoppingItem.getPrice(),
                        currentShoppingItem.getQuantity(),
                        currentShoppingItem.getCost()
                );
                myShoppingListBought.add(newBoughtItem);
                tempBudget -= myShoppingList.get(i).getCost();
            }
            else {
                BudgetItem newUnboughtItem = new BudgetItem(
                        currentShoppingItem.getName(),
                        currentShoppingItem.getPrice(),
                        currentShoppingItem.getQuantity(),
                        currentShoppingItem.getCost()
                );
                myShoppingListUnbought.add(newUnboughtItem);
            }
            i++;
        }

        totalCost = initialBudget - tempBudget;


//        TEST: See if the unbought and bought separated correctly
        System.out.println("totalCost: "+ totalCost);
        System.out.println("myShoppingListBought items:");
        System.out.println(this.myShoppingListBought.toString());
        System.out.println("myShoppingListUnbought items:");
        System.out.println(this.myShoppingListUnbought.toString());
    }

}