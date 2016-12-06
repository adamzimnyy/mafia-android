package mafia.adamzimny.mafia.api;

import android.provider.ContactsContract;
import com.google.gson.*;
import mafia.adamzimny.mafia.api.service.ImgurService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.util.Date;

public class RetrofitBuilder {
    private static boolean useLocalhost = false;
    public static final String BASE_URL = "http://mafia-test-env-java.eu-central-1.elasticbeanstalk.com/";
    public static final String IMGUR_URL = "https://api.imgur.com/";
    public static final String LOCALHOST = "http://192.168.0.103:8080";

    public static Retrofit build(String url) {
        GsonBuilder builder = new GsonBuilder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = builder.setDateFormat("yyyy-MM-dd HH:mm:ss Z").
        create();
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static Object getService(Class<?> clas, String url) {
        if(clas == ImgurService.class)
            return build(url).create(clas);
        return build(useLocalhost ? LOCALHOST : url).create(clas);
    }

}
