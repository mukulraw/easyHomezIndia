package com.technuoma.easyHomezIndia;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.nostra13.universalimageloader.BuildConfig;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.santalu.autoviewpager.AutoViewPager;
import com.technuoma.easyHomezIndia.homePOJO.Banners;
import com.technuoma.easyHomezIndia.homePOJO.Best;
import com.technuoma.easyHomezIndia.homePOJO.Cat;
import com.technuoma.easyHomezIndia.homePOJO.Member;
import com.technuoma.easyHomezIndia.homePOJO.Subcat;
import com.technuoma.easyHomezIndia.homePOJO.homeBean;
import com.technuoma.easyHomezIndia.seingleProductPOJO.singleProductBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import nl.dionsegijn.steppertouch.StepperTouch;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Home extends Fragment implements ResultCallback<LocationSettingsResult> {

    private static final String TAG = "Home";
    AutoViewPager pager;
    ProgressBar progress;
    RecyclerView categories, recent, loved, banner;
    BestAdapter adapter2, adapter3;
    BestAdapter adapter4;
    BestAdapter adapter5;
    BestAdapter adapter7;
    CategoryAdapter adapter6;
    List<Best> list;
    List<Best> list1;
    List<Best> list2;
    List<Cat> list3;
    List<Banners> list4;
    Button search, cate;
    OfferAdapter adapter;

    ImageView banner1, banner2, banner3;

    private FusedLocationProviderClient fusedLocationClient;

    String lat = "", lng = "";

    LocationSettingsRequest.Builder builder;
    LocationRequest locationRequest;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    static MainActivity mainActivity;

    ImageView image;
    Button upload;
    private Uri uri;
    private File f1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        mainActivity = (MainActivity) getActivity();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity);
        banner1 = view.findViewById(R.id.banner1);
        banner2 = view.findViewById(R.id.banner2);
        banner3 = view.findViewById(R.id.banner3);
        cate = view.findViewById(R.id.cate);
        banner = view.findViewById(R.id.banner);
        pager = view.findViewById(R.id.viewPager);
        pager.setPageMargin(20);
        progress = view.findViewById(R.id.progress);
        // indicator = findViewById(R.id.indicator);
        categories = view.findViewById(R.id.categories);
        loved = view.findViewById(R.id.loved);
        recent = view.findViewById(R.id.recent);
        search = view.findViewById(R.id.search);
        image = view.findViewById(R.id.imageView11);
        upload = view.findViewById(R.id.button3);

        list = new ArrayList<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();

        adapter = new OfferAdapter(mainActivity, list4);
        adapter2 = new BestAdapter(mainActivity, list);
        adapter3 = new BestAdapter(mainActivity, list);
        adapter4 = new BestAdapter(mainActivity, list1);
        adapter5 = new BestAdapter(mainActivity, list2);
        adapter6 = new CategoryAdapter(mainActivity, list3);
        adapter7 = new BestAdapter(mainActivity, list2);

        LinearLayoutManager manager1 = new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager2 = new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager3 = new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager4 = new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager manager6 = new LinearLayoutManager(mainActivity, RecyclerView.HORIZONTAL, false);
        GridLayoutManager manager5 = new GridLayoutManager(mainActivity, 1);
        GridLayoutManager manager7 = new GridLayoutManager(mainActivity, 1);

        recent.setAdapter(adapter2);
        recent.setLayoutManager(manager1);

        loved.setAdapter(adapter3);
        loved.setLayoutManager(manager2);


        categories.setAdapter(adapter6);
        categories.setLayoutManager(manager5);


        banner.setAdapter(adapter);
        banner.setLayoutManager(manager7);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mainActivity, Search.class);
                startActivity(intent);

            }
        });

        cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                FragmentTransaction ft4 = fm4.beginTransaction();
                Category frag14 = new Category();
                Bundle b = new Bundle();
                frag14.setArguments(b);
                ft4.replace(R.id.replace, frag14);
                ft4.addToBackStack(null);
                ft4.commit();

            }
        });


        progress.setVisibility(View.VISIBLE);
        createLocationRequest();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mainActivity);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
                            File newdir = new File(dir);
                            try {
                                newdir.mkdirs();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";


                            f1 = new File(file);
                            try {
                                f1.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            uri = FileProvider.getUriForFile(Objects.requireNonNull(mainActivity), com.technuoma.easyHomezIndia.BuildConfig.APPLICATION_ID + ".provider", f1);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 1);
                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uri != null) {
                    MultipartBody.Part body = null;
                    try {

                        RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                        body = MultipartBody.Part.createFormData("image", f1.getName(), reqFile1);


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

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


                    Call<loginBean> call = cr.uploadManual(SharePreferenceUtils.getInstance().getString("userId"), body);

                    call.enqueue(new Callback<loginBean>() {
                        @Override
                        public void onResponse(Call<loginBean> call, Response<loginBean> response) {

                            if (response.body().getStatus().equals("1")) {

                                onResume();
                                Toast.makeText(mainActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(mainActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<loginBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Toast.makeText(mainActivity, "Please add an image", Toast.LENGTH_SHORT).show();
                }


            }
        });

        return view;
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

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

        String getSpace(int position) {
            return list.get(position).getSpace();
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

            SubCategoryAdapter adapter = new SubCategoryAdapter(context, item.getSubcat());
            GridLayoutManager manager = new GridLayoutManager(context, 3);
            holder.gridl.setAdapter(adapter);
            holder.gridl.setLayoutManager(manager);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.hide.getVisibility() == View.VISIBLE) {
                        holder.hide.setVisibility(View.GONE);
                        holder.main.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    } else {
                        holder.hide.setVisibility(View.VISIBLE);
                        holder.main.setCardBackgroundColor(Color.parseColor("#FFF3E0"));

                        categories.smoothScrollToPosition(position);

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
            CardView hide, main;

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
            mainActivity.list = list;
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
            loader.displayImage(item.getImage(), holder.image, options);


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


    public void loaddata() {


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


                    BannerAdapter adapter1 = new BannerAdapter(getChildFragmentManager(), response.body().getPbanner());
                    pager.setAdapter(adapter1);

                    adapter2.setData(response.body().getBest());
                    adapter3.setData(response.body().getToday());
                    adapter4.setData(response.body().getBest());
                    adapter5.setData(response.body().getToday());
                    adapter7.setData(response.body().getToday());
                    adapter6.setData(response.body().getCat());

                    Log.d("ssiizzee", String.valueOf(response.body().getObanner().size()));


                    try {
                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        String url = response.body().getObanner().get(0).getImage();
                        loader.displayImage(url, banner1, options);

                        String cid = response.body().getObanner().get(0).getCid();
                        String tit = response.body().getObanner().get(0).getCname();
                        String image = response.body().getObanner().get(0).getCatimage();

                        banner1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cid != null) {
                                    Intent intent = new Intent(mainActivity, SubCat.class);
                                    intent.putExtra("id", cid);
                                    intent.putExtra("title", tit);
                                    intent.putExtra("image", image);
                                    startActivity(intent);
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        String url = response.body().getObanner().get(1).getImage();
                        loader.displayImage(url, banner2, options);

                        String cid = response.body().getObanner().get(1).getCid();
                        String tit = response.body().getObanner().get(1).getCname();
                        String image = response.body().getObanner().get(1).getCatimage();

                        banner2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cid != null) {
                                    Intent intent = new Intent(mainActivity, SubCat.class);
                                    intent.putExtra("id", cid);
                                    intent.putExtra("title", tit);
                                    intent.putExtra("image", image);
                                    startActivity(intent);
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        String url = response.body().getObanner().get(2).getImage();
                        loader.displayImage(url, banner3, options);

                        String cid = response.body().getObanner().get(2).getCid();
                        String tit = response.body().getObanner().get(2).getCname();
                        String image = response.body().getObanner().get(2).getCatimage();

                        banner3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (cid != null) {
                                    Intent intent = new Intent(mainActivity, SubCat.class);
                                    intent.putExtra("id", cid);
                                    intent.putExtra("title", tit);
                                    intent.putExtra("image", image);
                                    startActivity(intent);
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (response.body().getObanner().size() > 3) {
                        List<Banners> ll = response.body().getObanner();
                        ll.remove(0);
                        ll.remove(0);
                        ll.remove(0);
                        adapter.setData(ll);
                    }

                    SharePreferenceUtils.getInstance().saveString("location", response.body().getLocation());


                    Geocoder geocoder = new Geocoder(mainActivity, Locale.getDefault());
                    List<android.location.Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(Double.parseDouble(SharePreferenceUtils.getInstance().getString("lat")), Double.parseDouble(SharePreferenceUtils.getInstance().getString("lng")), 1);
                        mainActivity.location.setText(addresses.get(0).getAddressLine(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Log.d("address", addresses.toString());


                }

                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<homeBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        mainActivity.loadCart();
    }


    class BannerAdapter extends FragmentStatePagerAdapter {

        List<Banners> blist = new ArrayList<>();

        public BannerAdapter(FragmentManager fm, List<Banners> blist) {
            super(fm);
            this.blist = blist;
        }

        @Override
        public Fragment getItem(int position) {
            page frag = new page();
            frag.setData(blist.get(position).getImage(), blist.get(position).getCname(), blist.get(position).getCid(), blist.get(position).getCatimage());
            return frag;
        }

        @Override
        public int getCount() {
            return blist.size();
        }
    }


    public static class page extends Fragment {

        String url, tit, cid = "", image2;

        ImageView image;

        void setData(String url, String tit, String cid, String image2) {
            this.url = url;
            this.tit = tit;
            this.cid = cid;
            this.image2 = image2;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.banner_layout, container, false);

            image = view.findViewById(R.id.imageView3);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(url, image, options);


            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cid != null) {

                        FragmentManager fm4 = mainActivity.getSupportFragmentManager();

                        FragmentTransaction ft4 = fm4.beginTransaction();
                        SubCat frag14 = new SubCat();
                        Bundle b = new Bundle();
                        b.putString("id", cid);
                        b.putString("title", tit);
                        b.putString("image", image2);
                        frag14.setArguments(b);
                        ft4.replace(R.id.replace, frag14);
                        ft4.addToBackStack(null);
                        ft4.commit();

                        /*Intent intent = new Intent(getContext(), SubCat.class);
                        intent.putExtra("id", cid);
                        intent.putExtra("title", tit);
                        intent.putExtra("image", image2);
                        startActivity(intent);*/
                    }


                }
            });


            return view;
        }
    }

    class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {

        Context context;
        List<Banners> list = new ArrayList<>();

        public OfferAdapter(Context context, List<Banners> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Banners> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.best_list_model1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Banners item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getImage(), holder.image, options);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (item.getCid() != null) {
                        Intent intent = new Intent(context, SubCat.class);
                        intent.putExtra("id", item.getCid());
                        intent.putExtra("title", item.getCname());
                        intent.putExtra("image", item.getCatimage());
                        startActivity(intent);
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

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView4);


            }
        }
    }

    class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

        Context context;
        List<Member> list = new ArrayList<>();

        public MemberAdapter(Context context, List<Member> list) {
            this.context = context;
            this.list = list;
        }

        public void setData(List<Member> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.member_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Member item = list.get(position);


            holder.duration.setText(item.getDuration());
            holder.price.setText("\u20B9 " + item.getPrice());


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView duration, price, discount;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                duration = itemView.findViewById(R.id.textView13);
                price = itemView.findViewById(R.id.textView15);
                discount = itemView.findViewById(R.id.textView14);


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

                                Log.d("userid", SharePreferenceUtils.getInstance().getString("userid"));
                                Log.d("pid", item.getId());
                                Log.d("quantity", String.valueOf(stepperTouch.getCount()));
                                Log.d("price", nv1);

                                int versionCode = com.nostra13.universalimageloader.BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;

                                Call<singleProductBean> call = cr.addCart(SharePreferenceUtils.getInstance().getString("userId"), item.getId(), String.valueOf(stepperTouch.getCount()), nv1, versionName);

                                call.enqueue(new Callback<singleProductBean>() {
                                    @Override
                                    public void onResponse(Call<singleProductBean> call, Response<singleProductBean> response) {

                                        if (response.body().getStatus().equals("1")) {
                                            //loadCart();
                                            dialog.dismiss();
                                            mainActivity.loadCart();
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


    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(mainActivity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(mainActivity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getLocation();
            }
        });

        task.addOnFailureListener(mainActivity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but mainActivity can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(mainActivity,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }


    void getLocation() {
        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                        lat = String.valueOf(location.getLatitude());
                        lng = String.valueOf(location.getLongitude());

                        SharePreferenceUtils.getInstance().saveString("lat", lat);
                        SharePreferenceUtils.getInstance().saveString("lng", lng);

                        Log.d("lat123", lat);

                        LocationServices.getFusedLocationProviderClient(mainActivity).removeLocationUpdates(this);

                        loaddata();

                    }
                }
            }
        };

        LocationServices.getFusedLocationProviderClient(mainActivity).requestLocationUpdates(locationRequest, mLocationCallback, null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        getLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(mainActivity, "Location is required for mainActivity app", Toast.LENGTH_LONG).show();
                        mainActivity.finishAffinity();
                        break;
                }
                break;
        }

        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            uri = data.getData();

            Log.d("uri", String.valueOf(uri));

            String ypath = getPath(mainActivity, uri);
            assert ypath != null;


            File file;
            file = new File(ypath);

            try {
                f1 = new Compressor(Objects.requireNonNull(mainActivity)).compressToFile(file);

                uri = Uri.fromFile(f1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("path1", ypath);

            image.setImageURI(uri);


        } else if (requestCode == 1 && resultCode == RESULT_OK) {

            Log.d("uri1", String.valueOf(uri));

            try {

                f1 = new Compressor(mainActivity).compressToFile(f1);

                uri = Uri.fromFile(f1);

            } catch (Exception e1) {
                e1.printStackTrace();
            }

            image.setImageURI(uri);


        }


    }

    private static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        final String column = "_data";
        final String[] projection = {
                column
        };
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }
        return null;
    }

}
