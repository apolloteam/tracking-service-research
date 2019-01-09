package com.prueba.conex;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Headers;

public interface TrasladaTrackerService {

    @Headers("Content-Type:application/json")
    @POST("trackingpositions/data")
    public Call<Void> trackPosition(@Body String body);
}
