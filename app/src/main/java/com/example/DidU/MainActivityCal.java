package com.example.DidU;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivityCal extends AppCompatActivity {

    // 화면 연결
    TextView toAddPlan, toCheckPlan, toCourseRegi;
    TextView myList; //, today;
    Button addNewButton, showListButton;
    Button userButton;

    DatabaseReference reference;
    RecyclerView myPlans;
    ArrayList<PlanItemData> list;
    PlanAdapter planAdapter;
    Button drawerToUser;

    //Firebase Uid
    FirebaseAuth firebaseAuth;

    // 현재 시각 구하기
    Calendar calendar=Calendar.getInstance();
    int cYear=calendar.get(Calendar.YEAR);
    int cMonth=calendar.get(Calendar.MONTH)+1;
    int cDate=calendar.get(Calendar.DATE);
    int cHour=calendar.get(Calendar.HOUR_OF_DAY);
    int cMinute=calendar.get(Calendar.MINUTE);

    private DatePickerDialog.OnDateSetListener callbackMethodDate;
    private TimePickerDialog.OnTimeSetListener callbackMethodTime;

    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cal);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView=(View)findViewById(R.id.drawer);

        // 화면 연결
        toAddPlan=(TextView) findViewById(R.id.toAddPlan);
        toCheckPlan=(TextView) findViewById(R.id.toCheckPlan);
        toCourseRegi=(TextView) findViewById(R.id.toCourseRegi);

        toAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityCal.this, AddNewPlanActivity.class);
                startActivity(intent);
            }
        });

        toCheckPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityCal.this, PlanListActivity.class);
                startActivity(intent);
            }
        });

        toCourseRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityCal.this, CourseRegistrationActivity.class);
                startActivity(intent);
            }
        });

        Button btn_open=(Button)findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                drawerLayout.openDrawer(drawerView);
            }
        });

        drawerToUser=(Button) findViewById(R.id.drawerToUser);
        drawerToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityCal.this, UserActivity.class);
                startActivity(intent);
            }
        });

        myList=findViewById(R.id.MyList);
        userButton = findViewById(R.id.userButton);

        //Firebase Uid
        firebaseAuth = FirebaseAuth.getInstance();

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityCal.this, UserActivity.class);
                startActivity(intent);
            }
        });

        // today.setText(cYear+" / "+cMonth+" / "+cDate);

        // working with data
        myPlans=findViewById(R.id.MyPlans);
        myPlans.setHasFixedSize(true); //
        myPlans.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<PlanItemData>();

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // get data from FireBase
        //Firebase Uid
        FirebaseUser user = firebaseAuth.getCurrentUser();

        reference=FirebaseDatabase.getInstance().getReference().child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    PlanItemData planItemData=dataSnapshot1.getValue(PlanItemData.class);
                    list.add(planItemData);
                }

                planAdapter=new PlanAdapter(MainActivityCal.this, list);
                myPlans.setAdapter(planAdapter);
                planAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        reference = FirebaseDatabase.getInstance().getReference().child("DidU1");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // set code to retrive data and replace layout
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
//                {
//                    PlanItemData planItemData=dataSnapshot1.getValue(PlanItemData.class);
//                    list.add(planItemData);
//                }
//                planAdapter=new PlanAdapter(MainActivityCal.this, list);
//                myPlans.setAdapter(planAdapter);
//                planAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // set code to show error
//                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    public void OnClickHandlerDate(View view)
    {
        DatePickerDialog dialog=new DatePickerDialog(this, callbackMethodDate, cYear, cMonth, cDate);
        dialog.show();
    }

    public void OnClickHandlerTime(View view)
    {
        TimePickerDialog dialog=new TimePickerDialog(this, callbackMethodTime, cHour, cMinute, true);
        dialog.show();;
    }

//    @Override
//    public void onClick(View view) {
////        if(view == buttonSignin) {
////            userLogin();
////        }
//        if(view == userButton) {
//            finish();
//            startActivity(new Intent(this, MainActivity.class));
//        }
//    }
}