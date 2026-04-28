package agrotech.client.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Proporciona una instància única de Retrofit.
 *
 * <p>
 * Centralitza la configuració de la connexió amb el backend i la conversió
 * automàtica de JSON mitjançant Gson.</p>
 *
 * @author Javier Santana Coronado
 */
public class RetrofitProvider {

    private static final String BASE_URL
            = "https://a2001678-f14b-4a5a-8211.04a74656c25a.bastion.elmeuescriptori.cat/";

    private static Retrofit retrofit;

    /**
     * Retorna la instància única de Retrofit.
     *
     * @return instància configurada de Retrofit
     */
    public static Retrofit getInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().create();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor())
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }
}
