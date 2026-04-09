package agrotech.client.retrofit;

import agrotech.dto.auth.LoginRequestDto;
import agrotech.dto.auth.LoginResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Defineix les peticions d'autenticació del backend.
 *
 * <p>
 * Inclou les operacions de login i logout.</p>
 *
 * @author Javier Santana Coronado
 */
public interface AuthApi {

    /**
     * Envia la petició de login al backend.
     *
     * @param request dades de login
     * @return resposta del backend
     */
    @POST("api/auth/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto request);

    /**
     * Envia la petició de logout al backend.
     *
     * @return resposta del backend
     */
    @POST("api/auth/logout")
    Call<Void> logout();
}
