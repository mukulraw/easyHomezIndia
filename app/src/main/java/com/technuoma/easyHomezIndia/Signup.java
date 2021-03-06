package com.technuoma.easyHomezIndia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Signup extends AppCompatActivity {

    private Toolbar toolbar;
    EditText name, email, phone, referral;

    ProgressBar progress;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = findViewById(R.id.textView3);
        name = findViewById(R.id.editText);
        email = findViewById(R.id.editTextTextEmailAddress);
        phone = findViewById(R.id.editTextPhone);
        referral = findViewById(R.id.editTextTextPersonName);
        login = findViewById(R.id.button);
        progress = findViewById(R.id.progressBar);

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
        toolbar.setTitle("Create an Account");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String n = name.getText().toString();
                String e = email.getText().toString();
                String p = phone.getText().toString();
                String r = referral.getText().toString();

                if (n.length() > 0) {
                    if (e.length() > 0) {
                        if (p.length() == 10) {

                            if (r.length() > 0) {
                                progress.setVisibility(View.VISIBLE);

                                Bean b = (Bean) getApplicationContext();

                                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                                logging.level(HttpLoggingInterceptor.Level.HEADERS);
                                logging.level(HttpLoggingInterceptor.Level.BODY);

                                OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(b.baseurl)
                                        .client(client)
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                                Call<loginBean> call = cr.login(n, e, p, SharePreferenceUtils.getInstance().getString("token"), r);

                                call.enqueue(new Callback<loginBean>() {
                                    @Override
                                    public void onResponse(@NotNull Call<loginBean> call, @NotNull Response<loginBean> response) {

                                        assert response.body() != null;
                                        if (response.body().getStatus().equals("1")) {
                                            //SharePreferenceUtils.getInstance().saveString("userId" , response.body().getUserId());
                                            SharePreferenceUtils.getInstance().saveString("phone", response.body().getPhone());
                                            Toast.makeText(Signup.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(Signup.this, OTP.class);
                                            startActivity(intent);
                                            finishAffinity();

                                        } else {
                                            Toast.makeText(Signup.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        progress.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<loginBean> call, @NotNull Throwable t) {
                                        progress.setVisibility(View.GONE);
                                    }
                                });
                            } else {
                                Toast.makeText(Signup.this, "Invalid referral code", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(Signup.this, "Invalid phone", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Signup.this, "Invalid email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Signup.this, "Invalid name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}