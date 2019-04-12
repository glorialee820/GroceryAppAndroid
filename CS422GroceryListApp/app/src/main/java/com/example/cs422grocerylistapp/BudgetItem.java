package com.example.cs422grocerylistapp;

public class BudgetItem {

        private String name;
        private double price;
        private int quantity;
        private double cost;

        public BudgetItem(String n, double p, int q, double c){
            name = n;
            price = p;
            quantity = q;
            cost = c;
        }

        public String getName() {

            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getCost(){
            return getPrice() * getQuantity();
        }

    @Override
    public String toString() {
        return "<" + this.name + " " + this.price + " " + this.quantity + " " + this.cost + "> \n";
    }
}


