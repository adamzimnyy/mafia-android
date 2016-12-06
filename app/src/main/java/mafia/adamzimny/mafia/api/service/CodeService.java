package mafia.adamzimny.mafia.api.service;

import mafia.adamzimny.mafia.model.Code;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by adamz on 19.09.2016.
 */
public interface CodeService {

    @POST("/api/code")
    Call<String> createNewCode(@Header("X-Auth-Token") String token, @Body Code code);

}