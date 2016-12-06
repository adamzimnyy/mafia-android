package mafia.adamzimny.mafia.api.service;

import mafia.adamzimny.mafia.model.Target;
import mafia.adamzimny.mafia.model.json.TargetConfirmation;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by adamz on 18.08.2016.
 */
public interface TargetService {

    @GET("api/target")
    Call<List<Target>> getTargetsForUser(@Query("user") long id, @Header("X-Auth-Token") String token);

    @PATCH("api/target/confirm")
    Call<Target> confirm(@Query("target")Integer target, @Query("code") String code, @Header("X-Auth-Token") String token);

    @GET("api/target")
    Call<Target> getTargetById(@Query("id") long id, @Header("X-Auth-Token") String token);
}
