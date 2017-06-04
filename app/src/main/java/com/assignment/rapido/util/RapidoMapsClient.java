package com.assignment.rapido.util;


import android.util.Log;

import com.assignment.rapido.domain.DirectionResponse;
import com.assignment.rapido.rapidomap.MapsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RapidoMapsClient implements Callback<DirectionResponse>
{

    static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    public void GetJsonResponse(String Start ,  String End , String ApiKey )
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RapidoMapAPI RapidoAPI = retrofit.create(RapidoMapAPI.class);

        Call<DirectionResponse> call = RapidoAPI.LoadRoutes(Start , End , ApiKey , true);
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> Response) {
        if(Response.isSuccessful()) {
            DirectionResponse directionResponse = Response.body();
            Log.i("Routes" , directionResponse.toString());

            MapsActivity.DrawPolyLines(directionResponse);
            //changesList.forEach(change -> System.out.println(change.subject));
        } else {
            Log.i("Error" ,Response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(Call<DirectionResponse> call, Throwable t)
    {
        t.printStackTrace();
    }
}
