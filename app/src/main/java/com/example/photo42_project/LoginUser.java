package com.example.photo42_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class LoginUser extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private TextView forgotPassword;
    private TextView wrongpass;
    private EditText edMail, edPassword;
    private Button login;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private int cnt = 0;
    private Handler h = new Handler();

    //private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    private SharedPreferences pref;

    private int stage;

    private SharedPreferences.Editor editor;

    //private Session session;//global variable

    private SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        register = (TextView) findViewById(R.id.tvRegister);
        register.setOnClickListener(this);

        forgotPassword = (TextView) findViewById(R.id.tvForgot);
        forgotPassword.setOnClickListener(this);

        wrongpass = (TextView) findViewById(R.id.wrongpass);
        wrongpass.setVisibility(View.GONE);

        login = (Button) findViewById(R.id.btLogin);
        login.setOnClickListener(this);

        edMail = (EditText) findViewById(R.id.edEmailLog);
        edPassword = (EditText) findViewById(R.id.edPasswordLog);

        progressBar = (ProgressBar) findViewById(R.id.progressBarLog);

        mAuth = FirebaseAuth.getInstance();

        pref = getSharedPreferences("MyPref", MODE_PRIVATE);

        editor = pref.edit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvRegister:
                startActivity(new Intent(this, registerUser.class));
                break;

            case R.id.btLogin:
                userLogin();
                break;
            case R.id.tvForgot:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userLogin() {
        wrongpass.setVisibility(View.GONE);
        String email = edMail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        Boolean fl = true;
        if(email.isEmpty()){
            edMail.setError("Email is required!");
            edMail.requestFocus();
            fl = false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edMail.setError("Please enter a valid email!");
            edMail.requestFocus();
            fl = false;
        }

        if(password.isEmpty()){
            edPassword.setError("Password is required!");
            edPassword.requestFocus();
            fl = false;
        }

        if(password.length() < 6){
            edPassword.setError("Minimum password length is 6 characters!");
            edPassword.requestFocus();
            fl = false;
        }
        if (fl){
            String login = edMail.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if(user.isEmailVerified()){
                            Context cntx = getApplicationContext();
                            //session = new Session(cntx);
                            //session.setusename(login);
                            //session.setpass(password);
                            cntx = null;
                            //redirect to user profile
                            editor.putString("Login", email);
                            editor.putString("Pass", password);
                            editor.commit();
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            reference = FirebaseDatabase.getInstance().getReference("Users");
                            userID = user.getUid();
                            progressBar.setVisibility(View.GONE);
                            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //Toast.makeText(MainActivity.this, "ADSADASDASD", Toast.LENGTH_SHORT).show();
                                    _User userprofile = snapshot.getValue(_User.class);

                                    if(userprofile != null){
                                        stage = userprofile.getStage();
                                        if (stage != -1){
                                            startActivity(new Intent(LoginUser.this, UpdateProfile.class));
                                            finishActivity(0);
                                        }
                                        else{
                                            startActivity(new Intent(LoginUser.this, ProfileActivity.class));
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
                        }
                        else{

                            user.sendEmailVerification();
                            Toast.makeText(LoginUser.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    else{
                        wrongpass.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginUser.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}