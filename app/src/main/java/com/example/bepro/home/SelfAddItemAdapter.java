package com.example.bepro.home;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepro.MainActivity;
import com.example.bepro.R;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SelfAddItemAdapter extends RecyclerView.Adapter<SelfAddItemAdapter.ViewHolder> implements OnSelfAddItemClickListener {
    ArrayList<FoodItems> items = new ArrayList<FoodItems>();
    OnSelfAddItemClickListener listener;
    int position;

    @NonNull
    @NotNull
    @Override
    // 뷰홀더가 새로 만들어지는 시점에 호출, 정의된 XML 레이아웃으로 뷰 객체 생성
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        //인플레이션으로 뷰객체 생성 (카드뷰 품목 아이템)
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.add_food_items, viewGroup, false);

        return new ViewHolder(itemView, this); //뷰객체 생성과 동시에 뷰객체 반환
    }

    @Override
    // 뷰홀더가 재활용 될 시 호출, 데이터만 바꿔줌
    public void onBindViewHolder(@NonNull @NotNull ViewHolder viewHolder, final int position) {
        FoodItems foodItem = items.get(position);
        //viewHolder.setItem(foodItem);

        //viewHolder.mFoodName.setText(items.get(position).getFoodName()); //식품명
        //viewHolder.mFoodTotal.setText(items.get(position).getFoodNumber()); //개수
        //viewHolder.mFoodRemainDate.setText(items.get(position).getFoodRemainDate()); //남은날짜
        //viewHolder.tvFoodExp.setText(foodItem.getFoodExpiryDate()); //유통기한

        //foodItem.setFoodName(viewHolder.mFoodName.getText().toString());
        //items.get(position).setFoodNumber(viewHolder.mFoodTotal.getText());


        //카드뷰 삭제 버튼 이벤트 리스너 연결
        viewHolder.selfItemDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 선택한 아이템이 삭제되긴 하는데..
                setPosition(position); //클릭한 아이템 position set
                removeItem(position); //아이템 삭제 함수 호출
                Toast.makeText(v.getContext(), position + "번째 아이템 삭제", Toast.LENGTH_SHORT).show();

            }
        });

    }

    //품목 아이템 카드뷰 삭제 메소드
    public void removeItem(int position){
        items.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged(); //갱신처리
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(FoodItems item){
        items.add(item);
    }

    public void setItems(ArrayList<FoodItems> items){
        this.items = items;
    }

    public FoodItems getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, FoodItems item){
        items.set(position, item);
    }

    public void setPosition(int position) { this.position = position; }

    public int getPosition() { return position; }

    public void setOnItemClickListener(OnSelfAddItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) { //interface 구현
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    //커스텀 뷰홀더
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView selfItemDeleteBtn, mFoodRemainDate;
        LinearLayout tvSelfAddFoodName, editAddFoodName, editFoodTotalCountView;
        EditText mFoodName, mFoodTotal;

        Calendar calendar;
        int dateEndY, dateEndM, dateEndD;
        int dDayValue = 0;
        int currentYear, currentMonth, currentDay;
        private final int ONE_DAY = 24 * 60 * 60 * 1000; //Millisecond 형태의 하루(24 시간), 86400000 = 24시간 * 60분 * 60초 * 1000(=1초)

        TextView dateResult;
        LinearLayout datePicker;

        public ViewHolder(@NonNull @NotNull View itemView, final OnSelfAddItemClickListener listener) { //뷰홀더 생성자로 뷰객체 전달
            super(itemView);

            //뷰객체에 들어있는 텍스트뷰 참조
            //mFoodName = itemView.findViewById(R.id.editAddFoodName); //식품명
            //mFoodTotal = itemView.findViewById(R.id.editFoodTotalCount); //개수
            //mFoodRemainDate = itemView.findViewById(R.id.selfAddFoodRemainDate); //남은날짜

            selfItemDeleteBtn = itemView.findViewById(R.id.selfItemDeleteBtn); //삭제버튼

            /* TODO: 1. editText 클릭시 키보드 노출, 이외 영역 클릭 시 키보드 사라짐
                     2. 다른 아이템 editText 클릭시 포커스 이동 */

            //아이템 뷰에 click 이벤트 리스너 연결
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });

            //현재 년월일 저장
            calendar = Calendar.getInstance();
            currentYear = calendar.get(Calendar.YEAR);
            currentMonth = calendar.get(Calendar.MONTH);
            currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            //TODO: 직접등록이 아닌 자동등록의 경우 DB 데이터 가져와서 '-' 기준으로 split 후 년월일 구분해서 저장하기

            datePicker = itemView.findViewById(R.id.selfAddFoodDatePicker); //달력 다이얼로그 버튼
            dateResult = (TextView) itemView.findViewById(R.id.selfAddFoodRemainDate);

            //한국어 설정
            Locale.setDefault(Locale.KOREAN);

            //datePicker : 디데이 날짜 입력 버튼, 클릭시 DatePickerDialog 띄움
            datePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(itemView.getContext(), endDateSetListener, currentYear, currentMonth, currentDay).show();
                }
            });

        }

        public void setItem(FoodItems item){
            //mFoodName.setText(item.getFoodName());
            //mFoodTotal.setText(item.getFoodNumber());
            //tvFoodRemain.setText(item.getFoodRemainDate());
        }

        //DatePickerDialog 팝업, 종료일 저장, 기존에 입력한 값이 있으면 해당 데이터 설정후 띄움
        private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                dDayValue = dDayResult_int(dateEndY, dateEndM, dateEndD);

                dateResult.setText(getDday(year, month, day));

            }
        };

        //D-day 반환 함수
        private String getDday(int mYear, int mMonth, int mDay) {

            //D-day 설정
            final Calendar dDayCalendar = Calendar.getInstance();
            dDayCalendar.set(mYear, mMonth, mDay);

            //millisecond 으로 환산 후 d-day 에서 today 의 차를 구함
            final long dDay = dDayCalendar.getTimeInMillis() / ONE_DAY; //사용자 선택 날짜
            final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY; //오늘 날짜
            long result = dDay - today;

            //출력 시 d-day 에 맞게 표시
            String strFormat;

            if (result > 0) { //남은 기한
                strFormat = "%d일";
            } else if (result == 0) { //당일
                strFormat = "오늘";
            } else { //지난 기한
                //result *= -1;
                strFormat = "%d일";
            }

            final String strCount = (String.format(strFormat, result));

            return strCount;
        }


        //D-day 값 계산 함수
        public int onCalculatorDate (int dateEndY, int dateEndM, int dateEndD) {
            try {
                Calendar today = Calendar.getInstance(); //현재 날짜
                Calendar dDay = Calendar.getInstance();

                //시작일, 종료일 데이터 저장
                Calendar calendar = Calendar.getInstance();
                int cyear = calendar.get(Calendar.YEAR);
                int cmonth = (calendar.get(Calendar.MONTH) + 1);
                int cday = calendar.get(Calendar.DAY_OF_MONTH);

                today.set(cyear, cmonth, cday);
                dDay.set(dateEndY, dateEndM, dateEndD);// D-day의 날짜를 입력

                long day = dDay.getTimeInMillis() / ONE_DAY;
                long tday = today.getTimeInMillis() / ONE_DAY;
                long count = tday - day; // 오늘 날짜에서 dday 날짜를 빼주게 됨

                return (int) count; // 날짜는 하루 + 시켜줘야함

            } catch (Exception e) {
                e.printStackTrace();

                return -1;
            }
        }

        //D-day 값 계산 결과 출력
        public int dDayResult_int(int dateEndY, int dateEndM, int dateEndD) {
            int result = 0;

            result = onCalculatorDate(dateEndY, dateEndM, dateEndD);

            return result;
        }


    }


}
