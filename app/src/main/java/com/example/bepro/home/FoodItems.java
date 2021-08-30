package com.example.bepro.home;

import java.util.Date;

public class FoodItems {
    private int friIdx; //냉장고 인덱스
    private int foodIdx; //품목 인덱스
    private String foodName; //식품명
    private int foodNumber; //식품 개수
    private String foodRegistrant; //등록인
    private String foodExpiryDate; //식품 유통기한
    private String foodDate; //등록일 TODO: Date 형이 나을지 String이 나을지..
    private String foodRemainDate; //유통기한 남은 날짜 TODO: 추후 삭제하기

    public FoodItems(String foodName, String foodExpiryDate) {
        this.foodName = foodName;
        this.foodExpiryDate = foodExpiryDate;
    }

    public FoodItems(String foodName, String foodExpiryDate, String foodRemainDate) {
        this.foodName = foodName;
        this.foodExpiryDate = foodExpiryDate;
        this.foodRemainDate = foodRemainDate;
    }

    public FoodItems(String foodName, int foodNumber, String foodRemainDate) {
        this.foodName = foodName;
        this.foodNumber = foodNumber;
        this.foodRemainDate = foodRemainDate;
    }

    public FoodItems(int friIdx, int foodIdx, String foodName, int foodNumber, String foodRegistrant, String foodExpiryDate, String foodDate) {
        this.friIdx = friIdx;
        this.foodIdx = foodIdx;
        this.foodName = foodName;
        this.foodNumber = foodNumber;
        this.foodRegistrant = foodRegistrant;
        this.foodExpiryDate = foodExpiryDate;
        this.foodDate = foodDate;
    }

    public int getFriIdx() {
        return friIdx;
    }

    public void setFriIdx(int friIdx) {
        this.friIdx = friIdx;
    }

    public int getFoodIdx() {
        return foodIdx;
    }

    public void setFoodIdx(int foodIdx) {
        this.foodIdx = foodIdx;
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

    public int getFoodNumber() {
        return foodNumber;
    }

    public void setFoodNumber(int foodNumber) {
        this.foodNumber = foodNumber;
    }

    public String getFoodRegistrant() {
        return foodRegistrant;
    }

    public void setFoodRegistrant(String foodRegistrant) {
        this.foodRegistrant = foodRegistrant;
    }

    public String getFoodDate() {
        return foodDate;
    }

    public void setFoodDate(String foodDate) {
        this.foodDate = foodDate;
    }
}
