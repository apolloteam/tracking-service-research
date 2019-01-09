package com.prueba.conex;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TrasladaTrackerService {

    @POST("trackingpositions/data")
    public Call<Void> trackPosition(@Body String body);
}
