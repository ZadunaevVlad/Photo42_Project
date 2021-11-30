package com.example.photo42_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private String login = "", password = "";

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    private FirebaseAuth mAuth;

    private TextView logo;

    private SharedPreferences pref;

    private int stage;

    private SharedPreferences.Editor editor;

    private boolean ison = false;

   // private Session session;//global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();

        Context ctx = getApplicationContext();

        Runnable r=new Runnable() {
            public void run(){
                if (isOnline(ctx) != ison && !ison){
                    ison = true;
                    wwait();
                }
                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(r, 500);

        mAuth = FirebaseAuth.getInstance();

        pref = getSharedPreferences("MyPref", MODE_PRIVATE);

        editor = pref.edit();

        new CountDownTimer(2000, 100) {
            public void onFinish() {
                if (ison) {
                    get();
                }
                else {
                    Toast.makeText(MainActivity.this, "Нет соединения с интернетом!", Toast.LENGTH_SHORT).show();
                }
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }

    private void wwait(){
        new CountDownTimer(2000, 100) {
            public void onFinish() {
                if (ison) {
                    get();
                }
                else {
                    Toast.makeText(MainActivity.this, "Нет соединения с интернетом!", Toast.LENGTH_SHORT).show();
                }
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }

    private void get() {
        if (!isSes()){
            Toast.makeText(MainActivity.this, "Please, log in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginUser.class));
        }
        else{
            //startActivity(new Intent(MainActivity.this, registerUser.class));
            mAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference("Users");
                        userID = user.getUid();
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //Toast.makeText(MainActivity.this, "ADSADASDASD", Toast.LENGTH_SHORT).show();
                                _User userprofile = snapshot.getValue(_User.class);

                                if(userprofile != null){
                                    stage = userprofile.getStage();
                                    if (stage != -1){
                                        startActivity(new Intent(MainActivity.this, UpdateProfile.class));
                                        finishActivity(0);
                                    }
                                    else{
                                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                        finishActivity(0);
                                    }
                                    //Toast.makeText(UpdateProfile.this, String.valueOf(cnt), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                //Toast.makeText(ProfileActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
                            }
                        });
                        //startActivity(new Intent(MainActivity.this, UpdateProfile.class));

                        //finishActivity(0);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Please, log in", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginUser.class));
                    }
                }
            });
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    private boolean isSes() {
        login = "";
        login = pref.getString("Login", "");
        password = "";
        password = pref.getString("Pass", "");
        return (login != "" && password != "");
    }
}