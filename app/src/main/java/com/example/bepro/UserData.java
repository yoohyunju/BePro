package com.example.bepro;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable {
    private String password;
    private String type;
    private String nickname;
    private String email;
    private String image;
    private String dbImage;
    private String date;

    @Override
    public String toString() {
        return "UserData{" +
                "password='" + password + '\'' +
                ", type='" + type + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", dbImage='" + dbImage + '\'' +
                ", date='" + date + '\'' +
                ", update='" + update + '\'' +
                ", authority='" + authority + '\'' +
                ", index='" + index + '\'' +
                ", myFridge=" + myFridge +
                '}';
    }

    private String update;
    private String authority;
    private String index;
    ArrayList<Integer> myFridge = new ArrayList<>();

    //내가 만든 냉장고
    public void MyFridge(int mine) { myFridge.add(mine);}

    public ArrayList<Integer> MyFridge() { return myFridge; }

    public void MyFridgeClear() { myFridge.clear();}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDbImage() {
        return dbImage;
    }

    public void setDbImage(String dbImage) {
        this.dbImage = dbImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
