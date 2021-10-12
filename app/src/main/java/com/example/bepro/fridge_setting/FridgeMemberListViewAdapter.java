package com.example.bepro.fridge_setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bepro.FridgeData;
import com.example.bepro.R;
import com.example.bepro.UserData;

import java.util.ArrayList;
import java.util.List;

public class FridgeMemberListViewAdapter extends BaseAdapter {
    FridgeMemberActivity fridgeMemberActivity;
    private List<FridgeMember> listViewItemList = new ArrayList<FridgeMember>();
    FridgeData fridgeData;
    UserData userData;
    //쿼리문
    SendRequestImp sendRequestImp;
    //alertDialog
    AlertDialog.Builder builder;

    FridgeMemberListViewAdapter(SendRequestImp sendRequestImp, Context fridgeContext, TextView fridgeListCount,FridgeData fridgeData,UserData userData){
        this.sendRequestImp=sendRequestImp;
        this.fridgeListCount = fridgeListCount;
        this.fridgeData = fridgeData;
        this.userData=userData;

        builder = new AlertDialog.Builder(fridgeContext);
    }

    public void setFridgeMemberActivity(FridgeMemberActivity fridgeMemberActivity) {
        this.fridgeMemberActivity = fridgeMemberActivity;
    }

    //이벤트 설정
    Animation leftAnim;
    Animation rightAnim;
    SlidingPageAnimationListener animationListener = new SlidingPageAnimationListener();

    TextView fridgeListCount;

    // Adapter에 사용되는 데이터의 개수를 리턴.
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("test","getView "+position+"번째");
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fridge_setting_member, parent, false);
        }

        /////////////////데이터 설정
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        FridgeMember listViewItem = listViewItemList.get(position);

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        LinearLayout userProfile = (LinearLayout) convertView.findViewById(R.id.userProfile);
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.userImg);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.userNickname);
        Switch userAuthority = (Switch) convertView.findViewById(R.id.userAuthority);
        TextView userAuthorityText = (TextView)convertView.findViewById(R.id.userAuthorityText);

        LinearLayout userSet = (LinearLayout) convertView.findViewById(R.id.userSet);
        Button userOut = (Button)convertView.findViewById(R.id.userOut);
        Button userAdmin = (Button)convertView.findViewById(R.id.userAdmin);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.getUserImg());
        titleTextView.setText(listViewItem.getUserNickname());
        if(listViewItem.getFriSetAuthority().equals("member")){
            userAuthority.setChecked(true);
        }

        if(fridgeData.getFriSetAuthority().equals("admin") && position!=0) {
            Log.i("test","amin if문 들어감");
            userAuthority.setVisibility(View.VISIBLE);
            userAuthorityText.setVisibility(View.GONE);
            /////////////////이벤트 설정
            //클릭했을 때 슬라이딩 이벤트 발생 (회원 정보)
            userProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                        Log.i("test","leftAnim="+leftAnim);
                    //                        Log.i("test",position+"번째 프로필 이벤트 발생, 클릭 객체= "+userProfile+"애니객체= "+userSet+"출력");
                    //열려있는 userSet이 있으면 닫는 과정 처리
                    if (animationListener.getUserSet() != null && animationListener.getUserSet().getVisibility() == View.VISIBLE) {
                        //                            Log.i("test","=========\n다름");
                        LinearLayout userSetBefore = animationListener.getUserSet();
                        userSetBefore.setVisibility(View.INVISIBLE);
                        userSetBefore.startAnimation(rightAnim);
                        //                            Log.i("test","rightAnim 실행함\n========= ");
                    }
                    animationListener.setUserSet(userSet); //현재 위치의 userSet의 설정을 변경하도록 이벤트 리스너의 참조값을 변경해줌
                    leftAnim.setAnimationListener(animationListener); //변경된 이벤트 리스너를 적용
                    userSet.startAnimation(leftAnim); //userSet이 왼쪽으로 밀려오는 애니메이션 시작

                }
            });
            //클릭했을 때 슬라이딩 이벤트 발생 (회원 설정)
            userSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //                        Log.i("test","rightAnim="+rightAnim);
                    //                        Log.i("test",position+"번째 설정 이벤트 발생, 클릭 객체= "+userProfile+"애니객체= "+userSet+"출력");
                    //닫는 과정 처리
                    userSet.setVisibility(View.INVISIBLE);
                    userSet.startAnimation(rightAnim);
                }
            });
            //클릭했을 때 권한 변경 이벤트 발생
            userAuthority.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Log.i("test", "스위치 이벤트 발생, member로 변경");
                        sendRequestImp.setFriAuthority(listViewItem.getFriSetIdx(), "member");
                        listViewItem.setFriSetAuthority("member");
                    } else {
                        Log.i("test", "스위치 이벤트 발생, guest로 변경");
                        sendRequestImp.setFriAuthority(listViewItem.getFriSetIdx(), "guest");
                        listViewItem.setFriSetAuthority("guest");
                    }
                }
            });
            //클릭했을 때 내보내기 이벤트 발생
            userOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setTitle("회원을 내보내시겠습니까?");
                    builder.setMessage(listViewItem.getUserNickname() + " 님을 정말로 내보내시겠습니까?\n");
                    builder.setPositiveButton("예",new DialogInterface.OnClickListener() { //사용자의 허락
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"내보냈습니다.",Toast.LENGTH_SHORT).show();
                            //DB 삭제 쿼리
                            sendRequestImp.deleteFriUser(listViewItem.getUserIdx(),listViewItem.getFriIdx());

                            //UI 다시 그리기
                            userSet.setVisibility(View.INVISIBLE);
                            listViewItemList.remove(position);
                            notifyDataSetChanged();
                            fridgeListCount.setText("냉장고 멤버 (" + getCount() + "명)");
                        }
                    });
                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
            });
            //클릭했을 때 권한 위임 이벤트 발생
            userAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.setTitle("권한을 위임하시겠습니까?");
                    builder.setMessage(listViewItem.getUserNickname() + " 님에게 냉장고 주인 권한을 위임하시겠습니까?\n");
                    builder.setPositiveButton("예",new DialogInterface.OnClickListener() { //사용자의 허락
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"위임하였습니다.",Toast.LENGTH_SHORT).show();

                            sendRequestImp.setFriAuthority(listViewItem.getFriSetIdx(),"admin");
                            sendRequestImp.setFriAuthority(listViewItemList.get(0).getFriSetIdx(),"member");
                            fridgeData.setFriSetAuthority("member");

                            Intent intent = ((Activity)context).getIntent();
                            ((Activity)context).finish(); //현재 액티비티 종료 실시
                            ((Activity)context).overridePendingTransition(0, 0); //효과 없애기
                            ((Activity)context).startActivity(intent); //현재 액티비티 재실행 실시
                            ((Activity)context).overridePendingTransition(0, 0); //효과 없애기

                            userSet.setVisibility(View.INVISIBLE);
                        }
                    });
                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
            });
        }
        else {
            Log.i("test","amin if문 안들어감");
            userAuthorityText.setText(listViewItem.getFriSetAuthority());
        }
        return convertView;
    }

    //아이템 데이터 추가를 위한 함수
    public void addItem(Drawable userImg, String userNickname) {
        FridgeMember item = new FridgeMember();

        item.setUserImg(userImg);
        item.setUserNickname(userNickname);

        listViewItemList.add(item);
    }

    public List<FridgeMember> getListViewItemList() {
        return listViewItemList;
    }

    public void setListViewItemList(List<FridgeMember> listViewItemList) {
        this.listViewItemList = listViewItemList;
    }

    //애니메이션 객체 설정
    public void setLeftAnim(Animation leftAnim) {
        this.leftAnim = leftAnim;
    }

    public void setRightAnim(Animation rightAnim) {
        this.rightAnim = rightAnim;
    }


    //애니메이션 발생 감지 클래스
    public class SlidingPageAnimationListener implements Animation.AnimationListener {
        LinearLayout userSet;

        public void setUserSet(LinearLayout userSet) {
            this.userSet = userSet;
        }
        public LinearLayout getUserSet() {
            return userSet;
        }

        @Override
        //애니메이션이 시작되기 전 호출
        public void onAnimationStart(Animation animation) {
            if(animation==leftAnim){
                userSet.setVisibility(View.VISIBLE);
                //Log.i("test","leftAnim입니다. 보이게 설정"+userSet);
                rightAnim.setAnimationListener(animationListener);
            }
        }

        @Override
        //애니메이션이 끝났을 때 호출
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        //애니메이션이 반복될 때 호출
        public void onAnimationRepeat(Animation animation) {

        }
        
    }

    //dialog
    public void showDialog(){

    }
}

