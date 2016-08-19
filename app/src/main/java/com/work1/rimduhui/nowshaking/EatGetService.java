package com.work1.rimduhui.nowshaking;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by rimduhui on 2016. 8. 17..
 */
public interface EatGetService {
  @GET("./")
  Call<String> getString();
}
