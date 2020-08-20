package com.example.login10;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;

public class AddNewPlanActivity extends AppCompatActivity {

    TextView myList, planName, date, selectedDate, time, selectedTime, alarm, memo;
    EditText addPlanName, addMemo;
    Button addDate, addTime, saveButton, cancelButton;
    ToggleButton addAlarm;
    ToggleButton complete;
    private DatePickerDialog.OnDateSetListener callbackMethodDate;
    private TimePickerDialog.OnTimeSetListener callbackMethodTime;
    DatabaseReference reference;
    Integer planNumber=new Random().nextInt(10000)+1; // For unique number // planNumber=='id' of plans
    String key=Integer.toString(planNumber);
    String alarmTime;
    FirebaseAuth firebaseAuth; //Firebase UID

    // 현재 시각 구하기
    Calendar calendar=Calendar.getInstance();
    int cYear=calendar.get(Calendar.YEAR);
    int cMonth=calendar.get(Calendar.MONTH)+1;
    int cDate=calendar.get(Calendar.DATE);
    int cHour=calendar.get(Calendar.HOUR_OF_DAY);
    int cMinute=calendar.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_plan);

        myList=findViewById(R.id.MyList);
        planName=findViewById(R.id.planName);
        addPlanName=findViewById(R.id.AddPlanName);

        date=findViewById(R.id.date);
        selectedDate=findViewById(R.id.selectedDate);
        addDate=findViewById(R.id.addDate);

        time=findViewById(R.id.Time);
        selectedTime=findViewById(R.id.selectedTime);
        addTime=findViewById(R.id.addTime);

        alarm=findViewById(R.id.Alarm);
        addAlarm=findViewById(R.id.addAlarm);

        memo=findViewById(R.id.memo);
        addMemo=findViewById(R.id.AddMemo);

        saveButton=findViewById(R.id.SaveButton);
        cancelButton=findViewById(R.id.CancelButton);

        // Date 설정 관련 코드
        this.InitializeViewDate();
        this.InitializeListenerDate();
        // Time 설정 관련 코드
        this.InitializeViewTime();
        this.InitializeListenerTime();

        addPlanName.setPrivateImeOptions("defaultInputmode=korean;");
        addMemo.setPrivateImeOptions("defaultInputmode=korean;");

        //Firebase UID
        firebaseAuth = FirebaseAuth.getInstance();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // insert data to FireBase

                //Firebase UID
                FirebaseUser user = firebaseAuth.getCurrentUser();

                reference= FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("Plan"+planNumber);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("planName").setValue(addPlanName.getText().toString());
                        dataSnapshot.getRef().child("date").setValue(selectedDate.getText().toString());
                        dataSnapshot.getRef().child("time").setValue(selectedTime.getText().toString());
                        dataSnapshot.getRef().child("alarm").setValue(addAlarm.getText().toString());
                        dataSnapshot.getRef().child("memo").setValue(addMemo.getText().toString());
                        dataSnapshot.getRef().child("key").setValue(key);
//                        dataSnapshot.getRef().child("complete").setValue(complete.getText().toString());

                        Intent intent=new Intent(AddNewPlanActivity.this, PlanListActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddNewPlanActivity.this, PlanListActivity.class);
                startActivity(intent);
            }
        });
    }

    // 사용자가 지정한 날짜 정보를 TextView 에 표시
    public void InitializeViewDate ()
    {
        selectedDate=(TextView) findViewById(R.id.selectedDate);
    }

    // OnDateSetListener 구현 함수
    // onDateSet() 함수 오버라이딩하여 다이얼로그 창의 완료 버튼 클릭하면 실행되는 CallBack 함수
    public void InitializeListenerDate()
    {
        callbackMethodDate=new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                selectedDate.setText(year+" / " + monthOfYear + " / " + dayOfMonth);
            }
        };
    }

    public void OnClickHandlerDate(View view)
    {
        DatePickerDialog dialog=new DatePickerDialog(this, callbackMethodDate, cYear, cMonth, cDate);
        dialog.show();
    }

    // 사용자가 지정한 시간 정보를 TextView 에 표시
    public void InitializeViewTime()
    {
        selectedTime=(TextView) findViewById(R.id.selectedTime);
    }

    // OnDateSetListener 구현 함수
    // onDateSet() 함수 오버라이딩하여 다이얼로그 창의 완료 버튼 클릭하면 실행되는 CallBack 함수
    public void InitializeListenerTime()
    {
        callbackMethodTime=new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute)
            {
                selectedTime.setText(hour + "시 " + minute + "분");
                alarmTime=(hour-1)+"시"+minute+"분";
            }
        };
    }

    public void OnClickHandlerTime(View view)
    {
        TimePickerDialog dialog=new TimePickerDialog(this, callbackMethodTime, cHour, cMinute, true);
        dialog.show();;
    }
}