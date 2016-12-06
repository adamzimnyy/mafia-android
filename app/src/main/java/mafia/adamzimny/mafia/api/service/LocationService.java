package mafia.adamzimny.mafia.api.service;

import mafia.adamzimny.mafia.model.Location;
import mafia.adamzimny.mafia.util.RetroDate;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Date;
import java.util.List;

/**
 * Created by adamz on 18.08.2016.
 */
public interface LocationService {

    @GET("/api/location")
    Call<List<Location>> findByUser(@Query("user") long id, @Header("X-Auth-Token") String token);

    @GET("/api/location")
    Call<List<Location>> findByUserAndDateAfter(@Query("user") long id, @Query("date") RetroDate date, @Header("X-Auth-Token") String token);

    @POST("/api/location")
    Call<Void> save(@Body Location location, @Header("X-Auth-Token") String token);

}
