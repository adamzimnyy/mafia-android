package mafia.adamzimny.mafia.api.service;

import mafia.adamzimny.mafia.model.Target;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

import java.util.List;

/**
 * Created by adamz on 18.08.2016.
 */
public interface TargetService {

    @GET("api/target/{id}")
    Call<List<Target>> getTargetsForUser(@Path("id") long id, @Header("X-Auth-Token") String token);
}
