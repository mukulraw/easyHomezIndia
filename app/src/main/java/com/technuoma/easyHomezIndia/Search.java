package com.technuoma.easyHomezIndia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technuoma.easyHomezIndia.searchPOJO.Datum;
import com.technuoma.easyHomezIndia.searchPOJO.searchBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Search extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView grid;
    ProgressBar progress;
    EditText query;
    List<Datum> list;
    SearchAdapter adapter;
    GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        list = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar5);
        grid = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar5);
        query = findViewById(R.id.editText4);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Search");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        adapter = new SearchAdapter(this, list);
        manager = new GridLayoutManager(this, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 3) {


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

                    Call<searchBean> call = cr.search(s.toString(), SharePreferenceUtils.getInstance().getString("location"));
                    call.enqueue(new Callback<searchBean>() {
                        @Override
                        public void onResponse(Call<searchBean> call, Response<searchBean> response) {


                            if (response.body().getStatus().equals("1")) {
                                adapter.setData(response.body().getData());
                            } else {
                                adapter.setData(new ArrayList<>());
                            }

                            progress.setVisibility(View.GONE);


                        }

                        @Override
                        public void onFailure(Call<searchBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });


                } else {

                    adapter.setData(new ArrayList<Datum>());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

        Context context;
        List<Datum> list = new ArrayList<>();

        public SearchAdapter(Context context, List<Datum> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.search_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Datum item = list.get(position);


            holder.title.setText(item.getName());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, SingleProduct.class);
                    intent.putExtra("id", item.getPid());
                    intent.putExtra("title", item.getName());
                    context.startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);


                title = itemView.findViewById(R.id.textView37);


            }
        }
    }

}
