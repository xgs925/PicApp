package com.xcommon.network;

import com.xcommon.utils.update.model.UpdateInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Photostsrs on 2016/11/10.
 */
public class Network {
    protected static String BASE_URL = "http://photostars-0.bj.1252228844.clb.myqcloud.com/index.php/photostars2_4/";
    public static String AUTH_KEY = "eae08fc1bd9e9eb738fe5f94fe9582c0";
    private static UpdateApi mUpdateApi;
    protected static int DEFAULT_TIMEOUT = 2;
    protected static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .build();
    protected  static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    protected  static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static TestAPI testAPI;

    public static TestAPI getTestAPI() {
        if (testAPI == null) {
            Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl("").addConverterFactory(gsonConverterFactory).build();
            testAPI = retrofit.create(TestAPI.class);
        }
        return testAPI;
    }

    public static UpdateApi getUpdateApi() {
        if (mUpdateApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            mUpdateApi = retrofit.create(UpdateApi.class);
        }
        return mUpdateApi;
    }

    public interface UpdateApi {
        @FormUrlEncoded
        @POST("generalFunction/versionAndroidVerify")
        Observable<UpdateInfo> getUpdateInfo(@Field("authorityKey") String authorityKey, @Field("versionNumber") String vresionNumber);
    }
}
