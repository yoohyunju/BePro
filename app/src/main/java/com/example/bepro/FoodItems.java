package com.example.bepro;

public class FoodItems {
    private String foodName; //식품명
    private String foodExpiryDate; //식품 유통기한
    private String remainDate; //유통기한 남은 날짜

    public FoodItems(String foodName, String foodExpiryDate, String remainDate) {
        this.foodName = foodName;
        this.foodExpiryDate = foodExpiryDate;
        this.remainDate = remainDate;
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

    public String getRemainDate() {
        return remainDate;
    }

    public void setRemainDate(String remainDate) {
        this.remainDate = remainDate;
    }
}
