package agrotech.client;

import agrotech.dto.user.UserRequestDto;
import agrotech.model.LoginResponse;
import agrotech.model.ManagedUser;
import agrotech.model.Role;
import agrotech.session.SessionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves d'integració sobre la consulta i modificació del perfil propi.
 *
 * <p>
 * La prova crea un usuari temporal, inicia sessió amb aquest usuari i comprova
 * que pot consultar i modificar el seu propi perfil mitjançant les peticions
 * /users/me i /users/update-me.
 * </p>
 *
 * <p>
 * Es fa servir un usuari temporal per no modificar usuaris fixos del projecte.
 * </p>
 *
 * @author Javier Santana Coronado
 */
public class ProfileUpdateMeSafeIntegrationTest {

    private AuthApiClient authApiClient;
    private UserApiClient userApiClient;

    private String tempUsername;
    private String updatedUsername;

    private static final String ADMIN_USERNAME = "javier";
    private static final String ADMIN_PASSWORD = "1234";

    private static final String TEMP_PASSWORD = "test1234";

    /**
     * Prepara els clients d'API i genera noms d'usuari temporals per a la
     * prova.
     */
    @Before
    public void setUp() {
        SessionManager.logout();

        authApiClient = new AuthApiClient();
        userApiClient = new UserApiClient();

        long suffix = System.currentTimeMillis() % 1000000;

        tempUsername = "t4u" + suffix;
        updatedUsername = "t4up" + suffix;
    }

    /**
     * Intenta eliminar els usuaris temporals creats durant la prova.
     */
    @After
    public void tearDown() {
        deleteTemporaryUsers();
        SessionManager.logout();
    }

    /**
     * Comprova que un usuari temporal pot consultar el seu perfil i modificar
     * el seu propi nom d'usuari de manera segura.
     */
    @Test
    public void updateMyUser_modificaUsernamePropiAmbUsuariTemporal() {
        loginAs(ADMIN_USERNAME, ADMIN_PASSWORD, Role.ADMIN);
        createTemporaryCustomerUser();

        loginAs(tempUsername, TEMP_PASSWORD, Role.CUSTOMER);

        ClientResponse<ManagedUser> initialProfileResponse = userApiClient.getMyUser();

        assertNotNull(
                "La resposta inicial de getMyUser no hauria de ser null.",
                initialProfileResponse
        );

        assertTrue(
                "La consulta inicial del perfil propi hauria de ser correcta.",
                initialProfileResponse.isSuccess()
        );

        ManagedUser initialUser = initialProfileResponse.getData();

        assertNotNull(
                "L'usuari inicial no hauria de ser null.",
                initialUser
        );

        assertNotNull(
                "L'usuari inicial hauria de tenir id.",
                initialUser.getId()
        );

        assertEquals(
                "El username inicial hauria de coincidir amb l'usuari temporal creat.",
                tempUsername,
                initialUser.getUsername()
        );

        assertEquals(
                "L'usuari temporal hauria de tenir rol CUSTOMER.",
                Role.CUSTOMER,
                initialUser.getRole()
        );

        ClientResponse<Void> updateResponse = userApiClient.updateMyUser(
                updatedUsername,
                null
        );

        assertNotNull(
                "La resposta d'updateMyUser no hauria de ser null.",
                updateResponse
        );

        assertTrue(
                "L'actualització del perfil propi hauria de ser correcta.",
                updateResponse.isSuccess()
        );

        ClientResponse<ManagedUser> updatedProfileResponse = userApiClient.getMyUser();

        assertNotNull(
                "La resposta actualitzada de getMyUser no hauria de ser null.",
                updatedProfileResponse
        );

        assertTrue(
                "La consulta del perfil actualitzat hauria de ser correcta.",
                updatedProfileResponse.isSuccess()
        );

        ManagedUser updatedUser = updatedProfileResponse.getData();

        assertNotNull(
                "L'usuari actualitzat no hauria de ser null.",
                updatedUser
        );

        assertEquals(
                "L'id de l'usuari hauria de ser el mateix després de modificar el username.",
                initialUser.getId(),
                updatedUser.getId()
        );

        assertEquals(
                "El username del perfil propi hauria d'haver-se actualitzat.",
                updatedUsername,
                updatedUser.getUsername()
        );

        assertEquals(
                "El rol no hauria de canviar en actualitzar el perfil propi.",
                Role.CUSTOMER,
                updatedUser.getRole()
        );

        loginAs(updatedUsername, TEMP_PASSWORD, Role.CUSTOMER);
    }

    /**
     * Crea un usuari temporal amb rol CUSTOMER.
     */
    private void createTemporaryCustomerUser() {
        UserRequestDto request = new UserRequestDto(
                tempUsername,
                TEMP_PASSWORD,
                Role.CUSTOMER
        );

        ClientResponse<Void> response = userApiClient.createUser(request);

        assertNotNull(
                "La resposta de creació de l'usuari temporal no hauria de ser null.",
                response
        );

        assertTrue(
                "L'usuari temporal s'hauria de crear correctament.",
                response.isSuccess()
        );
    }

    /**
     * Inicia sessió amb l'usuari indicat i desa el token al SessionManager.
     *
     * @param username nom d'usuari
     * @param password contrasenya
     * @param expectedRole rol esperat
     */
    private void loginAs(String username, String password, Role expectedRole) {
        SessionManager.logout();

        LoginResponse loginResponse = authApiClient.login(username, password);

        assertNotNull(
                "La resposta del login no hauria de ser null.",
                loginResponse
        );

        assertTrue(
                "El login hauria de ser correcte per a l'usuari " + username + ".",
                loginResponse.isSuccess()
        );

        assertNotNull(
                "L'usuari autenticat no hauria de ser null.",
                loginResponse.getUser()
        );

        assertNotNull(
                "El token no hauria de ser null.",
                loginResponse.getToken()
        );

        assertEquals(
                "El rol de l'usuari autenticat no és l'esperat.",
                expectedRole,
                loginResponse.getUser().getRole()
        );

        SessionManager.login(loginResponse.getUser(), loginResponse.getToken());
    }

    /**
     * Intenta eliminar els usuaris temporals sense fer fallar la prova si algun
     * ja no existeix.
     */
    private void deleteTemporaryUsers() {
        try {
            LoginResponse loginResponse = authApiClient.login(
                    ADMIN_USERNAME,
                    ADMIN_PASSWORD
            );

            if (loginResponse == null || !loginResponse.isSuccess()
                    || loginResponse.getUser() == null
                    || loginResponse.getToken() == null) {
                return;
            }

            SessionManager.login(loginResponse.getUser(), loginResponse.getToken());

            if (updatedUsername != null) {
                userApiClient.deleteUser(updatedUsername);
            }

            if (tempUsername != null) {
                userApiClient.deleteUser(tempUsername);
            }

        } catch (Exception ex) {
            System.err.println("No s'han pogut eliminar els usuaris temporals de la prova.");
        }
    }
}
