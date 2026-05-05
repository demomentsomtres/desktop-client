package agrotech.client.retrofit;

import agrotech.dto.user.ApiMessageResponseDto;
import agrotech.dto.user.UserRequestDto;
import agrotech.dto.user.UserResponseDto;
import agrotech.dto.user.UsersListResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Defineix les peticions del backend per a la gestió d'usuaris.
 *
 * <p>
 * Inclou les operacions de consulta, alta, modificació i baixa.</p>
 *
 * @author Javier Santana Coronado
 */
public interface UsersApi {

    /**
     * Consulta tots els usuaris.
     *
     * @return resposta amb la llista d'usuaris
     */
    @GET("api/users/all")
    Call<UsersListResponseDto> getAllUsers();

    /**
     * Consulta les dades del propi usuari autenticat.
     *
     * @return resposta amb les dades de l'usuari loguejat
     */
    @GET("api/users/me")
    Call<UserResponseDto> getMyUser();

    /**
     * Modifica les dades del propi usuari autenticat.
     *
     * @param request noves dades de l'usuari
     * @return resposta del backend
     */
    @PUT("api/users/update-me")
    Call<ApiMessageResponseDto> updateMyUser(@Body UserRequestDto request);

    /**
     * Consulta un usuari pel seu nom.
     *
     * @param username nom d'usuari
     * @return resposta amb l'usuari consultat
     */
    @GET("api/users/{username}")
    Call<UserResponseDto> getUserByUsername(@Path("username") String username);

    /**
     * Dona d'alta un usuari nou.
     *
     * @param request dades de l'usuari
     * @return resposta del backend
     */
    @POST("api/users/register")
    Call<ApiMessageResponseDto> createUser(@Body UserRequestDto request);

    /**
     * Modifica un usuari existent.
     *
     * @param username nom original de l'usuari
     * @param request dades actualitzades
     * @return resposta del backend
     */
    @PUT("api/users/update/{username}")
    Call<ApiMessageResponseDto> updateUser(
            @Path("username") String username,
            @Body UserRequestDto request
    );

    /**
     * Elimina un usuari existent.
     *
     * @param username nom d'usuari
     * @return resposta del backend
     */
    @DELETE("api/users/delete/{username}")
    Call<ApiMessageResponseDto> deleteUser(@Path("username") String username);
}
