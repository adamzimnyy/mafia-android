package mafia.adamzimny.mafia.api.service;

import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.model.User;
import mafia.adamzimny.mafia.model.json.AuthenticationRequest;
import mafia.adamzimny.mafia.model.json.AuthenticationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by adamz on 18.08.2016.
 */
public interface AuthService {

    @POST("/auth/")
    Call<AuthenticationResponse> authenticate(@Body AuthenticationRequest request);

    @GET("/auth/getUser/")
    Call<User> getUserFromToken(@Header("X-Auth-Token") String token);
}

