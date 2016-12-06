package mafia.adamzimny.mafia.api.service;

import mafia.adamzimny.mafia.api.ProgressRequestBody;
import mafia.adamzimny.mafia.model.json.ImgurData;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by adamz on 19.08.2016.
 */
public interface ImgurService {

    @Multipart
    @POST("3/image")
    @Headers("Authorization: Client-ID 9df1a76680eff2a")
    Call<ImgurData> uploadImage(@Part("image") ProgressRequestBody image);
}
