package com.example.login10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CourseRegistrationActivity extends AppCompatActivity {

    TextView lec_Num, div_class_num;
    EditText addLec_Num, add_div_class_num, student_id, student_pw;
    Button SaveButton, CancelButton, BackHome;

    DatabaseReference reference;

    FirebaseAuth firebaseAuth; //Firebase UID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_register);

        // keyboard가 UI 가릴 때
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        lec_Num=findViewById(R.id.lec_Num);
        div_class_num=findViewById(R.id.div_class_num);

        addLec_Num=findViewById(R.id.addLec_Num);
        add_div_class_num=findViewById(R.id.add_div_class_num);
        student_id=findViewById(R.id.student_id);
        student_pw=findViewById(R.id.student_pw);

        SaveButton=findViewById(R.id.SaveButton);
        CancelButton=findViewById(R.id.CancelButton);

        BackHome=findViewById(R.id.BackHome);

        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CourseRegistrationActivity.this, MainActivityCal.class);
                startActivity(intent);
            }
        });

        //Firebase UID
        firebaseAuth = FirebaseAuth.getInstance();

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // insert data to FireBase

                //Firebase UID
                FirebaseUser user = firebaseAuth.getCurrentUser();

                reference= FirebaseDatabase.getInstance().getReference().child("Course_"+user.getUid()).child("Course_Registration");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("id").setValue(student_id.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(student_pw.getText().toString());
                        dataSnapshot.getRef().child("lectureNumber").setValue(addLec_Num.getText().toString());
                        dataSnapshot.getRef().child("classNumber").setValue(add_div_class_num.getText().toString());

                        if (student_id.getText().toString().equals("") || student_pw.getText().toString().equals("") || addLec_Num.getText().toString().equals("") || add_div_class_num.getText().toString().equals("")) {
                            Toast.makeText(CourseRegistrationActivity.this, "입력이 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Intent intent = new Intent(CourseRegistrationActivity.this, MainActivityCal.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CourseRegistrationActivity.this, MainActivityCal.class);
                startActivity(intent);
            }
        });


    }
}