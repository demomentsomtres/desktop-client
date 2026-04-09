package agrotech.client.retrofit;

import agrotech.session.SessionManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Afegeix automàticament el token de sessió a les peticions protegides.
 *
 * <p>
 * Si hi ha un token actiu al {@code SessionManager}, s'envia a la capçalera
 * {@code Authorization} amb el prefix {@code Bearer }.</p>
 *
 * @author Javier Santana Coronado
 */
public class AuthInterceptor implements Interceptor {

    /**
     * Intercepta la petició i hi afegeix el token si existeix.
     *
     * @param chain cadena d'interceptors
     * @return resposta de la petició
     * @throws IOException si hi ha un error de comunicació
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = SessionManager.getCurrentToken();

        if (token == null || token.isBlank()
                || originalRequest.header("Authorization") != null) {
            return chain.proceed(originalRequest);
        }

        Request newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(newRequest);
    }
}
