package agrotech.client;

import agrotech.model.LoginResponse;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Prova d'integració del login amb credencials incorrectes.
 *
 * Es comprova que el backend rebutja correctament un intent d'autenticació
 * invàlid i que el client processa bé la resposta d'error.
 *
 * @author Javier Santana Coronado
 */
public class AuthApiClientInvalidCredentialsIntegrationTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    private static final String INVALID_USERNAME = "usuari_inexistent";
    private static final String INVALID_PASSWORD = "contrasenya_incorrecta";

    /**
     * Comprova que el login amb credencials incorrectes falla correctament.
     */
    @Test
    public void loginIncorrecte_retornaError() {
        LoginResponse response = authApiClient.login(INVALID_USERNAME, INVALID_PASSWORD);

        assertNotNull("La resposta del login no hauria de ser null.", response);
        assertFalse("El login amb credencials incorrectes hauria de fallar.", response.isSuccess());
        assertNull("No s'hauria de retornar cap usuari.", response.getUser());
        assertNull("No s'hauria de retornar cap token.", response.getToken());
        assertNotNull("El missatge d'error no hauria de ser null.", response.getMessage());
        assertFalse("El missatge d'error no hauria d'estar buit.", response.getMessage().trim().isEmpty());
    }
}
