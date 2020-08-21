package com.example.DidU;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditPlanActivity extends AppCompatActivity {
    TextView myList, planName, date, selectedDate, time, selectedTime, alarm, memo, repeatDay, selectedRepeatDay;
    EditText addPlanName, addMemo;
    Button addDate, addTime, saveChangesButton, deleteButton, addRepeat, BackHome;
    ToggleButton addAlarm;
    private DatePickerDialog.OnDateSetListener callbackMethodDate;
    private TimePickerDialog.OnTimeSetListener callbackMethodTime;
    DatabaseReference reference;

    //Firebase Uid
    FirebaseAuth firebaseAuth;

    // 현재 시각 구하기
    Calendar calendar=Calendar.getInstance();
    int cYear=calendar.get(Calendar.YEAR);
    int cMonth=calendar.get(Calendar.MONTH);
    int cDate=calendar.get(Calendar.DATE);
    int cHour=calendar.get(Calendar.HOUR_OF_DAY);
    int cMinute=calendar.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);

        // keyboard가 UI 가릴 때
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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

        saveChangesButton=findViewById(R.id.SaveChangesButton);
        deleteButton=findViewById(R.id.DeleteButton);

        repeatDay=findViewById(R.id.RepeatDay);
        selectedRepeatDay=findViewById(R.id.selectedRepeatDay);
        addRepeat=findViewById(R.id.addRepeat);

        BackHome=findViewById(R.id.BackHome);

        //Firebase Uid
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Date 설정 관련 코드
        this.InitializeViewDate();
        this.InitializeListenerDate();
        // Time 설정 관련 코드
        this.InitializeViewTime();
        this.InitializeListenerTime();

        // Get value
        addPlanName.setText(getIntent().getStringExtra("planName"));
        selectedDate.setText(getIntent().getStringExtra("date"));
        selectedTime.setText(getIntent().getStringExtra("time"));
        addAlarm.setText(getIntent().getStringExtra("alarm"));
        addMemo.setText(getIntent().getStringExtra("memo"));
        selectedRepeatDay.setText(getIntent().getStringExtra("repeatDay"));
        final String keyKey=getIntent().getStringExtra("key");

        reference= FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("Plan"+keyKey);

        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditPlanActivity.this, PlanListActivity.class);
                startActivity(intent);
            }
        });

        // Make button Event(DeleteButton)
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(EditPlanActivity.this, PlanListActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Make button Event(SaveChangesButton)
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (addPlanName.getText().toString().equals("") || selectedDate.getText().toString().equals("") || selectedTime.getText().toString().equals("") || selectedRepeatDay.getText().toString().equals("")) {
                            Toast.makeText(EditPlanActivity.this, "입력이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            dataSnapshot.getRef().child("planName").setValue(addPlanName.getText().toString());
                            dataSnapshot.getRef().child("date").setValue(selectedDate.getText().toString());
                            dataSnapshot.getRef().child("time").setValue(selectedTime.getText().toString());
                            dataSnapshot.getRef().child("alarm").setValue(addAlarm.getText().toString());
                            dataSnapshot.getRef().child("memo").setValue(addMemo.getText().toString());
                            dataSnapshot.getRef().child("key").setValue(keyKey);

                            if(addAlarm.isChecked())
                            {
                                createNotification();
                            }
                            else
                            {
                                removeNotification();
                            }
                            Intent intent = new Intent(EditPlanActivity.this, PlanListActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        // 반복설정 수정
        addRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] dayOfWeek = new String[]{"반복 없음", "매일", "1주일마다", "2주일마다", "매달", "매년"};
                final int[] selectItem = {0};

                AlertDialog.Builder dialog = new AlertDialog.Builder(EditPlanActivity.this);
                dialog.setTitle("반복되는 요일을 선택하세요.").setSingleChoiceItems(dayOfWeek, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectItem[0] = i;
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedRepeatDay.setText(dayOfWeek[selectItem[0]]);
                    }
                }).create().show();
            }
        });
    }

    // 알림 create (Edit Plan)
    private void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        Intent intent = new Intent(this, PlanListActivity.class);//
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); //
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(addPlanName.getText());
        builder.setContentText(selectedDate.getText() + "\t\t" + selectedTime.getText() +"으로 일정이 수정되었습니다!");
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE); //
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(pendingIntent);//
        builder.setPriority(NotificationCompat.PRIORITY_HIGH); // 우선순위

        builder.setColor(Color.BLACK);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_HIGH));
        }

        AlarmManager alarmManager = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

//        //  Set the alarm to start at 8:30 a.m.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 8);
//        calendar.set(Calendar.MINUTE, 30);


        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, pendingIntent);

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(0, builder.build());
    }


    // 알림 삭제
    private void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(0);
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
                monthOfYear++;
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
            }
        };
    }

    public void OnClickHandlerTime(View view)
    {
        TimePickerDialog dialog=new TimePickerDialog(this, callbackMethodTime, cHour, cMinute, true);
        dialog.show();;
    }
}