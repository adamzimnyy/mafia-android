package mafia.adamzimny.mafia.api.service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by adamz on 18.08.2016.
 */
public interface UserService {

    @Multipart
    @POST("register/pic")
    Call<String> uploadFile( @Part MultipartBody.Part file);
}
