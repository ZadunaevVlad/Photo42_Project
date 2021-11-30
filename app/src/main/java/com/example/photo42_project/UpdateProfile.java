package com.example.photo42_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UpdateProfile extends AppCompatActivity {

    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;

    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    private FirebaseUser user;
    private DatabaseReference reference;

    private TextView tvava;

    private String userID;

    private Button BtBack;

    private DatabaseReference ref;

    private TextView tvDate;

    private TextView Error;

    private TextView tvSkip;

    private Button btNext;

    private Button btBack;

    private RadioGroup radioGroup;

    private TextView tvSex;

    private TextView tvRate;
    private TextView tv1, tv2, tv3, tv4, tv5;

    private SeekBar seekBar;

    public int cnt = 0;

    private boolean fl = false, isback = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_date);

        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        tvDate = findViewById(R.id.tvDate);

        tvDate.setVisibility(View.GONE);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        radioGroup.setVisibility(View.GONE);

        _User userProfile = new _User();

        btBack = (Button) findViewById(R.id.button3);

        tvSex = (TextView) findViewById(R.id.tvSex);

        tvava = (TextView) findViewById(R.id.textView5);

        tvSex.setVisibility(View.GONE);

        tvSkip = (TextView) findViewById(R.id.tvSkip);

        Error = (TextView) findViewById(R.id.textView8);

        btNext = (Button) findViewById(R.id.button2);

        tvRate = (TextView) findViewById(R.id.tvRate);

        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv4 = (TextView) findViewById(R.id.textView4);
        tv5 = (TextView) findViewById(R.id.textView6);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        tvRate.setVisibility(View.GONE);
        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        tv3.setVisibility(View.GONE);
        tv4.setVisibility(View.GONE);
        tv5.setVisibility(View.GONE);
        tvava.setVisibility(View.GONE);

        seekBar.setVisibility(View.GONE);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                _User userprofile = snapshot.getValue(_User.class);

                if(userprofile != null){
                    cnt = userprofile.getStage();
                    draw();
                    //Toast.makeText(UpdateProfile.this, String.valueOf(cnt), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(ProfileActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cnt > 0) {
                    cnt = cnt - 1;
                    ref = FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("stage");
                    ref.setValue(cnt);
                    isback = true;
                    draw();
                }
                else{
                    Toast.makeText(UpdateProfile.this, "Вы находитесь на первой старнице", Toast.LENGTH_SHORT).show();
                    draw();
                }
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fl || isback) {
                    Error.setVisibility(View.GONE);
                    fl = false;
                    cnt = cnt + 1;
                    ref = FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("stage");
                    ref.setValue(cnt);
                    isback = false;
                    draw();
                }
                else{
                    Error.setVisibility(View.VISIBLE);
                }
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateProfile.this, ProfileActivity.class));
            }
        });
        //Toast.makeText(UpdateProfile.this, String.valueOf(cnt), Toast.LENGTH_SHORT).show();
        //Toast.makeText(UpdateProfile.this, String.valueOf(cnt), Toast.LENGTH_SHORT).show();
    }

    private void draw() {
        if (cnt == 0) {
            tvDate.setVisibility(View.VISIBLE);
            tvSex.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            tvDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonSelectDate();
                }
            });
        }
        if (cnt == 1) {
            tvDate.setVisibility(View.GONE);
            tvRate.setVisibility(View.GONE);
            tv1.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
            tv3.setVisibility(View.GONE);
            tv4.setVisibility(View.GONE);
            tv5.setVisibility(View.GONE);
            seekBar.setVisibility(View.GONE);

            tvSex.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup.clearCheck();

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case -1:
                            break;
                        case R.id.rbMan:
                            ref = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("sex");
                            ref.setValue("Man");
                            fl = true;
                            break;
                        case R.id.rbWoman:
                            ref = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("sex");
                            ref.setValue("Woman");
                            fl = true;
                            break;
                    }
                }
            });
        }
        if (cnt == 2) {
            tvSex.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            tvRate.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            tv3.setVisibility(View.VISIBLE);
            tv4.setVisibility(View.VISIBLE);
            tv5.setVisibility(View.VISIBLE);

            seekBar.setVisibility(View.VISIBLE);

            tvava.setVisibility(View.GONE);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    tvRate.setText(String.valueOf(seekBar.getProgress() + 1));
                    ref = FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("exp");
                    ref.setValue(seekBar.getProgress() + 1);
                    fl = true;
                }
            });
        }
        if(cnt == 3){
            tvRate.setVisibility(View.GONE);
            tv1.setVisibility(View.GONE);
            tv2.setVisibility(View.GONE);
            tv3.setVisibility(View.GONE);
            tv4.setVisibility(View.GONE);
            tv5.setVisibility(View.GONE);

            seekBar.setVisibility(View.GONE);

            tvava.setVisibility(View.VISIBLE);
            fl = true;
        }
        if (cnt == 4){
            tvava.setVisibility(View.GONE);
            cnt = -1;
            ref = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("stage");
            ref.setValue(cnt);
            startActivity(new Intent(UpdateProfile.this, ProfileActivity.class));
            finishActivity(0);
        }
    }

    private void buttonSelectDate() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                tvDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                fl = true;

                tvDate.setTextSize(26);

                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;

                ref = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("year");
                ref.setValue(lastSelectedYear);
                ref = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("month");
                ref.setValue(lastSelectedMonth + 1);
                ref = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("day");
                ref.setValue(lastSelectedDayOfMonth);
            }
        };

        DatePickerDialog datePickerDialog = null;
        datePickerDialog = new DatePickerDialog(this, dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        // Show
        datePickerDialog.show();
    }
}