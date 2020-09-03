package com.technuoma.easyHomezIndia;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.technuoma.easyHomezIndia.cartPOJO.cartBean;
import com.technuoma.easyHomezIndia.homePOJO.Banners;
import com.technuoma.easyHomezIndia.homePOJO.Best;
import com.technuoma.easyHomezIndia.homePOJO.homeBean;
import com.technuoma.easyHomezIndia.seingleProductPOJO.Data;
import com.technuoma.easyHomezIndia.seingleProductPOJO.singleProductBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;
import nl.dionsegijn.steppertouch.StepperTouch;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SingleProduct extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager image;
    TextView discount, title, price;
    Button add;
    TextView brand, unit, seller;
    TextView description, key_features, packaging, life, disclaimer, stock;
    TextView descriptiontitle, key_featurestitle, packagingtitle, lifetitle;
    ProgressBar progress;

    String id, name;

    String pid, nv1;

    CircleIndicator indicator;

    RecyclerView recent, loved;
    List<Best> list;
    BestAdapter adapter2, adapter3;

    ImageButton wishlist;

    ImageButton cart1;
    TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        list = new ArrayList<>();

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("title");

        loved = findViewById(R.id.loved);
        count = findViewById(R.id.count);
        cart1 = findViewById(R.id.imageButton3);
        wishlist = findViewById(R.id.wishlist);
        recent = findViewById(R.id.recent);
        toolbar = findViewById(R.id.toolbar);
        descriptiontitle = findViewById(R.id.descriptiontitle);
        key_featurestitle = findViewById(R.id.key_featurestitle);
        packagingtitle = findViewById(R.id.packagingtitle);
        lifetitle = findViewById(R.id.lifetitle);
        indicator = findViewById(R.id.indicator);
        image = findViewById(R.id.image);
        discount = findViewById(R.id.discount);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        add = findViewById(R.id.add);
        brand = findViewById(R.id.brand);
        unit = findViewById(R.id.unit);
        seller = findViewById(R.id.seller);
        description = findViewById(R.id.description);
        key_features = findViewById(R.id.key_features);
        packaging = findViewById(R.id.packaging);
        life = findViewById(R.id.life);
        disclaimer = findViewById(R.id.disclaimer);
        progress = findViewById(R.id.progress);
        stock = findViewById(R.id.stock);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(name);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        adapter2 = new BestAdapter(this, list);
        adapter3 = new BestAdapter(this, list);
        LinearLayoutManager manager1 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        recent.setAdapter(adapter2);
        recent.setLayoutManager(manager1);

        loved.setAdapter(adapter3);
        loved.setLayoutManager(manager2);


        descriptiontitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (description.getVisibility() == View.GONE) {
                    description.setVisibility(View.VISIBLE);
                } else {
                    description.setVisibility(View.GONE);
                }
            }
        });

        key_featurestitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (key_features.getVisibility() == View.GONE) {
                    key_features.setVisibility(View.VISIBLE);
                } else {
                    key_features.setVisibility(View.GONE);
                }
            }
        });


        packagingtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (packaging.getVisibility() == View.GONE) {
                    packaging.setVisibility(View.VISIBLE);
                } else {
                    packaging.setVisibility(View.GONE);
                }
            }
        });


        lifetitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (life.getVisibility() == View.GONE) {
                    life.setVisibility(View.VISIBLE);
                } else {
                    life.setVisibility(View.GONE);
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (pid.length() > 0) {
                    String uid = SharePreferenceUtils.getInstance().getString("userId");

                    if (uid.length() > 0) {

                        final Dialog dialog = new Dialog(SingleProduct.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.add_cart_dialog);
                        dialog.show();

                        final StepperTouch stepperTouch = dialog.findViewById(R.id.stepperTouch);
                        Button add = dialog.findViewById(R.id.button8);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar2);


                        stepperTouch.setMinValue(1);
                        stepperTouch.setMaxValue(99);
                        stepperTouch.setSideTapEnabled(true);
                        stepperTouch.setCount(1);

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressBar.setVisibility(View.VISIBLE);

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

                                Log.d("userid", SharePreferenceUtils.getInstance().getString("userid"));
                                Log.d("pid", pid);
                                Log.d("quantity", String.valueOf(stepperTouch.getCount()));
                                Log.d("price", nv1);

                                int versionCode = BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId"), pid, String.valueOf(stepperTouch.getCount()), nv1, versionName);

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            loadCart();
                                            dialog.dismiss();
                                        }

                                        Toast.makeText(SingleProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        progressBar.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });


                            }
                        });

                    } else {
                        Toast.makeText(SingleProduct.this, "Please login to continue", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SingleProduct.this, Login.class);
                        startActivity(intent);

                    }
                }


            }
        });

        cart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SingleProduct.this, Cart.class);
                startActivity(intent);


            }
        });


    }

    void loadCart() {
        String uid = SharePreferenceUtils.getInstance().getString("userId");

        if (uid.length() > 0) {
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

            Call<cartBean> call2 = cr.getCart(SharePreferenceUtils.getInstance().getString("userId"));
            call2.enqueue(new Callback<cartBean>() {
                @Override
                public void onResponse(Call<cartBean> call, Response<cartBean> response) {

                    if (response.body().getData().size() > 0) {


                        count.setText(String.valueOf(response.body().getData().size()));


                    } else {

                        count.setText("0");

                    }


                }

                @Override
                public void onFailure(Call<cartBean> call, Throwable t) {

                }
            });


        } else {
            count.setText("0");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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

        Call<singleProductBean> call = cr.getProductById(id, SharePreferenceUtils.getInstance().getString("userId"));
        call.enqueue(new Callback<singleProductBean>() {
            @Override
            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {


                if (response.body().getStatus().equals("1")) {
                    Data item = response.body().getData();

                    pid = item.getId();

                    BannerAdapter adapter = new BannerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, item.getImage());
                    image.setAdapter(adapter);
                    indicator.setViewPager(image);

                    float dis = Float.parseFloat(item.getDiscount());

                    if (dis > 0) {

                        float pri = Float.parseFloat(item.getPrice());
                        float dv = (dis / 100) * pri;

                        float nv = pri - dv;

                        nv1 = String.valueOf(nv);

                        discount.setVisibility(View.VISIBLE);
                        discount.setText(item.getDiscount() + "% OFF");
                        price.setText(Html.fromHtml("Selling Price:  <font color=\"#000000\"><b>\u20B9" + String.valueOf(nv) + " </b></font><strike>\u20B9" + item.getPrice() + "</strike>"));
                    } else {

                        nv1 = item.getPrice();
                        discount.setVisibility(View.GONE);
                        price.setText(Html.fromHtml("Selling Price:  <font color=\"#000000\"><b>\u20B9" + String.valueOf(item.getPrice()) + " </b></font>"));
                    }

                    if (item.getWishlist().equals("1")) {
                        wishlist.setBackground(getDrawable(R.drawable.ic_heart1));

                        wishlist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

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


                                int versionCode = com.nostra13.universalimageloader.BuildConfig.VERSION_CODE;
                                String versionName = com.nostra13.universalimageloader.BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.removeWishlist(SharePreferenceUtils.getInstance().getString("userId"), item.getId());

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            onResume();
                                        }

                                        Toast.makeText(SingleProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        progress.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                                        progress.setVisibility(View.GONE);
                                    }
                                });

                            }
                        });

                    } else {
                        wishlist.setBackground(getDrawable(R.drawable.ic_heart));

                        wishlist.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

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


                                int versionCode = com.nostra13.universalimageloader.BuildConfig.VERSION_CODE;
                                String versionName = com.nostra13.universalimageloader.BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.addWishlist(SharePreferenceUtils.getInstance().getString("userId"), item.getId());

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            onResume();
                                        }

                                        Toast.makeText(SingleProduct.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        progress.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                                        progress.setVisibility(View.GONE);
                                    }
                                });

                            }
                        });


                    }


                    title.setText(item.getName());

                    brand.setText(item.getBrand());
                    unit.setText(item.getSize());
                    seller.setText(item.getSeller());

                    description.setText(item.getDescription());
                    key_features.setText(item.getKeyFeatures());
                    packaging.setText(item.getPackagingType());
                    life.setText(item.getShelfLife());
                    disclaimer.setText(item.getDisclaimer());


                    if (item.getStock().equals("In stock")) {
                        add.setEnabled(true);
                    } else {
                        add.setEnabled(false);
                    }

                    stock.setText(item.getStock());


                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<singleProductBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        Call<homeBean> call2 = cr.getHome(SharePreferenceUtils.getInstance().getString("lat"), SharePreferenceUtils.getInstance().getString("lng"));
        call2.enqueue(new Callback<homeBean>() {
            @Override
            public void onResponse(Call<homeBean> call, Response<homeBean> response) {


                if (response.body().getStatus().equals("1")) {

                    adapter2.setData(response.body().getBest());
                    adapter3.setData(response.body().getToday());

                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<homeBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        loadCart();

    }

    class BannerAdapter extends FragmentStatePagerAdapter {

        List<String> blist = new ArrayList<>();

        public BannerAdapter(@NonNull FragmentManager fm, int behavior, List<String> blist) {
            super(fm, behavior);
            this.blist = blist;
        }

        /*public BannerAdapter(FragmentManager fm, List<Banners> blist) {
            super(fm);
            this.blist = blist;
        }*/

        @Override
        public Fragment getItem(int position) {
            page frag = new page();
            frag.setData(blist.get(position));
            return frag;
        }

        @Override
        public int getCount() {
            return blist.size();
            //return 1;
        }
    }


    public static class page extends Fragment {

        String url, tit, cid = "", image2;

        ImageView image;

        void setData(String url) {
            this.url = url;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.banner_layout2, container, false);

            image = view.findViewById(R.id.imageView3);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url, image, options);


            return view;
        }
    }

    class BestAdapter extends RecyclerView.Adapter<BestAdapter.ViewHolder> {

        Context context;
        List<Best> list = new ArrayList<>();

        public BestAdapter(Context context, List<Best> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Best> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.best_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            final Best item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

            float dis = Float.parseFloat(item.getDiscount());

            final String nv1;

            if (item.getStock().equals("In stock")) {
                holder.add.setEnabled(true);
            } else {
                holder.add.setEnabled(false);
            }

            holder.stock.setText(item.getStock());
            holder.size.setText(item.getSize());

            if (dis > 0) {

                float pri = Float.parseFloat(item.getPrice());
                float dv = (dis / 100) * pri;

                float nv = pri - dv;

                nv1 = String.valueOf(nv);

                holder.discount.setVisibility(View.VISIBLE);
                holder.discount.setText(item.getDiscount() + "% OFF");
                holder.price.setText(Html.fromHtml("\u20B9 " + String.valueOf(nv)));
                holder.newamount.setText(Html.fromHtml("<strike>\u20B9 " + item.getPrice() + "</strike>"));
                holder.newamount.setVisibility(View.VISIBLE);
            } else {

                nv1 = item.getPrice();
                holder.discount.setVisibility(View.GONE);
                holder.price.setText("\u20B9 " + item.getPrice());
                holder.newamount.setVisibility(View.GONE);
            }


            holder.title.setText(item.getName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, SingleProduct.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("title", item.getName());
                    context.startActivity(intent);

                }
            });

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String uid = SharePreferenceUtils.getInstance().getString("userId");

                    if (uid.length() > 0) {

                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.add_cart_dialog);
                        dialog.show();

                        final StepperTouch stepperTouch = dialog.findViewById(R.id.stepperTouch);
                        Button add = dialog.findViewById(R.id.button8);
                        final ProgressBar progressBar = dialog.findViewById(R.id.progressBar2);


                        stepperTouch.setMinValue(1);
                        stepperTouch.setMaxValue(99);
                        stepperTouch.setSideTapEnabled(true);
                        stepperTouch.setCount(1);

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressBar.setVisibility(View.VISIBLE);

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

                                Log.d("userid", SharePreferenceUtils.getInstance().getString("userid"));
                                Log.d("pid", item.getId());
                                Log.d("quantity", String.valueOf(stepperTouch.getCount()));
                                Log.d("price", nv1);

                                int versionCode = com.nostra13.universalimageloader.BuildConfig.VERSION_CODE;
                                String versionName = com.nostra13.universalimageloader.BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId"), item.getId(), String.valueOf(stepperTouch.getCount()), nv1, versionName);

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            loadCart();
                                            dialog.dismiss();
                                        }

                                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                        progressBar.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });


                            }
                        });

                    } else {
                        Toast.makeText(context, "Please login to continue", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);

                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            TextView price, title, discount, stock, newamount, size;
            Button add;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView4);
                price = itemView.findViewById(R.id.textView11);
                title = itemView.findViewById(R.id.textView12);
                discount = itemView.findViewById(R.id.textView10);
                add = itemView.findViewById(R.id.button5);
                stock = itemView.findViewById(R.id.textView63);
                newamount = itemView.findViewById(R.id.textView6);
                size = itemView.findViewById(R.id.textView7);


            }
        }
    }

}
