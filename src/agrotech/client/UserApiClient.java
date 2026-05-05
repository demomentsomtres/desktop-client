package agrotech.client;

import agrotech.client.retrofit.RetrofitProvider;
import agrotech.client.retrofit.UsersApi;
import agrotech.dto.user.ApiMessageResponseDto;
import agrotech.dto.user.ManagedUserDto;
import agrotech.dto.user.UserRequestDto;
import agrotech.dto.user.UserResponseDto;
import agrotech.dto.user.UsersListResponseDto;
import agrotech.model.ManagedUser;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Gestiona les peticions d'usuaris contra el backend.
 *
 * <p>
 * Inclou les operacions de consulta, alta, modificació i baixa utilitzant
 * Retrofit.</p>
 *
 * @author Javier Santana Coronado
 */
public class UserApiClient {

    private final UsersApi usersApi;
    private final Gson gson;

    /**
     * Crea el client de gestió d'usuaris.
     */
    public UserApiClient() {
        this.usersApi = RetrofitProvider.getInstance().create(UsersApi.class);
        this.gson = new Gson();
    }

    /**
     * Consulta tots els usuaris del sistema.
     *
     * @return resposta amb la llista d'usuaris
     */
    public ClientResponse<List<ManagedUser>> getAllUsers() {
        try {
            Call<UsersListResponseDto> call = usersApi.getAllUsers();
            Response<UsersListResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                UsersListResponseDto body = response.body();

                if (body == null || body.getUsers() == null) {
                    return new ClientResponse<>(false,
                            "Resposta del servidor incompleta.", null);
                }

                List<ManagedUser> users = mapUsers(body.getUsers());

                return new ClientResponse<>(true,
                        "Consulta d'usuaris correcta.", users);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Recupera les dades del propi usuari autenticat.
     *
     * @return resposta amb les dades de l'usuari loguejat
     */
    public ClientResponse<ManagedUser> getMyUser() {
        try {
            Call<UserResponseDto> call = usersApi.getMyUser();
            retrofit2.Response<UserResponseDto> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                ManagedUserDto dto = response.body().getUser();
                ManagedUser user = new ManagedUser(
                        dto.getId(),
                        dto.getUsername(),
                        dto.getRole()
                );
                return new ClientResponse<>(true, "Dades carregades correctament.", user);
            }

            return new ClientResponse<>(false, "No s'han pogut carregar les dades.", null);

        } catch (IOException ex) {
            return new ClientResponse<>(false, "Error de connexió amb el servidor.", null);
        }
    }

    /**
     * Modifica les dades del propi usuari autenticat.
     *
     * @param username nou nom d'usuari (null si no es vol canviar)
     * @param password nova contrasenya (null si no es vol canviar)
     * @return resposta de l'operació
     */
    public ClientResponse<Void> updateMyUser(String username, String password) {
        try {
            UserRequestDto request = new UserRequestDto(username, password, null);
            Call<ApiMessageResponseDto> call = usersApi.updateMyUser(request);
            retrofit2.Response<ApiMessageResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                return new ClientResponse<>(true, "Perfil actualitzat correctament.", null);
            }

            String errorMessage = "No s'ha pogut actualitzar el perfil.";
            if (response.errorBody() != null) {
                try {
                    String body = response.errorBody().string();
                    if (body != null && !body.isBlank()) {
                        errorMessage = body;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return new ClientResponse<>(false, errorMessage, null);

        } catch (IOException ex) {
            return new ClientResponse<>(false, "Error de connexió amb el servidor.", null);
        }
    }

    /**
     * Consulta un usuari pel seu nom.
     *
     * @param username nom d'usuari
     * @return resposta amb l'usuari consultat
     */
    public ClientResponse<ManagedUser> getUserByUsername(String username) {
        try {
            Call<UserResponseDto> call = usersApi.getUserByUsername(username);
            Response<UserResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                UserResponseDto body = response.body();

                if (body == null || body.getUser() == null) {
                    return new ClientResponse<>(false,
                            "Resposta del servidor incompleta.", null);
                }

                ManagedUser user = mapUser(body.getUser());

                return new ClientResponse<>(true,
                        "Consulta d'usuari correcta.", user);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Dona d'alta un usuari nou.
     *
     * @param request dades de l'usuari
     * @return resposta de l'operació
     */
    public ClientResponse<Void> createUser(UserRequestDto request) {
        try {
            Call<ApiMessageResponseDto> call = usersApi.createUser(request);
            Response<ApiMessageResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                String message = extractSuccessMessage(response.body(),
                        "Usuari creat correctament.");

                return new ClientResponse<>(true, message, null);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Modifica un usuari existent.
     *
     * @param originalUsername nom original de l'usuari
     * @param request dades actualitzades
     * @return resposta de l'operació
     */
    public ClientResponse<Void> updateUser(String originalUsername, UserRequestDto request) {
        try {
            Call<ApiMessageResponseDto> call
                    = usersApi.updateUser(originalUsername, request);
            Response<ApiMessageResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                String message = extractSuccessMessage(response.body(),
                        "Usuari modificat correctament.");

                return new ClientResponse<>(true, message, null);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Elimina un usuari existent.
     *
     * @param username nom d'usuari
     * @return resposta de l'operació
     */
    public ClientResponse<Void> deleteUser(String username) {
        try {
            Call<ApiMessageResponseDto> call = usersApi.deleteUser(username);
            Response<ApiMessageResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                String message = extractSuccessMessage(response.body(),
                        "Usuari eliminat correctament.");

                return new ClientResponse<>(true, message, null);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Converteix una llista de DTOs a una llista de models del client.
     *
     * @param userDtos llista de DTOs d'usuaris
     * @return llista de models del client
     */
    private List<ManagedUser> mapUsers(List<ManagedUserDto> userDtos) {
        List<ManagedUser> users = new ArrayList<>();

        for (ManagedUserDto dto : userDtos) {
            ManagedUser user = mapUser(dto);

            if (user != null) {
                users.add(user);
            }
        }

        return users;
    }

    /**
     * Converteix un DTO d'usuari a un model del client.
     *
     * @param dto DTO retornat pel backend
     * @return model del client o {@code null} si no és vàlid
     */
    private ManagedUser mapUser(ManagedUserDto dto) {
        if (dto == null || dto.getId() == null || dto.getUsername() == null
                || dto.getRole() == null) {
            return null;
        }

        return new ManagedUser(dto.getId(), dto.getUsername(), dto.getRole());
    }

    /**
     * Extreu el missatge d'èxit d'una resposta.
     *
     * @param body cos de la resposta
     * @param defaultMessage missatge per defecte
     * @return missatge de la resposta
     */
    private String extractSuccessMessage(ApiMessageResponseDto body, String defaultMessage) {
        if (body != null && body.getMessage() != null && !body.getMessage().isBlank()) {
            return body.getMessage();
        }

        return defaultMessage;
    }

    /**
     * Extreu el missatge d'error d'una resposta HTTP.
     *
     * @param response resposta HTTP
     * @return missatge d'error
     */
    private String extractErrorMessage(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                String errorJson = response.errorBody().string();

                if (errorJson != null && !errorJson.isBlank()) {
                    ApiMessageResponseDto errorResponse
                            = gson.fromJson(errorJson, ApiMessageResponseDto.class);

                    if (errorResponse != null
                            && errorResponse.getMessage() != null
                            && !errorResponse.getMessage().isBlank()) {
                        return errorResponse.getMessage();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error del servidor. Codi HTTP: " + response.code();
    }
}
