package com.example.bepro.fridge_setting;

public interface SendRequest {
    public void getFriSetAll();
    public void deleteFriUser(long userIdx, long friIdx);
    public void deleteFri(long friIdx);
    public void setFriAuthority(long friSetIdx, String friSetAuthority);
}
