package com.example.bepro.fridge_setting;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.bepro.FridgeData;
import com.example.bepro.R;
import com.example.bepro.UserData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import android.app.Dialog;
import android.widget.Toast;

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
    FridgeDialog fridgeDialog;

    //fridgeCodeDialog
    Dialog fridgeCodeDialog;

    //기타
    EditText fridgeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge_setting);

        Intent intent = getIntent();
        FridgeData fridgeData = (FridgeData)intent.getSerializableExtra("fridgeData");
        Log.i("test","fridgeData = "+fridgeData.toString());

        parseJSON=new ParseJSON(getApplicationContext());
        sendRequestImp = new SendRequestImp(getApplicationContext());

        fridgeDialog = new FridgeDialog(FridgeMemberActivity.this);
        fridgeCodeDialog = new Dialog(FridgeMemberActivity.this);
        fridgeCodeDialog.setContentView(R.layout.fridge_code_popup);

        ////////////RequestQueue 생성
        if(requestQueue != null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        ////////////냉장고 회원 리스트
        listView = (ListView)findViewById(R.id.friMemberListView); //리스트뷰 참조
        fridgeListCount = (TextView)findViewById(R.id.fridgeListCount);
        friMemberListViewAdapter = new FridgeMemberListViewAdapter(sendRequestImp,FridgeMemberActivity.this,fridgeListCount,fridgeData.getFriSetAuthority()); //Adapter 생성

        if(fridgeData.getFriSetAuthority().equals("admin")) {
            ////////////애니메이션 설정
            LeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_left); //anim 폴더의 애니메이션을 가져와서 준비
            RightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_right); //anim 폴더의 애니메이션을 가져와서 준비
            //애니메이션 객체 전달
            friMemberListViewAdapter.setLeftAnim(LeftAnim);
            friMemberListViewAdapter.setRightAnim(RightAnim);
        }


        ////////////데이터 요청
        sendRequest();

        ////////////냉장고명 초기설정
        fridgeName = (EditText)findViewById(R.id.refName);
        fridgeName.setText(fridgeData.getFriId());

        ////////////냉장고 탈퇴
        Button fridgeQuit = (Button)findViewById(R.id.fridgeQuit);
        fridgeQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fridgeDialog.showDialog("냉장고를 탈퇴하시겠습니까?","정말로 냉장고를 탈퇴하시겠습니까?","냉장고를 탈퇴하였습니다.");
                //sendRequestImp.deleteFriUser(1,1); //변경 필요 : 현재 회원.
            }
        });

        ////////////냉장고 삭제 & 냉장고명 변경
        if(fridgeData.getFriSetAuthority().equals("admin")) {
            fridgeName.setEnabled(true);
            Button fridgeNameChangeBtn = (Button)findViewById(R.id.fridgeNameChangeBtn);
            fridgeNameChangeBtn.setVisibility(View.VISIBLE);
            Button fridgeDelete = (Button) findViewById(R.id.fridgeDelete);
            fridgeDelete.setVisibility(View.VISIBLE);
            fridgeDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fridgeDialog.showDialog("냉장고를 삭제하시겠습니까?", "정말로 냉장고를 삭제하시겠습니까?\n30일 후 완전히 삭제가 됩니다.", "냉장고를 삭제하였습니다.");
                    //sendRequestImp.deleteFri(4); //변경 필요 : 현재 냉장고 인덱스 가져와서 넣기.
                }
            });
        }

        ////////////냉장고 멤버 추가
        Button fridgeAddUser = (Button)findViewById(R.id.fridgeCodeBtn);
        fridgeAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("test","클릭했어");//WHY
                showFridgeCodeDialog();
            }
        });
    }

    public void sendRequest(){
        String url="http://3.37.119.236:80/fridgeSet/selectFriSet.php?friIdx=4";
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

    public void showFridgeCodeDialog(){
        //팝업창 사이즈 조절
        WindowManager.LayoutParams params = fridgeCodeDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        fridgeCodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        fridgeCodeDialog.setCanceledOnTouchOutside(false);

        fridgeCodeDialog.show(); //Dialog 띄우기

        Button fridgeCodeBtn = fridgeCodeDialog.findViewById(R.id.fridgeCodeOK); //냉장고 리스트 취소 버튼
        fridgeCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fridgeCodeDialog.dismiss(); //Dialog 닫기
            }
        });

        TextView fridgeCodeText = fridgeCodeDialog.findViewById(R.id.fridgeCodeText);
        fridgeCodeText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                    String code = fridgeCodeText.getText().toString();
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("CODE",code); //클립보드에 CODE라는 이름표로 code 값을 복사하여 저장
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(getApplicationContext(),"코드가 복사되었습니다.",Toast.LENGTH_SHORT).show();
                return true;
            }
        });


    }
}
