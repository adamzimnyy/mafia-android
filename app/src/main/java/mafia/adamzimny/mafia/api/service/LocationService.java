package mafia.adamzimny.mafia.api.service;

import mafia.adamzimny.mafia.model.Location;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by adamz on 18.08.2016.
 */
public interface LocationService {

    @GET("/api/location/user/{id}")
    Call<List<Location>> findByUser(@Path("id") long id, @Header("X-Auth-Token") String token);

    @GET("/api/location/user/{id}/{date}")
    Call<List<Location>> findByUserAndDateAfter(@Path("id") long id,@Path("date") long date, @Header("X-Auth-Token") String token);

    @POST("/api/location/user/{id}")
    Call<String> save(@Body Location location, @Header("X-Auth-Token") String token);

}
