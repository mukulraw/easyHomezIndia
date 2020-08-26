package com.technuoma.easyHomezIndia;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.technuoma.easyHomezIndia.cartPOJO.Datum;
import com.technuoma.easyHomezIndia.cartPOJO.cartBean;
import com.technuoma.easyHomezIndia.homePOJO.Best;
import com.technuoma.easyHomezIndia.homePOJO.homeBean;
import com.technuoma.easyHomezIndia.seingleProductPOJO.singleProductBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.steppertouch.StepperTouch;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Cart extends AppCompatActivity {

    private Toolbar toolbar;
    ProgressBar bar;
    String base;
    TextView btotal, bproceed, clear;

    float amm = 0;

    View bottom;

    CartAdapter adapter;

    GridLayoutManager manager;

    RecyclerView grid;

    List<Datum> list;

    String client, txn;

    RecyclerView recent;
    List<Best> list2;
    BestAdapter adapter2;

    TextView sp, grand;

    TextView fifty, ninetynine, twentyfive;
    EditText tip;

    float tt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        list = new ArrayList<>();
        list2 = new ArrayList<>();

        fifty = findViewById(R.id.textView85);
        ninetynine = findViewById(R.id.textView86);
        twentyfive = findViewById(R.id.textView87);
        tip = findViewById(R.id.editTextNumber);
        sp = findViewById(R.id.textView76);
        grand = findViewById(R.id.textView80);
        recent = findViewById(R.id.recent);
        toolbar = findViewById(R.id.toolbar3);
        bar = findViewById(R.id.progressBar3);
        bottom = findViewById(R.id.cart_bottom);
        btotal = findViewById(R.id.textView10);
        bproceed = findViewById(R.id.textView9);
        grid = findViewById(R.id.grid);
        clear = findViewById(R.id.textView12);
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
        toolbar.setTitle("Cart");

        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tip.setText("50");

            }
        });

        ninetynine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tip.setText("99");

            }
        });


        twentyfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tip.setText("25");

            }
        });

        tip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    tt = Float.parseFloat(charSequence.toString());
                } else {
                    tt = 0;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adapter2 = new BestAdapter(this, list2);
        LinearLayoutManager manager1 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        recent.setAdapter(adapter2);
        recent.setLayoutManager(manager1);


        adapter = new CartAdapter(list, this);

        manager = new GridLayoutManager(this, 1);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bar.setVisibility(View.VISIBLE);

                Bean b = (Bean) getApplicationContext();

                base = b.baseurl;

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                Call<singleProductBean> call = cr.clearCart(SharePreferenceUtils.getInstance().getString("userId"));

                call.enqueue(new Callback<singleProductBean>() {
                    @Override
                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                        if (response.body().getStatus().equals("1")) {
                            finish();
                        }

                        Toast.makeText(Cart.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        bar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<singleProductBean> call, Throwable t) {
                        bar.setVisibility(View.GONE);
                    }
                });


            }
        });

        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (amm > 0) {
                    Intent intent = new Intent(Cart.this, Checkout.class);
                    intent.putExtra("amount", String.valueOf(amm));
                    startActivity(intent);
                } else {
                    Toast.makeText(Cart.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadCart();

    }

    void loadCart() {
        bar.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        base = b.baseurl;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Log.d("userid", SharePreferenceUtils.getInstance().getString("userId"));


        Call<cartBean> call = cr.getCart(SharePreferenceUtils.getInstance().getString("userId"));
        call.enqueue(new Callback<cartBean>() {
            @Override
            public void onResponse(Call<cartBean> call, Response<cartBean> response) {

                if (response.body().getData().size() > 0) {


                    adapter.setgrid(response.body().getData());

                    amm = Float.parseFloat(response.body().getTotal());


                    btotal.setText("Total: \u20B9" + response.body().getTotal());
                    sp.setText("\u20B9" + response.body().getTotal());
                    grand.setText("\u20B9" + response.body().getTotal());

                    bottom.setVisibility(View.VISIBLE);
                } else {
                    adapter.setgrid(response.body().getData());
                    bottom.setVisibility(View.GONE);
                    Toast.makeText(Cart.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                    finish();
                }

                bar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<cartBean> call, Throwable t) {
                bar.setVisibility(View.GONE);
            }
        });

        Call<homeBean> call2 = cr.getHome(SharePreferenceUtils.getInstance().getString("lat"), SharePreferenceUtils.getInstance().getString("lng"));
        call2.enqueue(new Callback<homeBean>() {
            @Override
            public void onResponse(Call<homeBean> call, Response<homeBean> response) {


                if (response.body().getStatus().equals("1")) {

                    adapter2.setData(response.body().getBest());
                }


            }

            @Override
            public void onFailure(Call<homeBean> call, Throwable t) {

            }
        });

    }

    class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

        List<Datum> list = new ArrayList<>();
        Context context;
        LayoutInflater inflater;

        CartAdapter(List<Datum> list, Context context) {
            this.context = context;
            this.list = list;
        }

        void setgrid(List<Datum> list) {

            this.list = list;
            notifyDataSetChanged();

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.prod_list_model4, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i1) {

            final Datum item = list.get(i1);

            //viewHolder.setIsRecyclable(false);


            viewHolder.title.setText(item.getName());
            viewHolder.brand.setText(item.getBrand());


            viewHolder.quantity.setText(item.getQuantity());

            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int q = Integer.parseInt(item.getQuantity());

                    if (q < 99) {

                        q++;

                        viewHolder.quantity.setText(String.valueOf(q));

                        bar.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();

                        base = b.baseurl;

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Call<singleProductBean> call = cr.updateCart(item.getPid(), String.valueOf(q), item.getPrice());

                        call.enqueue(new Callback<singleProductBean>() {
                            @Override
                            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                if (response.body().getStatus().equals("1")) {
                                    loadCart();
                                }

                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                bar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<singleProductBean> call, Throwable t) {
                                bar.setVisibility(View.GONE);
                            }
                        });

                    }


                }
            });


            viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int q = Integer.parseInt(item.getQuantity());

                    if (q > 1) {

                        q--;

                        viewHolder.quantity.setText(String.valueOf(q));

                        bar.setVisibility(View.VISIBLE);

                        Bean b = (Bean) getApplicationContext();

                        base = b.baseurl;

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(b.baseurl)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                        Call<singleProductBean> call = cr.updateCart(item.getPid(), String.valueOf(q), item.getPrice());

                        call.enqueue(new Callback<singleProductBean>() {
                            @Override
                            public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                if (response.body().getStatus().equals("1")) {
                                    loadCart();
                                }

                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                bar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onFailure(Call<singleProductBean> call, Throwable t) {
                                bar.setVisibility(View.GONE);
                            }
                        });

                    }


                }
            });


            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    bar.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    base = b.baseurl;

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<singleProductBean> call = cr.deleteCart(item.getPid());

                    call.enqueue(new Callback<singleProductBean>() {
                        @Override
                        public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                            if (response.body().getStatus().equals("1")) {
                                loadCart();
                            }

                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            bar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<singleProductBean> call, Throwable t) {
                            bar.setVisibility(View.GONE);
                            Log.d("error", t.toString());
                        }
                    });

                }
            });


            viewHolder.price.setText("\u20B9 " + item.getPrice());


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), viewHolder.imageView, options);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            ImageButton delete;

            Button add, remove;
            TextView quantity, title, brand, price;


            ViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.image);
                //buy = itemView.findViewById(R.id.play);


                delete = itemView.findViewById(R.id.delete);

                add = itemView.findViewById(R.id.increment);
                remove = itemView.findViewById(R.id.decrement);
                quantity = itemView.findViewById(R.id.display);
                title = itemView.findViewById(R.id.textView20);
                brand = itemView.findViewById(R.id.textView21);
                price = itemView.findViewById(R.id.textView22);

                //buy.setSideTapEnabled(true);

                //name = itemView.findViewById(R.id.name);


            }
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
                                            //loadCart();
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
