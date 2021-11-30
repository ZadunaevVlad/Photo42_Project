package com.example.photo42_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class registerUser extends AppCompatActivity implements View.OnClickListener {

    private TextView retToLog, registerus;
    private EditText edName, edSec, edLog, edMail, edPassword, edPhone;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        retToLog = (TextView) findViewById(R.id.tvAlready);
        retToLog.setOnClickListener(this);

        registerus = (Button) findViewById(R.id.btReg);
        registerus.setOnClickListener(this);

        edLog = (EditText) findViewById(R.id.edLoginReg);
        edName = (EditText) findViewById(R.id.edNameReg);
        edSec = (EditText) findViewById(R.id.edSecReg);
        edMail = (EditText) findViewById(R.id.edMailReg);
        edPassword = (EditText) findViewById(R.id.edPasswordReg);
        edPhone = (EditText) findViewById(R.id.edPhoneReg);

        checkbox = (CheckBox) findViewById(R.id.checkBox);

        progressBar = (ProgressBar) findViewById(R.id.progressBarReg);
        progressBar.setVisibility(View.GONE);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    edPassword.setInputType(129);
                } else {
                    edPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvAlready:
                startActivity(new Intent(this, LoginUser.class));
                break;
            case R.id.btReg:
                regUser();
                break;
        }
    }

    private void regUser() {
        String email = edMail.getText().toString().trim();
        String name = edName.getText().toString().trim();
        String secondName = edSec.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String login = edLog.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();

        Boolean fl = true;
        if(name.isEmpty()) {
            edName.setError("Name is required!");
            edName.requestFocus();
            fl = false;
        }
        if(secondName.isEmpty()) {
            edSec.setError("Second name is required!");
            edSec.requestFocus();
            fl = false;
        }
        if(login.isEmpty()) {
            edLog.setError("Login is required!");
            edLog.requestFocus();
            fl = false;
        }
        if(email.isEmpty()) {
            edMail.setError("E-mail is required!");
            edMail.requestFocus();
            fl = false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edMail.setError("Please provide valid email!");
            edMail.requestFocus();
            fl = false;
        }
        if(phone.isEmpty()) {
            edPhone.setError("Phone is required!");
            edPhone.requestFocus();
            fl = false;
        }
        if(!Patterns.PHONE.matcher(phone).matches()){
            edPhone.setError("Please provide valid phone!");
            edPhone.requestFocus();
            fl = false;
        }
        if(password.isEmpty()) {
            edPassword.setError("Password is required!");
            edPassword.requestFocus();
            fl = false;
        }
        if(password.length() < 6) {
            edPassword.setError("Minimum password length should be 6 characters!");
            edPassword.requestFocus();
            fl = false;
        }
        if (fl) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                _User user = new _User(0, 0, 0, "", 0, 0, name, secondName, email, login, phone);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(registerUser.this, "User has been registered successfully!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.VISIBLE);
                                            startActivity(new Intent(registerUser.this, LoginUser.class));
                                        }
                                        else{
                                            Toast.makeText(registerUser.this, "Failed to register user! Try again", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(registerUser.this, "Failed to register user! Try again", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }
}