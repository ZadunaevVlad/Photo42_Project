package com.example.photo42_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    private Button logout;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    //private Session session;//global variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        logout = (Button) findViewById(R.id.btLogOut);
        pref = getSharedPreferences("MyPref", MODE_PRIVATE);

        editor = pref.edit();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Context cntx = getApplicationContext();
                //session = new Session(cntx);
                //session.setpass("");
                //session.setusename("");
                cntx = null;
                editor.remove("Login");
                editor.remove("Pass");
                editor.commit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(ProfileActivity.this, LoginUser.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView tvmail = (TextView) findViewById(R.id.showmail);
        final TextView tvname = (TextView) findViewById(R.id.showname);
        final TextView tvsec = (TextView) findViewById(R.id.showsec);
        final TextView tvlogin = (TextView) findViewById(R.id.showlogin);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                _User userprofile = snapshot.getValue(_User.class);

                if(userprofile != null){
                    String name = userprofile.getName();
                    String login = userprofile.getLogin();
                    String email = userprofile.getEmail();
                    String sec = userprofile.getSecondName();

                    tvmail.setText(email);
                    tvlogin.setText(login);
                    tvname.setText(name);
                    tvsec.setText(sec);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
    }
}