package com.example.bepro.home;

public class FoodItems {
    private String foodName; //식품명
    private String foodExpiryDate; //식품 유통기한
    private String foodRemainDate; //유통기한 남은 날짜
    private int foodTotalCount; //식품 개수

    public FoodItems(String foodName, String foodExpiryDate) {
        this.foodName = foodName;
        this.foodExpiryDate = foodExpiryDate;
    }

    public FoodItems(String foodName, String foodExpiryDate, String foodRemainDate) {
        this.foodName = foodName;
        this.foodExpiryDate = foodExpiryDate;
        this.foodRemainDate = foodRemainDate;
    }

    public FoodItems(String foodName, String foodRemainDate, int foodTotalCount) {
        this.foodName = foodName;
        this.foodRemainDate = foodRemainDate;
        this.foodTotalCount = foodTotalCount;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodExpiryDate() {
        return foodExpiryDate;
    }

    public void setFoodExpiryDate(String foodExpiryDate) {
        this.foodExpiryDate = foodExpiryDate;
    }

    public String getFoodRemainDate() {
        return foodRemainDate;
    }

    public void setFoodRemainDate(String foodRemainDate) {
        this.foodRemainDate = foodRemainDate;
    }

    public int getFoodTotalCount() {
        return foodTotalCount;
    }

    public void setFoodTotalCount(int foodTotalCount) {
        this.foodTotalCount = foodTotalCount;
    }
}
