package com.example.unifood.Main.PushNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAATIznCdc:APA91bE4BAog2RuisISMaOTSSEaZfOwEXm3ljXtQBLqSw7m-O8-pAjl1mVpxvqQH-mcEmXv3nJsU-lxaeYTWoyUUoByQaRKekGmJII1MbkdN7YZ0YLzlXawdjLvhh7rvbvU7YfYNkctc"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}