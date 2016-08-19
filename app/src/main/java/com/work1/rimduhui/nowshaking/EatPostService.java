package com.work1.rimduhui.nowshaking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by rimduhui on 2016. 8. 17..
 */
public interface EatPostService {
  @POST("data")
  Call<String> postData(@Body String data);
}
