package com.assignment.rapido.util;


import com.assignment.rapido.domain.DirectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RapidoMapAPI
{
    @GET("directions/json")
    Call<DirectionResponse> LoadRoutes(@Query("origin") String Start , @Query("destination") String End ,
                                       @Query("key") String ApiKey , @Query("alternatives") boolean Alternatives );
}
