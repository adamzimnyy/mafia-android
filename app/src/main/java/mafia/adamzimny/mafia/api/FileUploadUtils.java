package mafia.adamzimny.mafia.api;

import android.net.Uri;
import android.util.Log;
import mafia.adamzimny.mafia.api.service.UserService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;

/**
 * Created by adamz on 18.08.2016.
 */
public class FileUploadUtils {

    private void uploadFile(File file) {
        // create upload service client
        UserService service =
                (UserService) RetrofitBuilder.getService(UserService.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        //  File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // finally, execute the request
        Call<String> call = service.uploadFile(body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call,
                                   Response<String> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
}
