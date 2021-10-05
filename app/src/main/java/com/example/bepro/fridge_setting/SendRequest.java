package com.example.bepro.fridge_setting;

import java.util.List;

public interface SendRequest {
    public void getFriSetAll(List<FridgeMember> fridgeMemberList);
    public void deleteFriUser(long userIdx, long friIdx);
    public void deleteFri(long friIdx);
    public void setFriAuthority(long friSetIdx, String friSetAuthority);
}
