package com.example.h.classattendance.Retrofit;

import com.example.h.classattendance.DBModel.PostRes;

import java.lang.reflect.Array;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IEndpoints {

    @GET("alpha/secusers")
    Call <StaffList> getStaffDetails();

    @POST("alpha/classattend")
    @FormUrlEncoded
    Call<PostRes> postDetails (@Field("course") String course, @Field("time") String time, @Field("matricno") String matricno);
}
