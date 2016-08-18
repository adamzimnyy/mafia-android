package mafia.adamzimny.mafia.api;

import com.google.gson.*;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.util.Date;

public class RetrofitBuilder {
    private static boolean useLocalhost = false;
    public static final String BASE_URL = "http://mafia-test-env-java.eu-central-1.elasticbeanstalk.com/";

    public static final String LOCALHOST = "localhost:8080/";

    public static Retrofit  build() {
        GsonBuilder builder = new GsonBuilder();

// Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        Gson gson = builder.create();



        return new Retrofit.Builder()
                .baseUrl(useLocalhost ? LOCALHOST : BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static Object getService(Class<?> clas){
        return build().create(clas);
    }
}
