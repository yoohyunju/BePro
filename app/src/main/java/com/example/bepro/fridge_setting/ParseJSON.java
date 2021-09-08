package com.example.bepro.fridge_setting;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.bepro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ParseJSON {
    Context context;
    ParseJSON(Context context){
        this.context = context;
    }

    public List<FridgeMember> getListFridgeMember(JSONArray jsonArray) throws JSONException {
        List<FridgeMember> list = new ArrayList<>();
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FridgeMember member = new FridgeMember();
                member.setUserIdx(Integer.parseInt(jsonObject.getString("userIdx")));
                member.setUserNickname(jsonObject.getString("userNickname"));
                member.setFriIdx(Integer.parseInt(jsonObject.getString("friIdx")));
                member.setFriSetIdx(Integer.parseInt(jsonObject.getString("friSetIdx")));
                member.setFriSetAuthority(jsonObject.getString("friSetAuthority"));
                member.setUserImg(ContextCompat.getDrawable(context, R.drawable.ic_baseline_person_outline_24));
                list.add(member);
            }
        }catch (JSONException e){
            e.printStackTrace();
            Log.i("test","json error");
        }
        return list;
    }
}
