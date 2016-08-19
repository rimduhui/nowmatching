package com.work1.rimduhui.nowshaking;

import java.net.URL;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rimduhui on 2016. 8. 17..
 */
public interface SlackGetService {
  @GET("api/chat.postMessage")
  Call<Objects> sendQuery(@Query("token") String token , @Query("channel") String channel, @Query("text") String msg,
                          @Query("username") String name, @Query("icon_url") URL url);

}
