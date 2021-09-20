package com.example.bepro;

//선택된 냉장고 세팅 데이터
public class FridgeSettingData {
    private int fridgeIndex;
    private String Authority;
    private int View;
    private int Category;
    private int Sort;
    private int Push;

    public int getFridgeIndex() {
        return fridgeIndex;
    }

    public void setFridgeIndex(int fridgeIndex) {
        this.fridgeIndex = fridgeIndex;
    }

    public String getAuthority() {
        return Authority;
    }

    public void setAuthority(String authority) {
        Authority = authority;
    }

    public int getView() {
        return View;
    }

    public void setView(int view) {
        View = view;
    }

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public int getPush() {
        return Push;
    }

    public void setPush(int push) {
        Push = push;
    }
}
