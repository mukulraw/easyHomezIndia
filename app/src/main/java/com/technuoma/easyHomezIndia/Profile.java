package com.technuoma.easyHomezIndia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    private Toolbar toolbar;
    TextView code, name, email, phone, edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar3);
        code = findViewById(R.id.textView89);
        name = findViewById(R.id.textView91);
        email = findViewById(R.id.textView93);
        phone = findViewById(R.id.textView95);
        edit = findViewById(R.id.textView96);

        setSupportActionBar(toolbar);


        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("My Profile");



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile.this , EditProfile.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        code.setText(SharePreferenceUtils.getInstance().getString("code"));
        name.setText(SharePreferenceUtils.getInstance().getString("name"));
        email.setText(SharePreferenceUtils.getInstance().getString("email"));
        phone.setText(SharePreferenceUtils.getInstance().getString("phone"));

    }
}