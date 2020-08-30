package com.technuoma.easyHomezIndia;

import com.technuoma.easyHomezIndia.addressPOJO.addressBean;
import com.technuoma.easyHomezIndia.cartPOJO.cartBean;
import com.technuoma.easyHomezIndia.checkPromoPOJO.checkPromoBean;
import com.technuoma.easyHomezIndia.checkoutPOJO.checkoutBean;
import com.technuoma.easyHomezIndia.homePOJO.homeBean;
import com.technuoma.easyHomezIndia.orderDetailsPOJO.orderDetailsBean;
import com.technuoma.easyHomezIndia.ordersPOJO.ordersBean;
import com.technuoma.easyHomezIndia.productsPOJO.productsBean;
import com.technuoma.easyHomezIndia.searchPOJO.searchBean;
import com.technuoma.easyHomezIndia.seingleProductPOJO.singleProductBean;
import com.technuoma.easyHomezIndia.subCat1POJO.subCat1Bean;

import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {

    @Multipart
    @POST("easyhomez/api/getHome2.php")
    Call<homeBean> getHome(
            @Part("lat") String lat,
            @Part("lng") String lng
    );

    @Multipart
    @POST("easyhomez/api/getSubCat1.php")
    Call<subCat1Bean> getSubCat1(
            @Part("cat") String cat
    );

    @Multipart
    @POST("easyhomez/api/getSubCat2.php")
    Call<subCat1Bean> getSubCat2(
            @Part("subcat1") String cat
    );

    @Multipart
    @POST("easyhomez/api/getProducts.php")
    Call<productsBean> getProducts(
            @Part("subcat2") String cat,
            @Part("location_id") String location_id
    );

    @Multipart
    @POST("easyhomez/api/getProductById.php")
    Call<singleProductBean> getProductById(
            @Part("id") String cat,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("easyhomez/api/search.php")
    Call<searchBean> search(
            @Part("query") String query,
            @Part("location_id") String location_id
    );

    @Multipart
    @POST("easyhomez/api/login.php")
    Call<loginBean> login(
            @Part("name") String name,
            @Part("email") String email,
            @Part("phone") String phone,
            @Part("token") String token,
            @Part("referrer") String referrer
    );

    @Multipart
    @POST("easyhomez/api/verify.php")
    Call<loginBean> verify(
            @Part("phone") String phone,
            @Part("otp") String otp
    );

    @Multipart
    @POST("easyhomez/api/addCart.php")
    Call<singleProductBean> addCart(
            @Part("user_id") String user_id,
            @Part("product_id") String product_id,
            @Part("quantity") String quantity,
            @Part("unit_price") String unit_price,
            @Part("version") String version
    );

    @Multipart
    @POST("easyhomez/api/addWishlist.php")
    Call<singleProductBean> addWishlist(
            @Part("user_id") String user_id,
            @Part("product_id") String product_id
    );

    @Multipart
    @POST("easyhomez/api/removeWishlist.php")
    Call<singleProductBean> removeWishlist(
            @Part("user_id") String user_id,
            @Part("product_id") String product_id
    );

    @Multipart
    @POST("easyhomez/api/updateCart.php")
    Call<singleProductBean> updateCart(
            @Part("id") String id,
            @Part("quantity") String quantity,
            @Part("unit_price") String unit_price
    );

    @Multipart
    @POST("easyhomez/api/deleteCart.php")
    Call<singleProductBean> deleteCart(
            @Part("id") String id
    );

    @Multipart
    @POST("easyhomez/api/getRew.php")
    Call<String> getRew(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("easyhomez/api/clearCart.php")
    Call<singleProductBean> clearCart(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("easyhomez/api/getOrderDetails.php")
    Call<orderDetailsBean> getOrderDetails(
            @Part("order_id") String order_id
    );

    @Multipart
    @POST("easyhomez/api/getWishlist.php")
    Call<orderDetailsBean> getWishlist(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("easyhomez/api/getCart.php")
    Call<cartBean> getCart(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("easyhomez/api/getOrders.php")
    Call<ordersBean> getOrders(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("easyhomez/api/getAddress.php")
    Call<addressBean> getAddress(
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("easyhomez/api/deleteAddress.php")
    Call<addressBean> deleteAddress(
            @Part("id") String id
    );

    @Multipart
    @POST("easyhomez/api/checkPromo.php")
    Call<checkPromoBean> checkPromo(
            @Part("promo") String promo,
            @Part("user_id") String user_id
    );

    @Multipart
    @POST("easyhomez/api/buyVouchers.php")
    Call<checkoutBean> buyVouchers(
            @Part("user_id") String user_id,
            @Part("lat") String lat,
            @Part("lng") String lng,
            @Part("amount") String amount,
            @Part("txn") String txn,
            @Part("name") String name,
            @Part("address") String address,
            @Part("pay_mode") String pay_mode,
            @Part("slot") String slot,
            @Part("date") String date,
            @Part("promo") String promo,
            @Part("house") String house,
            @Part("area") String area,
            @Part("city") String city,
            @Part("pin") String pin,
            @Part("isnew") String isnew
    );
}
