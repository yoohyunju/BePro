package com.example.bepro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bepro.fridge_setting.FridgeMemberActivity;
import com.example.bepro.home.FoodItems;
import com.example.bepro.home.HomeActivity;
import com.example.bepro.home.SelfAddItemAdapter;
import com.example.bepro.login.snsRequest;
import com.example.bepro.my_page.MyPageActivity;
import com.example.bepro.notice.NoticeActivity;
import com.example.bepro.recipe.RecipeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager; //??? fragment?????? ????????? ??????, ??????, ???????????? ??? ????????? ???????????? ?????????
    private FragmentTransaction transaction; //fragment ????????? ?????? ????????????(????????????)

    private HomeActivity mHome;
    private RecipeActivity mRecipe;
    private MyPageActivity mMyPage;
    private NoticeActivity mNotice;
    private BottomNavigationView mNavView;

    private LinearLayout mFridgeListOpenBtn, mAddFridgeBtn, mSelfAddBtn, mFridgeSettingBtn, m_btnOCR, fridgeInviteCodeAddBtn;
    private Dialog mAddItemDialog, mFridgeListDialog, mFridgeAddDialog, mSelfAddDialog, mFridgeInviteAddDialog;
    private Button mAddCancelBtn, mFridgeListCancelBtn, mFridgeNameAddBtn, mFridgeAddCancelBtn,
            mSelfAddCancelBtn, mSelfAddConfirmBtn, mSelfItemAddBtn, mFridgeInviteCancelBtn, mFridgeInviteCodeAddBtn;

    private CardView mSelfAddCardView;
    ArrayList<FoodItems> foodItems = new ArrayList<>();
    ArrayList<String> fridgeIdxItems = new ArrayList<>();
    ArrayList<FridgeData> fridgeSpinnerItems = new ArrayList<>();
    FridgeData fridgeData = new FridgeData();

    //JSON DATA ????????? ??????
    String friIdx, foodName, foodNum, foodExp, foodRegistrant, text, authority;
    public ArrayList<String> OCRText = new ArrayList<>();

    public String image, email, type, fridgeName, userIdx;
    ArrayList<Integer> fridgeIdx = new ArrayList<Integer>();
    public TextView selected;
    EditText addFridge;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FridgeAdapter fridgeAdapter;
    UserData user = new UserData(); //user ??????
    public FridgeSettingData settingData = new FridgeSettingData();

    Bitmap testImage;
    InputImage inputImage;
    File file;
    Uri uri;
    private static final int ALBUM_RESULT_CODE = 0; //?????? ??????
    private static final int CAMERA_RESULT_CODE = 101; //?????? ??????

    int fridgePosition=0;

    Intent fridgeIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavView = findViewById(R.id.navigation);
        mNavView.setOnItemSelectedListener(new ItemSelectedListener()); //BottomNavigationView??? ????????? ????????? ??????

        fridgeIntent = new Intent(getApplicationContext(), FridgeMemberActivity.class);

        //????????? ????????? ????????????
        Intent intent = getIntent();
        image = intent.getStringExtra("userImage");
        email = intent.getStringExtra("userEmail");
        type = intent.getStringExtra("userType");


        if(image != null)
            //SNS ?????????
            user.setImage(image);
        user.setEmail(email);
        user.setType(type);

        getUser(); //????????? ?????? ??????
        //System.out.println("?????????????????? ????????? ?????????: "+ fridgeData.getFriIdx());

        //fragment ?????? ??????
        mHome = new HomeActivity();
        mRecipe = new RecipeActivity();
        mMyPage = new MyPageActivity();
        mNotice = new NoticeActivity();

        //dialog ?????????
        mAddItemDialog = new Dialog(MainActivity.this);
        mAddItemDialog.setContentView(R.layout.add_item_popup); //?????? ?????? ?????? xml ??????

        mFridgeListDialog = new Dialog(MainActivity.this);
        mFridgeListDialog.setContentView(R.layout.fridge_list_popup); //????????? ????????? ?????? xml ??????

        mFridgeAddDialog = new Dialog(MainActivity.this);
        mFridgeAddDialog.setContentView(R.layout.fridge_add_popup); //????????? ?????? ?????? xml ??????

        mSelfAddDialog = new Dialog(MainActivity.this);
        mSelfAddDialog.setContentView(R.layout.self_item_add_popup); //?????? ?????? ?????? ?????? xml ??????

        mFridgeInviteAddDialog = new Dialog(MainActivity.this);
        mFridgeInviteAddDialog.setContentView(R.layout.fridge_invite_popup); //?????? ?????? ?????? ?????? xml ??????

        mFridgeListOpenBtn = findViewById(R.id.fridgeListOpenBtn);
        mFridgeListOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFridgeListDialog(); /*????????? ????????? ?????? ?????? ??????*/
            }
        });

        //????????? ?????????
        fridgeAdapter = new FridgeAdapter();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = mFridgeListDialog.findViewById(R.id.FridgeRc);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(fridgeAdapter);

        fridgeAdapter.setOnFridgeClickListener(new OnFridgeClickListener() {
            @Override
            public void onFridgeClick(FridgeAdapter.ViewHolder holder, View view, int position) {
                //????????? ?????? ?????? ?????? ????????????
                //FridgeData name = fridgeAdapter.getItem(position);
                fridgeData = fridgeAdapter.getItem(position);
                selected = findViewById(R.id.myFridge);
                selected.setText(fridgeData.getFriId());

                Bundle bundle = new Bundle();
                bundle.putSerializable("fridgeData",fridgeAdapter.getItem(position));
                mHome.setArguments(bundle);
                mHome.setHomeData();

                authority = fridgeData.getFriSetAuthority();

                fridgeIntent.putExtra("fridgeData",fridgeAdapter.getItem(position));
                mFridgeListDialog.cancel();
            }
        });


        //????????? ?????? ??????
        mFridgeSettingBtn = findViewById(R.id.fridgeSettingBtn);
        mFridgeSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(fridgeIntent);
                finish();
            }
        });

        fragmentManager = getSupportFragmentManager();

        transaction = fragmentManager.beginTransaction(); //???????????? ??????
        transaction.replace(R.id.frameLayout, mHome).commitAllowingStateLoss();

    }


    //BottomNavigationView ????????? ????????? ??????
    private class ItemSelectedListener implements NavigationBarView.OnItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.home:
                    item.setChecked(true);
                    transaction.replace(R.id.frameLayout, mHome).commitAllowingStateLoss();
                    break;

                case R.id.recipe:
                    item.setChecked(true);
                    transaction.replace(R.id.frameLayout, mRecipe).commitAllowingStateLoss();
                    break;

                case R.id.add:
                    item.setChecked(true);

                    if(authority.equals("guest")){
                        Toast.makeText(MainActivity.this, "?????? ?????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                    }else{
                        showItemAddDialog(); //Dialog ?????? ??????
                    }
                    break;

                case R.id.myPage:
                    item.setChecked(true);
                    //?????? ????????? ?????????
                    mMyPage.user = user;
                    //mMyPage.fridgeAdapter = fridgeAdapter;

                    transaction.replace(R.id.frameLayout, mMyPage).commitAllowingStateLoss();
                    break;

                case R.id.notice:
                    item.setChecked(true);
                    transaction.replace(R.id.frameLayout, mNotice).commitAllowingStateLoss();
                    break;
            }
            return false;
        }

    }

    //???????????? ?????? ????????????
    @Override
    public void onBackPressed(){

    }

    //?????? ?????? ?????????
    public void showItemAddDialog(){

       //????????? ????????? ??????
        WindowManager.LayoutParams params = mAddItemDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        mAddItemDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //?????? ?????????????????? ???????????? ??????
        mAddItemDialog.setCanceledOnTouchOutside(false); //??? ?????? ?????? ?????? ?????? ?????? ??????

        mAddItemDialog.show(); //Dialog ?????????

        m_btnOCR = mAddItemDialog.findViewById(R.id.btn_OCR);
        m_btnOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ocr ?????? (text ?????? ?????? - log ?????? (cropper ?????? ??? ?????? ??????))
                /*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("????????? ??????");
                builder.setMessage("????????? ????????? ????????? ??????????????????!");

                builder.setPositiveButton("???????????? ??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takePhoto();
                    }
                });
                builder.setNegativeButton("???????????? ????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, ALBUM_RESULT_CODE);
                    }
                });
                builder.setCancelable(true);
                builder.create().show();*/
                //????????? ????????? ????????? ?????? ????????? ????????? ?????? (?????????, ?????????, ?????? ????????? ???????????? ?????????)
                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
            }
        });

        //?????? ?????? ?????? ??????
        mAddCancelBtn = mAddItemDialog.findViewById(R.id.addCancelBtn);
        mAddCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddItemDialog.dismiss(); //Dialog ??????
            }
        });

        //?????? ???????????? ??????
        mSelfAddBtn = mAddItemDialog.findViewById(R.id.selfAddBtn);
        mSelfAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddItemDialog.dismiss(); //?????? ??????????????? ??????
                showSelfAddDialog();
            }
        });

    }

    //?????? ?????? ???????????? ??????
    public void showSelfAddDialog(){

        //????????? ????????? ??????
        WindowManager.LayoutParams params = mSelfAddDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        //mSelfAddDialog.getWindow().setLayout(1000, 1500); //?????? ??????

        mSelfAddDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //?????? ?????????????????? ???????????? ??????
        mSelfAddDialog.setCanceledOnTouchOutside(false); //??? ?????? ?????? ?????? ?????? ?????? ??????

        mSelfAddDialog.show();

        //????????? ?????? ?????????
        Spinner fridgeSpinner = mSelfAddDialog.findViewById(R.id.fridgeSelectSpinner);

        ArrayAdapter<FridgeData> mAdapter = new ArrayAdapter<FridgeData>(
                this, android.R.layout.simple_spinner_item, fridgeSpinnerItems);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fridgeSpinner.setAdapter(mAdapter);

        fridgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fridgeData = (FridgeData) fridgeSpinner.getItemAtPosition(position);
                //Log.i("????????? ????????? ","????????? ?????????: " + fridgeData.getFriIdx() + "????????? ???: " + fridgeData.getFriId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //????????????????????? ???????????? ????????? ??????
        RecyclerView recyclerView = mSelfAddDialog.findViewById(R.id.addFoodRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        SelfAddItemAdapter adapter = new SelfAddItemAdapter();
        recyclerView.setAdapter(adapter);

        //OCR ?????? ????????? ???????????? ??????
        if(OCRText.isEmpty()){ //????????? ?????? ???????????? ???????????? ??????
            System.out.println("????????? ?????? ?????? ???????????? ????????????.");
        } else{ //????????? ?????? ???????????? ??????
            String foodItem = "";

            for(int i=0; i < OCRText.size(); i++){
                if(OCRText.get(i).isEmpty()){ //?????? null ?????????
                    continue;
                }else {
                    foodItem = OCRText.get(i);
                    //System.out.println("?????? ?????? ?????????: "+ foodItem);

                    adapter.addItem(new FoodItems(foodItem)); //????????? ??????
                    adapter.notifyDataSetChanged();

                }
                //System.out.println("????????? ????????? ??????: "+ adapter.getItemCount());

            }

        }

        //?????? ??????
        mSelfAddCancelBtn = mSelfAddDialog.findViewById(R.id.selfAddCancelBtn);
        mSelfAddCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelfAddDialog.dismiss();
                showItemAddDialog(); //?????? ??????????????? ?????????
            }
        });

        //?????? ???????????? ??????
        mSelfItemAddBtn = mSelfAddDialog.findViewById(R.id.selfItemAddBtn);
        mSelfItemAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??? ????????? ??????
                adapter.addItem(new FoodItems("",0,""));
                adapter.notifyDataSetChanged();

            }
        });

        //?????? ?????? ?????? ??????
        mSelfAddConfirmBtn = mSelfAddDialog.findViewById(R.id.selfAddConfirmBtn);
        mSelfAddConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(adapter.getItemCount() >= 1){ //?????? ???????????? ?????? ?????? ?????? ???
                    for(int i=0; i < adapter.getItemCount(); i++){ //????????? ???????????? ??????
                        adapter.setPosition(i);

                        //System.out.println("????????? ?????????: "+ friIdx + " ?????????: " + foodRegistrant);
                        EditText mFoodName = recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.editAddFoodName);
                        EditText mFoodTotal = recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.editFoodTotalCount);
                        TextView mFoodExpDate = recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.selfAddFoodExpDate);

                        friIdx = String.valueOf(fridgeData.getFriIdx()); //????????? ?????????
                        foodName = mFoodName.getText().toString(); //????????? ?????? ?????????
                        foodNum = mFoodTotal.getText().toString(); //????????? ?????? ??????
                        foodExp = mFoodExpDate.getText().toString(); //????????? ?????? ?????? ??????
                        foodRegistrant = user.getNickname(); //?????????

                        if(TextUtils.isEmpty(foodName) != true && TextUtils.isEmpty(foodNum)!= true && TextUtils.isEmpty(foodExp) != true){ //???????????? ?????? ???????????? ?????? ???

                            //bundle??? data ????????? (?????? ?????? ??? ?????? UI??? ????????? ?????? ??????)
                            Bundle bundle = new Bundle();
                            bundle.putString("friIdx", friIdx);
                            bundle.putString("foodName", foodName);
                            bundle.putString("foodNum", foodNum);
                            bundle.putString("foodExp", foodExp);

                            mHome.setArguments(bundle);
                            try {
                                mHome.selfAddItemFunc();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //?????? ?????? ?????? ?????? ?????? ??????
                            selfFoodItemInsert(friIdx, foodName, foodNum, foodExp, foodRegistrant);

                            if(i == adapter.getItemCount() - 1){ //????????? ??? ?????? ???????????? ????????? ??????
                                mSelfAddDialog.dismiss();
                                mAddItemDialog.dismiss();

                                mHome.foodAdapter.notifyDataSetChanged(); // UI ?????? ??????
                                //home.foodItemSetting(); //DB ?????? ????????? ????????? TODO: null ????????? ??? ??????
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "??? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //????????? ????????? ?????????
    public void showFridgeListDialog(){

        //????????? ????????? ??????
        WindowManager.LayoutParams params = mFridgeListDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        mFridgeListDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFridgeListDialog.setCanceledOnTouchOutside(false);

        mFridgeListDialog.show(); //Dialog ?????????
        Log.i("test","!!! == ????????? ????????? ?????? ==!!!"); //PLUS:??????
        for(int i=0;i<fridgeAdapter.getItemCount();i++){
            Log.i ("test","????????? ????????? : "+fridgeAdapter.getItem(i));
        }
        mFridgeListCancelBtn = mFridgeListDialog.findViewById(R.id.fridgeListCancelBtn); //????????? ????????? ?????? ??????
        mFridgeListCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFridgeListDialog.dismiss(); //Dialog ??????
            }
        });

        mAddFridgeBtn = mFridgeListDialog.findViewById(R.id.addFridgeBtn); //????????? ?????? ??????
        mAddFridgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFridgeListDialog.dismiss();
                showFridgeAddDialog();
            }
        });

    }

    //????????? ???????????? ?????????
    public void showFridgeAddDialog(){
        addFridge = mFridgeAddDialog.findViewById(R.id.fridgeName);
        addFridge.setText(null);

        //????????? ????????? ??????
        WindowManager.LayoutParams params = mFridgeAddDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        mFridgeAddDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFridgeAddDialog.setCanceledOnTouchOutside(false);

        mFridgeAddDialog.show();

        //????????? ???????????? ?????? TODO
        fridgeInviteCodeAddBtn = mFridgeAddDialog.findViewById(R.id.fridgeInviteCodeAddBtn);
        fridgeInviteCodeAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????? ????????? ??????
                WindowManager.LayoutParams params = mFridgeInviteAddDialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;

                mFridgeInviteAddDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mFridgeInviteAddDialog.setCanceledOnTouchOutside(false);

                mFridgeInviteAddDialog.show();

                mFridgeInviteCancelBtn = mFridgeInviteAddDialog.findViewById(R.id.fridgeInviteCancelBtn);
                mFridgeInviteCodeAddBtn = mFridgeInviteAddDialog.findViewById(R.id.fridgeInviteCodeAddBtn);

                //????????? ?????? ?????? ?????? ??????
                mFridgeInviteCancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFridgeInviteAddDialog.dismiss();
                    }
                });

                //????????? ?????? ?????? ?????? ??????
                mFridgeInviteCodeAddBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFridge = mFridgeInviteAddDialog.findViewById(R.id.fridgeName);
                        String code = addFridge.getText().toString();
                        FridgeCode fridgeCode = new FridgeCode();

                        String url= null;
                        try {
                            url = "http://3.37.119.236/fridgeSet/addFridgeUser.php?friIdx="+fridgeCode.getFriIdx(code)+"&userIdx="+user.getIndex();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"?????????????????? ??????"+e,Toast.LENGTH_SHORT).show();
                        }
                        RequestQueue requestQueue;
                        StringRequest request = new StringRequest(
                                Request.Method.GET,
                                url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("success")) {
                                            Toast.makeText(getApplicationContext(), "???????????? ?????????????????????." , Toast.LENGTH_SHORT).show();
                                            mFridgeInviteAddDialog.dismiss();
                                            getMyFridgeAndShowList(userIdx);
                                        }
                                        else if(response.equals("overlap")){
                                            Toast.makeText(getApplicationContext(), "?????? ?????????????????? ??????????????????." , Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Log.i("test", "????????? ????????? ??????????????????. ????????? ?????? ??????????????????.");
                                            Toast.makeText(getApplicationContext(),"????????? ????????? ??????????????????. ????????? ?????? ??????????????????.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i("test","????????? ??????????????? ??????!"+error);
                                    }
                                }
                        );
                        request.setShouldCache(false); //?????? ?????? ????????? ?????? ???????????? ????????? ????????????.
                        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                                20000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        requestQueue = Volley.newRequestQueue(MainActivity.this); // requestQueue ????????? ??????
                        requestQueue.add(request);
                        Log.i("test","????????? ????????? ?????? ????????? ????????????. : "+url);

                    }
                });



            }
        });

        //????????? ???????????? ?????? ??????
        mFridgeNameAddBtn = mFridgeAddDialog.findViewById(R.id.fridgeNameAddBtn);
        mFridgeNameAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addFridgeName = addFridge.getText().toString();
                Log.i("test","?????? ?????? : " + addFridgeName);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            getMyFridgeAndShowList(userIdx);
                        }catch (Exception e){

                        }
                    }
                };
                AddFridgeNameRequest addFridgeNameRequest = new AddFridgeNameRequest(addFridgeName, userIdx, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(addFridgeNameRequest);
            }
        });

        //????????? ???????????? ?????? ??????
        mFridgeAddCancelBtn = mFridgeAddDialog.findViewById(R.id.fridgeAddCancelBtn);
        mFridgeAddCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFridgeAddDialog.dismiss();
                showFridgeListDialog(); //?????? ??????????????? ?????????

            }
        });
    }

    /*????????? ???????????? ????????? ????????? ?????? ??????
    public void getFridgeName(String friIdx){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    fridgeData.setFriId(jsonObject.getString("friName")); //???????????? ???????????? ????????? ??????

                }catch (Exception e){

                }
            }
        };
        FridgeNameRequest getFridgeName = new FridgeNameRequest(friIdx, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(getFridgeName);
    }
     */

    public void getMyFridge(String userIDX){
        String URL = "http://3.37.119.236:80/fridgeSet/fridge.php?userIDX="+userIDX;
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(
                Request.Method.POST,
                URL,
                null,
                new Response.Listener<JSONArray>() { //???????????? ???????????? ??? ???????????? ??????
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            FridgeCode fridgeCode = new FridgeCode();
                            fridgeAdapter.itemClear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                fridgeData = new FridgeData(
                                        jsonObject.getInt("friIdx"),
                                        jsonObject.getString("friSetAuthority"),
                                        jsonObject.getString("friId"),
                                        fridgeCode.getCode(jsonObject.getInt("friIdx"))
                                );

                                if(i == 0) { //?????? ?????? ????????? ?????????

                                    selected = findViewById(R.id.myFridge);
                                    selected.setText(fridgeData.getFriId()); //????????? ?????? ??????

                                    System.out.println("????????? ????????????: "+ fridgeData.getFriId());

                                    //home?????? ????????? ?????? ??????
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("fridgeData", fridgeData);
                                    mHome.setArguments(bundle);
                                    mHome.setHomeData();

                                    authority = fridgeData.getFriSetAuthority();
                                }

                                fridgeAdapter.addItem(fridgeData); //????????? ?????? ????????? ??????

                                //????????? ????????? ??????
                                fridgeSpinnerItems.add(new FridgeData(fridgeData.getFriIdx(), fridgeData.getFriId()));
                                fridgeIntent.putExtra("fridgeData",fridgeAdapter.getItem(0));
                                //fridgeIdxItems.add(String.valueOf(fridgeData.getFriIdx()));

                                mHome.setHomeData();

                                Log.i("test","?????? : "+fridgeData.toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(response.length() == 0){ //?????? ?????? ???????????? ?????????
                            addBasicFridge(); //?????? ????????? ?????? ?????? ??????

                        }
                    }
                }, new Response.ErrorListener() { //???????????? ???????????? ??? ???????????? ??????
                @Override
                public void onErrorResponse(VolleyError error) {
                    String TAG = "";
                    Log.e(TAG, "Error " + error.getMessage());
                }
            });
            RequestQueue requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);
    }

    public void getMyFridgeAndShowList(String userIDX){
        mFridgeAddDialog.dismiss();
        String URL = "http://3.37.119.236:80/fridgeSet/fridge.php?userIDX="+userIDX;
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(
                Request.Method.POST,
                URL,
                null,
                new Response.Listener<JSONArray>() { //???????????? ???????????? ??? ???????????? ??????
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            FridgeCode fridgeCode = new FridgeCode();
                            fridgeAdapter.itemClear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                fridgeData = new FridgeData(
                                        jsonObject.getInt("friIdx"),
                                        jsonObject.getString("friSetAuthority"),
                                        jsonObject.getString("friId"),
                                        fridgeCode.getCode(jsonObject.getInt("friIdx"))
                                );
                                fridgeAdapter.addItem(fridgeData); //????????? ?????? ????????? ??????
                                Log.i("test","?????? : "+fridgeData.toString());
                                showFridgeListDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() { //???????????? ???????????? ??? ???????????? ??????
            @Override
            public void onErrorResponse(VolleyError error) {
                String TAG = "";
                Log.e(TAG, "Error " + error.getMessage());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void getUser(){
        //DB?????? ?????? ????????? ????????????
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //false??? nick??? ????????? ???
                    user.setNickname(jsonObject.getString(("userNickname")));
                    user.setPassword(jsonObject.getString("userPassword"));
                    user.setDbImage(jsonObject.getString("userImg"));
                    user.setIndex(jsonObject.getString("userIDX"));
                    userIdx = jsonObject.getString("userIDX");

                    getMyFridge(user.getIndex());
                    fridgeIntent.putExtra("userData",(Serializable)user);
                    Log.i("test","getUser()??? ?????? ??? = "+user.toString()); //PLUS
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        snsRequest snsRequest = new snsRequest(type, email, responseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(snsRequest);
    }

    //?????? ????????? ??????
    public void addBasicFridge(){
        // ?????? ?????????
       // if(fridgeData.getFriIdx() == 0){ //????????? ????????? ???????????? ????????? ?????? (??? ????????? ????????? x)
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("?????? ????????? ??????").setMessage("?????? ???????????? ???????????????.");

            builder.setPositiveButton("??????", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    //Toast.makeText(getApplicationContext(), "?????? ????????? ??????", Toast.LENGTH_SHORT).show();
                    String addFridgeName = "?????? ?????????";

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //?????? ????????? ?????????(??????, ????????????, ????????? ?????????) ??????
                            selected = findViewById(R.id.myFridge);
                            selected.setText(fridgeData.getFriId()); //?????? ????????? ?????? ??????

                            getMyFridge(userIdx); //??? ????????? ????????? ????????? ??????

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("fridgeData", fridgeData);
                            mHome.setArguments(bundle);
                            mHome.setHomeData();

                            authority = fridgeData.getFriSetAuthority();
                        }
                    };
                    AddFridgeNameRequest addFridgeNameRequest = new AddFridgeNameRequest(addFridgeName, userIdx, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(addFridgeNameRequest);


                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

    }


    //?????? data ??????
    public void selfFoodItemInsert(String friIdx, String foodName, String foodNum, String foodExp, String foodRegistrant){
        String URL = "http://3.37.119.236:80/food/insertFood.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                String TAG = "";
                Log.e(TAG, "foodItemInsert() onErrorResponse ?????? ?????????: " + String.valueOf(error));
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> requestedParams = new HashMap<>();
                requestedParams.put("friIdx", friIdx);
                requestedParams.put("foodName", foodName);
                requestedParams.put("foodNum", foodNum);
                requestedParams.put("foodExp", foodExp);
                requestedParams.put("foodRegistrant", foodRegistrant);

                return requestedParams;
            }

        };

        //?????? ?????? ????????? ??????????????? ????????? ?????? ??????
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        //???????????? ?????? ?????? ??????
        requestQueue.add(stringRequest);

    }

    //????????? ????????? ????????? ?????? OCR ??????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(resultUri));
                        inputImage = InputImage.fromBitmap(bitmap, 0);
                        getText(inputImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
        }
    }

    //text?????? ?????? ????????????
    public static String getOnlyKor(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null && str.length()!=0){
            Pattern p = Pattern.compile("[???-???]");
            Matcher m = p.matcher(str);
            while (m.find()) {
                sb.append(m.group());
            }
        }
        return sb.toString();
    }

    public void getText(InputImage image){
        System.out.println("????????? ?????? ??????");
        TextRecognizer recognizer = TextRecognition.getClient(new KoreanTextRecognizerOptions.Builder().build());
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(@NonNull @NotNull Text visionText) {
                                for(Text.TextBlock block:visionText.getTextBlocks()){
                                    text = block.getText();
                                    for(Text.Line line : block.getLines()) {
                                        String lineText = line.getText();
                                        System.out.println(getOnlyKor(lineText));
                                        OCRText.add(getOnlyKor(lineText));
                                    }
                                }
                                mAddItemDialog.dismiss(); //?????? ????????? ??????
                                showSelfAddDialog(); //?????? ?????? ?????? ?????? ??????
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                System.out.println("????????? ?????? ??????");
                            }
                        });
    }
    /*public void onSelectImageClick(View view) {
        //File mFile = new File(getActivity().getExternalFilesDir(null), "pic.jpg");
        //String filePath = file.getPath();
        //CropImage.activity().start(this);
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
    }*/

}