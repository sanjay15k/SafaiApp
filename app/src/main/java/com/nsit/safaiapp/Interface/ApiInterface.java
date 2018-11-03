package com.nsit.safaiapp.Interface;

import com.google.gson.JsonObject;
import com.nsit.safaiapp.DTO.DustbinsLocations;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("api/register")
    Call<JsonObject> registerUser(@Field("username") String username, @Field("email")
            String email, @Field("name") String name, @Field("phone") String phone, @Field("password") String password, @Field("aadhaar") String aadhaarNumber, @Field("license") String licenseNumber, @Field("address") String address);

    @FormUrlEncoded
    @POST("api/login")
    Call<JsonObject> loginUser(@Field("username") String username, @Field("password") String password);

    @GET("bins")
    Call<ArrayList<DustbinsLocations>> getDustbinsLocatons();

    @FormUrlEncoded
    @POST("schedulePickup")
    Call<JsonObject> schedulePickup(@Field("lat") double latitude, @Field("lon") double longitude, @Field("recyclable") double recyclableWeight, @Field("nonRecyclable") double nonRecyclableWeight, @Field("username") String username);

    @FormUrlEncoded
    @POST("complaint")
    Call<JsonObject> fileAComplaint(@Field("lat") double latitude, @Field("lon") double longitude, @Field("username") String username, @Field("comments") String comments);

    @FormUrlEncoded
    @POST("requestbin")
    Call<JsonObject> requestBin(@Field("location") String location, @Field("username") String username);

}

