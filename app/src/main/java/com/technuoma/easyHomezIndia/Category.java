package com.technuoma.easyHomezIndia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.technuoma.easyHomezIndia.homePOJO.Cat;
import com.technuoma.easyHomezIndia.homePOJO.Subcat;
import com.technuoma.easyHomezIndia.homePOJO.homeBean;
import com.technuoma.easyHomezIndia.subCat1POJO.Datum;

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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Category extends Fragment {

    Toolbar toolbar;
    RecyclerView grid;
    ProgressBar progress;
    List<Cat> list;
    CategoryAdapter adapter;

    static MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_category, container, false);
        mainActivity = (MainActivity) getActivity();
        list = new ArrayList<>();

        toolbar = view.findViewById(R.id.toolbar2);
        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progressBar2);

        adapter = new CategoryAdapter(mainActivity, list);
        GridLayoutManager manager = new GridLayoutManager(mainActivity, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) mainActivity.getApplicationContext();

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

        Call<homeBean> call = cr.getHome(SharePreferenceUtils.getInstance().getString("lat"), SharePreferenceUtils.getInstance().getString("lng"));
        call.enqueue(new Callback<homeBean>() {
            @Override
            public void onResponse(Call<homeBean> call, Response<homeBean> response) {


                if (response.body().getStatus().equals("1")) {
                    adapter.setData(response.body().getCat());
                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<homeBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        Context context;
        List<Cat> list = new ArrayList<>();

        public CategoryAdapter(Context context, List<Cat> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Cat> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.category_list_model1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Cat item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

            //holder.tag.setText(item.getTag());
            holder.title.setText(item.getName());
            holder.desc.setText(item.getDescription());

            SubCategoryAdapter adapter = new SubCategoryAdapter(context , item.getSubcat());
            GridLayoutManager manager = new GridLayoutManager(context , 3);
            holder.gridl.setAdapter(adapter);
            holder.gridl.setLayoutManager(manager);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.hide.getVisibility() == View.VISIBLE)
                    {
                        holder.hide.setVisibility(View.GONE);
                        holder.main.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    else
                    {
                        holder.hide.setVisibility(View.VISIBLE);
                        holder.main.setCardBackgroundColor(Color.parseColor("#FFF3E0"));

                        grid.smoothScrollToPosition(position);

                    }

                    /*Intent intent = new Intent(context, SubCat.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("title", item.getName());
                    intent.putExtra("image", item.getImage());
                    context.startActivity(intent);*/

                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView tag, title, desc;
            RecyclerView gridl;
            CardView hide , main;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView5);
                //tag = itemView.findViewById(R.id.textView17);
                title = itemView.findViewById(R.id.textView18);
                desc = itemView.findViewById(R.id.textView5);
                gridl = itemView.findViewById(R.id.secondgrid);
                hide = itemView.findViewById(R.id.grid);
                main = itemView.findViewById(R.id.linearLayout3);


            }
        }
    }

    class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

        Context context;
        List<Subcat> list = new ArrayList<>();

        public SubCategoryAdapter(Context context, List<Subcat> list) {
            this.context = context;
            this.list = list;
        }

        /*public void setData(List<Datum> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }
*/
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.sub_category_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Subcat item = list.get(position);


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage() , holder.image , options);


            holder.title.setText(item.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                    FragmentTransaction ft4 = fm4.beginTransaction();
                    Products frag14 = new Products();
                    Bundle b = new Bundle();
                    b.putString("id", item.getId());
                    b.putString("title", item.getName());
                    frag14.setArguments(b);
                    ft4.replace(R.id.replace, frag14);
                    ft4.addToBackStack(null);
                    ft4.commit();


                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView4);

                title = itemView.findViewById(R.id.textView11);


            }
        }
    }


}
