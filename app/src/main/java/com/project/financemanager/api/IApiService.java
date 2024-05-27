package com.project.financemanager.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.financemanager.dtos.CategoryDTO;
import com.project.financemanager.dtos.LoginResponse;
import com.project.financemanager.dtos.StatisticByCategoryDTO;
import com.project.financemanager.dtos.StatisticByDayDTO;
import com.project.financemanager.dtos.TitleTime;
import com.project.financemanager.dtos.Total;
import com.project.financemanager.dtos.UserDTO;
import com.project.financemanager.dtos.UserLogin;
import com.project.financemanager.dtos.WalletDTO;
import com.project.financemanager.models.Category;
import com.project.financemanager.models.Transaction;
import com.project.financemanager.models.User;
import com.project.financemanager.models.Wallet;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApiService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    String BASE_URL = "http://192.168.195.189:8081/api/v1/";

    @GET("transaction/monthandyear")
    Call<List<TitleTime>> getTransByMonthAndYear(@Query("month") int month, @Query("year") int year, @Query("walletId") long walletId);

    @GET("wallet/{id}")
    Call<Wallet> getWalletById(@Path("id") long walletId);

    @GET("wallet/mine")
    Call<List<Wallet>> getAllMyWallet();

    @GET("wallet/first")
    Call<Wallet> getFirstWallet();

    @PUT("wallet/update/first")
    Call<WalletDTO> createFirstWallet(@Body WalletDTO dataWallet);
    @POST("wallet/create")
    Call<Wallet> createWallet(@Body WalletDTO dataWallet);
    @PUT("wallet/update/{id}")
    Call<Wallet> updateWallet(@Path("id") int idWallet, @Body WalletDTO dataWallet);
    @DELETE("wallet/delete/{id}")
    Call<Void> deleteWallet(@Path("id") int idWallet);
    @GET("categories/parent/{type}")
    Call<List<Category>> getCategoryParents(@Path("type") String type);

    @GET("categories/outcome")
    Call<List<Category>> getAllOutcomeCategories();

    @GET("categories/income")
    Call<List<Category>> getAllIncomeCategories();
    @GET("transaction/all")
    Call<List<Transaction>> getAllTransaction();
    @POST("user/login")
    Call<LoginResponse> login(@Body UserLogin userLogin);

    @POST("user/register")
    Call<Void> register(@Body UserDTO dataUser);

    @GET("user/current")
    Call<User> getCurrentUser();

    @GET("transaction/statistic")
    Call<List<StatisticByDayDTO>> getStatisticByDay(@Query("when") String when);
    @GET("transaction/statistic/category")
    Call<List<StatisticByCategoryDTO>> getStatisticByCategory(@Query("start") String start, @Query("end") String end, @Query("type") String type);

    @GET("transaction/total")
    Call<Total> getTotalIncomeAndOutcome(@Query("month") int month, @Query("year") int year, @Query("walletId") long walletId);
    @POST("transaction/create/{type}")
    Call<Transaction> createTransaction(@Path("type") String typeTrans,@Body Transaction dataTrans);
    @PUT("transaction/update/{type}/{id}")
    Call<Transaction> updateTransaction(@Path("type") String typeTrans, @Path("id") int idTrans, @Body Transaction dataTrans);
    @PUT("transaction/delete/{type}/{id}")
    Call<Void> deleteTransaction(@Path("type") String typeTrans, @Path("id") int idTrans);

    @POST("categories/create/{type}")
    Call<CategoryDTO> createCategory(@Path("type") String typeCate, @Body CategoryDTO dataCate);
    @PUT("categories/update/{type}/{id}")
    Call<CategoryDTO> updateCategory(@Path("type") String typeCate, @Path("id") int idCate, @Body CategoryDTO dataCate);
    @PUT("categories/delete/{type}/{id}")
    Call<Void> deleteCategory(@Path("type") String typeCate, @Path("id") int idCate);


}
