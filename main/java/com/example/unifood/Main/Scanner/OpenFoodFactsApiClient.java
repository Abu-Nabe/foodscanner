package com.example.unifood.Main.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OpenFoodFactsApiClient
{
    private static final String API_ENDPOINT = "https://world.openfoodfacts.org/api/v0/product/";

    public static JSONObject getProductInfo(String barcode) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(API_ENDPOINT + barcode + ".json")
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            JSONObject productJson = new JSONObject(responseBody);
            return productJson.getJSONObject("product");
        } else {
            throw new IOException("Unexpected response code: " + response.code());
        }
    }
}
