package com.example.bepro.fridge_setting;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bepro.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


public class FridgeMemberActivity extends AppCompatActivity {

    //php에서 가져올...
    public static RequestQueue requestQueue;
    JSONArray FridgeMemberJSON;
    List<FridgeMember> fridgeMemberList;
    ParseJSON parseJSON;
    SendRequestImp sendRequestImp;

    //리스트뷰
    ListView listView;
    FridgeMemberListViewAdapter friMemberListViewAdapter;
    int listHeight=0;
    Animation LeftAnim;
    Animation RightAnim;
    TextView fridgeListCount;

    //alertDialog
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge_setting);

        parseJSON=new ParseJSON(getApplicationContext());
        sendRequestImp = new SendRequestImp(getApplicationContext());
        dialog = new Dialog(getApplicationContext());

        ////////////RequestQueue 생성
        if(requestQueue != null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        ////////////냉장고 회원 리스트
        listView = (ListView)findViewById(R.id.friMemberListView); //리스트뷰 참조
        fridgeListCount = (TextView)findViewById(R.id.fridgeListCount);
        friMemberListViewAdapter = new FridgeMemberListViewAdapter(sendRequestImp,dialog,fridgeListCount); //Adapter 생성


        ////////////애니메이션 설정
        LeftAnim = AnimationUtils.loadAnimation(this,R.anim.translate_left); //anim 폴더의 애니메이션을 가져와서 준비
        RightAnim = AnimationUtils.loadAnimation(this,R.anim.translate_right); //anim 폴더의 애니메이션을 가져와서 준비
        //애니메이션 객체 전달
        friMemberListViewAdapter.setLeftAnim(LeftAnim);
        friMemberListViewAdapter.setRightAnim(RightAnim);


        ////////////데이터 요청
        sendRequest();

        ////////////냉장고 탈퇴
        Button fridgeQuit = (Button)findViewById(R.id.fridgeQuit);
        fridgeQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestImp.deleteFriUser(1,1); //변경 필요 : 현재 회원.
            }
        });

        ////////////냉장고 삭제
        Button fridgeDelete = (Button)findViewById(R.id.fridgeDelete);
        fridgeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestImp.deleteFri(1); //변경 필요 : 현재 냉장고 인덱스 가져와서 넣기.
            }
        });

    }

    public void sendRequest(){
        String url="http://10.0.2.2/selectFriSet.php";
        JsonArrayRequest request = new JsonArrayRequest( //지정된 URL에서 JSONObject의 응답 본문을 가져오기 위한 요청
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("test", "결과는" + response);
                        FridgeMemberJSON = response;
                        try {
                            fridgeMemberList=parseJSON.getListFridgeMember(response);
                            friMemberListViewAdapter.setListViewItemList(fridgeMemberList);
                            ////////////고정된 리스트 설정 (리스트뷰 아이템의 크기를 측정하고 높이를 모두 합하여 ListView의 Height를 설정한다.)
                            for (int i=0;i<friMemberListViewAdapter.getCount();i++){
                                View listItem = friMemberListViewAdapter.getView(i,null,listView);
                                listItem.measure(0,0);
                                listHeight+=listItem.getMeasuredHeight();
                            }
                            ViewGroup.LayoutParams params = listView.getLayoutParams();
                            params.height = listHeight+(listView.getDividerHeight()+(friMemberListViewAdapter.getCount())-1);
                            listView.setLayoutParams(params);

                            fridgeListCount.setText("냉장고 멤버 ("+friMemberListViewAdapter.getCount()+"명)");
                            listView.setAdapter(friMemberListViewAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("test","json오류라능");
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
        request.setShouldCache(false); //이전 결과 있어도 새로 요청하여 응답을 보여준다.
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(this); // requestQueue 초기화 필수
        requestQueue.add(request);
        Log.i("test","요청을 보냈어요.");

    }

}
