package agrotech.client;

import agrotech.client.retrofit.RetrofitProvider;
import agrotech.model.LoginResponse;
import agrotech.model.Role;
import agrotech.session.SessionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;

import static org.junit.Assert.*;

/**
 * Proves d'integració de la connexió segura amb el backend.
 *
 * <p>
 * Es comprova que el client utilitza el protocol HTTPS i que pot iniciar sessió
 * correctament contra el servidor real fent servir la configuració de Retrofit.
 * </p>
 *
 * @author Javier Santana Coronado
 */
public class HttpsConnectionIntegrationTest {

    private AuthApiClient authApiClient;

    private static final String ADMIN_USERNAME = "javier";
    private static final String ADMIN_PASSWORD = "1234";

    /**
     * Prepara la prova eliminant qualsevol sessió anterior i inicialitzant el
     * client d'autenticació.
     */
    @Before
    public void setUp() {
        SessionManager.logout();
        authApiClient = new AuthApiClient();
    }

    /**
     * Tanca la sessió després de cada prova per no deixar dades compartides
     * entre execucions.
     */
    @After
    public void tearDown() {
        SessionManager.logout();
    }

    /**
     * Comprova que Retrofit està configurat amb una URL HTTPS.
     */
    @Test
    public void retrofitProvider_utilitzaProtocolHttps() {
        Retrofit retrofit = RetrofitProvider.getInstance();

        assertNotNull(
                "La instància de Retrofit no hauria de ser null.",
                retrofit
        );

        assertEquals(
                "El protocol de comunicació hauria de ser HTTPS.",
                "https",
                retrofit.baseUrl().scheme()
        );

        assertTrue(
                "La URL base hauria de començar per https://",
                retrofit.baseUrl().toString().startsWith("https://")
        );
    }

    /**
     * Comprova que el client pot iniciar sessió correctament contra el backend
     * real utilitzant la connexió HTTPS configurada.
     */
    @Test
    public void loginAdmin_perHttps_retornaTokenUsuariIRol() {
        LoginResponse response = authApiClient.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        assertNotNull(
                "La resposta del login no hauria de ser null.",
                response
        );

        assertTrue(
                "El login per HTTPS hauria de ser correcte. Comprova que el backend està en marxa i que el truststore està configurat.",
                response.isSuccess()
        );

        assertNotNull(
                "L'usuari autenticat no hauria de ser null.",
                response.getUser()
        );

        assertNotNull(
                "El token retornat pel backend no hauria de ser null.",
                response.getToken()
        );

        assertFalse(
                "El token retornat pel backend no hauria d'estar buit.",
                response.getToken().trim().isEmpty()
        );

        assertEquals(
                "L'usuari autenticat hauria de tenir rol ADMIN.",
                Role.ADMIN,
                response.getUser().getRole()
        );
    }
}
