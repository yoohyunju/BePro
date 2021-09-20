package com.example.bepro.fridge_setting;

import android.graphics.drawable.Drawable;

public class FridgeMember {
    Drawable userImg;
    String userNickname;
    String userAuthority;

    public Drawable getUserImg() {
        return userImg;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserAuthority() {
        return userAuthority;
    }

    public void setUserAuthority(String userAuthority) {
        this.userAuthority = userAuthority;
    }

    public void setUserImg(Drawable userImg) {
        this.userImg = userImg;
    }
}
