package com.hackday.play.api;

import android.arch.lifecycle.LiveData;

import com.hackday.play.utils.LiveDataCallAdapterFactory;
import com.hackday.play.bean.LoginResponse;
import com.hackday.play.bean.NeedInfo;
import com.hackday.play.bean.NeedList;
import com.hackday.play.bean.StatusInfo;
import com.hackday.play.bean.UserInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by victor on 10/10/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public class UserApi {
    private static UserApi instance;
    private UserApiService mUserApiService;

    public static UserApi getInstance() {
        if (instance == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            instance = new UserApi(client);
        }
        return instance;
    }

    public UserApi(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://121.201.69.178:8888/api/v1/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        mUserApiService = retrofit.create(UserApiService.class);
    }

    public Observable<StatusInfo> register(String name, String phone, String password,
                                           String qq) {
        return mUserApiService.register(new JsonRequestBody().setUsername(name)
                .setPassword(password).setPhone(phone).setQq(qq).create());
    }

    public Observable<LoginResponse> login(String phone, String password) {
        return mUserApiService.login(phone, new JsonRequestBody().setPassword(password).addClient
                ().create());
    }

    public Observable<StatusInfo> editUser(String phone, String token, String name, String
            new_phone,
                                           String qq) {
        return mUserApiService.editUser(phone, token, new JsonRequestBody().setUsername(name)
                .setPhone(new_phone).setQq(qq).create());
    }

    public LiveData<ApiResponse<UserInfo>> getUserInfo(String phone, String token) {
        return mUserApiService.getUserInfo(phone, token);
    }

    public Observable<NeedList> getAllNeeds(String header) {
        return mUserApiService.getAllNeeds(header);
    }

    public Observable<NeedInfo> addNeed(String creator_phone, String desc, String continue_time,
                                        String sex, float longitude, float latitude, String
                                                location, String destination, long created_time,
                                        String token) {
        return mUserApiService.addNeed(token, new JsonRequestBody().setCreatorPhone(creator_phone)
                .setDesc(desc).setContinue(continue_time).setSex(sex).setLongitude(longitude)
                .setLatitude(latitude).setLocation(location).setCreatedTime(created_time)
                .setDestination(destination).create());

    }

    public Observable<NeedInfo> getNeedInfo(String id, String token) {
        return mUserApiService.getNeedInfo(token, id);
    }

    public Observable<NeedInfo> helpNeed(String id, String helperPhone, String token) {
        return mUserApiService.changeNeedInfo(token, id, new JsonRequestBody().setHeplerPhone
                (helperPhone).setStatus(1).create());
    }

    public Observable<NeedInfo> finishedNeed(String id, String token) {
        return mUserApiService.changeNeedInfo(token, id, new JsonRequestBody().setStatus(2)
                .create());
    }

    public Observable<UserInfo> changeLoveLevel(String phone, String token) {
        return mUserApiService.changeLoveLevel(token, phone);
    }

    public Observable<StatusInfo> deleteNeed(String id, String token) {
        return mUserApiService.deleteNeed(token, id);
    }
}
