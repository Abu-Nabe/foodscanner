package com.example.unifood.Main.PushNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.unifood.Main.Utils.PreferencesUtils;
import com.example.unifood.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    ApiService apiService;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Send Firebase notification
        // ...
        apiService = Client.getClient("https://fcm.googleapis.com/").create(ApiService.class);
        // can change it using intent.getStringExtra to get data from tables, but wanna save time for now
        String body = "Come check if your food is expiring";
        String title = "Food Expiring";
        sendFirebaseNotification(context, body, title);
    }

    private void sendFirebaseNotification(Context context, String body, String title)
    {
        String token = PreferencesUtils.getToken(context);
        Data data = new Data(R.drawable.ic_launcher_foreground, body, title);

        Sender sender = new Sender(data, token);

        apiService.sendNotification(sender)
                .enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                                    Toast.makeText(TextActivity.this, ""+response.message(), Toast.LENGTH_SHORT).show();
                        Log.d("XAS", response.toString());
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Log.d("XAS", t.toString());
                    }
                });
    }
}

