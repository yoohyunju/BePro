package com.example.bepro.fridge_setting;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.bepro.R;

public class FridgeMemberActivity extends AppCompatActivity {
    int listHeight=0;
    Animation LeftAnim;
    Animation RightAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge_setting);


        ////////////냉장고 회원 리스트
        ListView listView = (ListView)findViewById(R.id.friMemberListView); //리스트뷰 참조
        FridgeMemberListViewAdapter friMemberListViewAdapter = new FridgeMemberListViewAdapter(); //Adapter 생성

        ////////////애니메이션 설정
        LeftAnim = AnimationUtils.loadAnimation(this,R.anim.translate_left); //anim 폴더의 애니메이션을 가져와서 준비
        RightAnim = AnimationUtils.loadAnimation(this,R.anim.translate_right); //anim 폴더의 애니메이션을 가져와서 준비
        //애니메이션 객체 전달
        friMemberListViewAdapter.setLeftAnim(LeftAnim);
        friMemberListViewAdapter.setRightAnim(RightAnim);

        ////////////아이템 추가
        friMemberListViewAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.ic_baseline_person_outline_24),"다빈");
        friMemberListViewAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.ic_baseline_person_outline_24),"다빈");
        friMemberListViewAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.ic_baseline_person_outline_24),"다빈");
        friMemberListViewAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.ic_baseline_person_outline_24),"다빈");
        friMemberListViewAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.ic_baseline_person_outline_24),"다빈");
        friMemberListViewAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.ic_baseline_person_outline_24),"다빈");
        friMemberListViewAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.ic_baseline_person_outline_24),"다빈");
        friMemberListViewAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.ic_baseline_person_outline_24),"다빈");

        ////////////고정된 리스트 설정 (리스트뷰 아이템의 크기를 측정하고 높이를 모두 합하여 ListView의 Height를 설정한다.)
        for (int i=0;i<friMemberListViewAdapter.getCount();i++){
            View listItem = friMemberListViewAdapter.getView(i,null,listView);
            listItem.measure(0,0);
            listHeight+=listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = listHeight+(listView.getDividerHeight()+(friMemberListViewAdapter.getCount())-1);
        listView.setLayoutParams(params);

        listView.setAdapter(friMemberListViewAdapter);


    }
}
