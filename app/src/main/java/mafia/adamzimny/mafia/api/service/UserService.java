package mafia.adamzimny.mafia.api.service;

import mafia.adamzimny.mafia.model.User;
import mafia.adamzimny.mafia.model.UserTemplate;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by adamz on 18.08.2016.
 */
public interface UserService {

    @POST("/register")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<User> register(@Body UserTemplate userTemplate);

    @GET("/register/check/")
    Call<Boolean> checkUsername(@Query("username") String username);

}
