package com.belicoffee.Model;

public class Food {
    public int foodID;
    public String foodName;
    public double foodPrice;
    public String foodImage;

    public Food() {
    }

    public Food(int foodID, String foodName, double foodPrice, String foodImage) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodImage = foodImage;
    }

    public int getFoodID() {
        return foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }
}
