package com.example.bepro.fridge_setting;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class FridgeMember {
    long friIdx;

    long friSetIdx;
    String friSetAuthority;
    Date friSetUpdate;

    long userIdx;
    Drawable userImg;
    String userNickname;
    String userAuthority;

    @Override
    public String toString() {
        return "FridgeMember{" +
                "friIdx=" + friIdx +
                ", friSetIdx=" + friSetIdx +
                ", friSetAuthority='" + friSetAuthority + '\'' +
                ", friSetUpdate=" + friSetUpdate +
                ", userIdx=" + userIdx +
                ", userImg=" + userImg +
                ", userNickname='" + userNickname + '\'' +
                ", userAuthority='" + userAuthority + '\'' +
                '}';
    }

    public long getFriIdx() {
        return friIdx;
    }

    public void setFriIdx(long friIdx) {
        this.friIdx = friIdx;
    }

    public long getFriSetIdx() {
        return friSetIdx;
    }

    public void setFriSetIdx(long friSetIdx) {
        this.friSetIdx = friSetIdx;
    }

    public String getFriSetAuthority() {
        return friSetAuthority;
    }

    public void setFriSetAuthority(String friSetAuthority) {
        this.friSetAuthority = friSetAuthority;
    }

    public Date getFriSetUpdate() {
        return friSetUpdate;
    }

    public void setFriSetUpdate(Date friSetUpdate) {
        this.friSetUpdate = friSetUpdate;
    }

    public long getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(long userIdx) {
        this.userIdx = userIdx;
    }

    public Drawable getUserImg() {
        return userImg;
    }

    public void setUserImg(Drawable userImg) {
        this.userImg = userImg;
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
}
