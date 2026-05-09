package agrotech.client;

import agrotech.client.retrofit.AuthApi;
import agrotech.client.retrofit.RetrofitProvider;
import agrotech.dto.auth.AuthenticatedUserDto;
import agrotech.dto.auth.LoginRequestDto;
import agrotech.dto.auth.LoginResponseDto;
import agrotech.model.LoginResponse;
import agrotech.model.User;
import agrotech.session.SessionManager;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Gestiona les peticions d'autenticació contra el backend.
 *
 * <p>
 * Inclou les operacions de login i logout utilitzant Retrofit.</p>
 *
 * @author Javier Santana Coronado
 */
public class AuthApiClient {

    private final AuthApi authApi;

    /**
     * Crea el client d'autenticació.
     */
    public AuthApiClient() {
        this.authApi = RetrofitProvider.getInstance().create(AuthApi.class);
    }

    /**
     * Envia una petició de login al backend.
     *
     * @param username nom d'usuari
     * @param password contrasenya
     * @return resposta del login
     */
    public LoginResponse login(String username, String password) {
        try {
            LoginRequestDto requestDto = new LoginRequestDto(username, password);
            Call<LoginResponseDto> call = authApi.login(requestDto);
            Response<LoginResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                LoginResponseDto responseDto = response.body();
                String token = extractToken(response.headers().get("Authorization"));

                if (responseDto == null || responseDto.getUser() == null || token == null) {
                    return new LoginResponse(false,
                            "Resposta del servidor incompleta.", null, null);
                }

                User user = mapUser(responseDto.getUser());

                if (user == null) {
                    return new LoginResponse(false,
                            "Resposta del servidor no vàlida.", null, null);
                }

                return new LoginResponse(true, "Login correcte.", user, token);
            }

            if (response.code() == 401) {
                return new LoginResponse(false, "Contrasenya incorrecta.", null, null);
            }

            if (response.code() == 404) {
                return new LoginResponse(false, "Usuari no trobat.", null, null);
            }

            return new LoginResponse(false,
                    "Error del servidor. Codi HTTP: " + response.code(), null, null);

        } catch (IOException e) {
            e.printStackTrace();
            return new LoginResponse(false,
                    "No s'ha pogut connectar amb el servidor.", null, null);
        }
    }

    /**
     * Envia una petició de logout al backend.
     *
     * @return {@code true} si el logout és correcte; {@code false} altrament
     */
    public boolean logout() {
        String token = SessionManager.getCurrentToken();

        if (token == null || token.isBlank()) {
            return false;
        }

        try {
            Call<Void> call = authApi.logout();
            Response<Void> response = call.execute();
            return response.isSuccessful();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extreu el token del header Authorization.
     *
     * @param authHeader valor del header Authorization
     * @return token sense el prefix Bearer, o {@code null} si no existeix
     */
    private String extractToken(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }

        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return authHeader;
    }

    /**
     * Converteix el DTO autenticat a un usuari del client.
     *
     * @param dto usuari retornat pel backend
     * @return usuari del client o {@code null} si no és vàlid
     */
    private User mapUser(AuthenticatedUserDto dto) {
        if (dto == null || dto.getId() == null || dto.getUsername() == null
                || dto.getRole() == null) {
            return null;
        }

        return new User(dto.getId(), dto.getUsername(), dto.getRole());
    }
}
