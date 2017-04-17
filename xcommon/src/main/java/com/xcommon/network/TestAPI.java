package com.xcommon.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Photostsrs on 2016/11/10.
 */
public interface  TestAPI {
    @GET("users/{user}")
    Call<Test> userInfo(@Path("user") String user);
}
