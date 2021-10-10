package com.example.bepro;

import java.io.Serializable;

public class FridgeData implements Serializable{

    private int friIdx; //냉장고 인덱스
    private String friSetAuthority; //내가 가진 냉장고 권한

    private String friId; //냉장고 이름
    private String friCode; //냉장고 초대 코드

    public FridgeData(){}

    public FridgeData(int friIdx, String friSetAuthority, String friId, String friCode) {
        this.friIdx = friIdx;
        this.friSetAuthority = friSetAuthority;
        this.friId = friId;
        this.friCode = friCode;
    }

    public FridgeData(int friIdx, String friId) { //key, value
        this.friIdx = friIdx;
        this.friId = friId;
    }

    @Override
    public String toString() {
        return friId;
    }

    public int getFriIdx() {
        return friIdx;
    }

    public void setFriIdx(int friIdx) {
        this.friIdx = friIdx;
    }

    public String getFriSetAuthority() {
        return friSetAuthority;
    }

    public void setFriSetAuthority(String friSetAuthority) {
        this.friSetAuthority = friSetAuthority;
    }

    public String getFriId() {
        return friId;
    }

    public void setFriId(String friId) {
        this.friId = friId;
    }

    public String getFriCode() {
        return friCode;
    }

    public void setFriCode(String friCode) {
        this.friCode = friCode;
    }
}
