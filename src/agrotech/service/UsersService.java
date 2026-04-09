package agrotech.service;

import agrotech.client.ClientResponse;
import agrotech.client.UserApiClient;
import agrotech.dto.user.UserRequestDto;
import agrotech.model.ManagedUser;
import agrotech.model.Role;
import java.util.List;

/**
 * Gestiona la lògica de negoci del mòdul d'usuaris.
 *
 * <p>
 * Valida les dades del formulari i decideix si cal fer una alta, una
 * modificació o una baixa.</p>
 *
 * @author Javier Santana Coronado
 */
public class UsersService {

    private final UserApiClient userApiClient;

    /**
     * Crea el servei de gestió d'usuaris.
     */
    public UsersService() {
        this.userApiClient = new UserApiClient();
    }

    /**
     * Consulta tots els usuaris del sistema.
     *
     * @return resposta amb la llista d'usuaris
     */
    public ClientResponse<List<ManagedUser>> getAllUsers() {
        return userApiClient.getAllUsers();
    }

    /**
     * Consulta un usuari pel seu nom.
     *
     * @param username nom d'usuari
     * @return resposta amb l'usuari consultat
     */
    public ClientResponse<ManagedUser> getUserByUsername(String username) {
        String normalizedUsername = normalize(username);

        if (normalizedUsername.isEmpty()) {
            return new ClientResponse<>(false,
                    "Cal indicar un nom d'usuari.", null);
        }

        return userApiClient.getUserByUsername(normalizedUsername);
    }

    /**
     * Desa un usuari nou o modifica un existent.
     *
     * <p>
     * Si {@code originalUsername} és buit, es tracta d'una alta. Si conté
     * valor, es tracta d'una modificació.</p>
     *
     * @param originalUsername nom original de l'usuari
     * @param username nom d'usuari actual
     * @param password contrasenya
     * @param role rol seleccionat
     * @return resposta de l'operació
     */
    public ClientResponse<Void> saveUser(
            String originalUsername,
            String username,
            String password,
            Role role
    ) {
        String normalizedOriginalUsername = normalize(originalUsername);
        String normalizedUsername = normalize(username);
        String normalizedPassword = normalize(password);

        ClientResponse<Void> validationResponse = validateUserData(
                normalizedOriginalUsername,
                normalizedUsername,
                normalizedPassword,
                role
        );

        if (!validationResponse.isSuccess()) {
            return validationResponse;
        }

        UserRequestDto request = buildRequestDto(
                normalizedUsername,
                normalizedPassword,
                role,
                isCreateMode(normalizedOriginalUsername)
        );

        if (isCreateMode(normalizedOriginalUsername)) {
            return userApiClient.createUser(request);
        }

        return userApiClient.updateUser(normalizedOriginalUsername, request);
    }

    /**
     * Elimina un usuari existent.
     *
     * @param username nom d'usuari
     * @return resposta de l'operació
     */
    public ClientResponse<Void> deleteUser(String username) {
        String normalizedUsername = normalize(username);

        if (normalizedUsername.isEmpty()) {
            return new ClientResponse<>(false,
                    "Cal seleccionar un usuari.", null);
        }

        return userApiClient.deleteUser(normalizedUsername);
    }

    /**
     * Valida les dades del formulari d'usuari.
     *
     * @param originalUsername nom original de l'usuari
     * @param username nom d'usuari actual
     * @param password contrasenya
     * @param role rol seleccionat
     * @return resposta de validació
     */
    private ClientResponse<Void> validateUserData(
            String originalUsername,
            String username,
            String password,
            Role role
    ) {
        if (username.isEmpty()) {
            return new ClientResponse<>(false,
                    "El nom d'usuari és obligatori.", null);
        }

        if (role == null) {
            return new ClientResponse<>(false,
                    "Cal seleccionar un rol.", null);
        }

        if (isCreateMode(originalUsername) && password.isEmpty()) {
            return new ClientResponse<>(false,
                    "La contrasenya és obligatòria en l'alta.", null);
        }

        return new ClientResponse<>(true, "Validació correcta.", null);
    }

    /**
     * Construeix el DTO de la petició d'usuari.
     *
     * @param username nom d'usuari
     * @param password contrasenya
     * @param role rol seleccionat
     * @param createMode indica si és una alta
     * @return DTO amb les dades de la petició
     */
    private UserRequestDto buildRequestDto(
            String username,
            String password,
            Role role,
            boolean createMode
    ) {
        if (createMode) {
            return new UserRequestDto(username, password, role);
        }

        if (password.isEmpty()) {
            return new UserRequestDto(username, null, role);
        }

        return new UserRequestDto(username, password, role);
    }

    /**
     * Indica si l'operació actual és una alta.
     *
     * @param originalUsername nom original de l'usuari
     * @return {@code true} si és una alta; {@code false} altrament
     */
    private boolean isCreateMode(String originalUsername) {
        return originalUsername == null || originalUsername.isBlank();
    }

    /**
     * Normalitza un text del formulari.
     *
     * @param text text original
     * @return text netejat
     */
    private String normalize(String text) {
        if (text == null) {
            return "";
        }

        return text.trim();
    }
}
