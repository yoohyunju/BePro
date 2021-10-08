package com.example.bepro.fridge_setting;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendRequestImp implements SendRequest{
    Context context;
    RequestQueue requestQueue;
    String url;
    Request request;

    ParseJSON parseJSON;

    SendRequestImp(Context context){
        this.context=context;
        if(requestQueue != null) {
            requestQueue = Volley.newRequestQueue(context);
        }
    }

    private void sendRequest(){
        request.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(context); // requestQueue 초기화 필수
        requestQueue.add(request);
        Log.i("test","요청을 보냈어요.");
    }

    @Override
    public void getFriSetAll(List<FridgeMember> fridgeMemberList){
        url="http://3.37.119.236:80/fridgeSet/selectFriSet.php?friIdx=4";
        request = new JsonArrayRequest( //지정된 URL에서 JSONObject의 응답 본문을 가져오기 위한 요청
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            fridgeMemberList.addAll(parseJSON.getListFridgeMember(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("test","에러"+error);
                    }
                }
        );
        sendRequest();
    }

    @Override
    public void deleteFriUser(long userIdx, long friIdx) {
        url = "http://3.37.119.236:80/fridgeSet/deleteFriSetUser.php?userIdx="+userIdx+"&friIdx="+friIdx;
        request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("test","delete:"+url);
                        if(response.equals("success"))
                            Log.i("test","delete 성공");
                        else
                            Log.i("test","delete 실패"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("test","delete 에러!"+error);
                    }
                }
        );
        sendRequest();
    }

    @Override
    public void deleteFri(long friIdx) {
        url = "http://3.37.119.236:80/fridgeSet/deleteFri.php?friIdx="+friIdx;
        request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("test","deleteFri:"+url);
                        if(response.equals("success"))
                            Log.i("test","deleteFri 성공");
                        else
                            Log.i("test","deleteFri 실패"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("test","deleteFri 에러!"+error);
                    }
                }
        );
        sendRequest();
    }

    @Override
    public void setFriAuthority(long friSetIdx,String friSetAuthority) {
        url = "http://3.37.119.236:80/fridgeSet/updateFriSet.php?friSetIdx="+friSetIdx+"&friSetAuthority="+friSetAuthority;
        request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("test","setFriAuhority:"+url);
                        if(response.equals("success"))
                            Log.i("test","setFriAuhority 성공");
                        else
                            Log.i("test","setFriAuhority 실패"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("test","setFriAuhority 에러!"+error);
                    }
                }
        );
        sendRequest();
    }

    @Override
    public void updateFriId(int friIdx, String friId) {
        url = "http://3.37.119.236:80/fridgeSet/updateFriName.php?friIdx="+friIdx+"&friId="+friId;
        request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("test","updateFriId:"+url);
                        if(response.equals("success")) {
                            Log.i("test", "setFriAuhority 성공");
                            Toast.makeText(context, "냉장고 이름이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.i("test", "setFriAuhority 실패" + response);
                            Toast.makeText(context, "냉장고 이름 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("test","setFriAuhority 에러!"+error);
                    }
                }
        );
        sendRequest();
    }


}
