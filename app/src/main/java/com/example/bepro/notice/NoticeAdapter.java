package com.example.bepro.notice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bepro.R;

import java.util.ArrayList;

public class NoticeAdapter extends BaseAdapter {

    private final Context context;
    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<NoticeItems> NoticeItemsList = new ArrayList<NoticeItems>();

    public NoticeAdapter(Context context, ArrayList<NoticeItems> NoticeItemsList) { //생성자
        this.context = context;
        this.NoticeItemsList = NoticeItemsList;
    }

    //필수 구현체들
    @Override
    public int getCount() { //Adapter 데이터의 개수 리턴
        return NoticeItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return NoticeItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 View 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // notice_item 레이아웃을 inflate 하여 convertView 참조 획득
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.notice_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView noticeCategory = convertView.findViewById(R.id.noticeCategory); //공지 종류
        TextView noticeTitle = convertView.findViewById(R.id.noticeTitle); //공지 제목
        TextView noticeDate = convertView.findViewById(R.id.noticeDate); //공지 게시일

        // Data Set(NoticeItemsList)에서 position에 위치한 데이터 참조 획득
        NoticeItems noticeItems = NoticeItemsList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        noticeCategory.setText(noticeItems.getNoticeCategory());
        noticeTitle.setText(noticeItems.getNoticeTitle());
        noticeDate.setText(noticeItems.getNoticeDate());

        return convertView;
    }

    /* 아이템 데이터 추가 함수
    public void addItem(int index, String category, String title, String date) {
        NoticeItems noticeItems = new NoticeItems(index, category, title, date);

        noticeItems.setNoticeIdx(index);
        noticeItems.setNoticeCategory(category);
        noticeItems.setNoticeTitle(title);
        noticeItems.setNoticeDate(date);

        NoticeItemsList.add(noticeItems);
    }

     */
}
