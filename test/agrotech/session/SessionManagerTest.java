package agrotech.session;

import agrotech.model.Role;
import agrotech.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves unitàries de la classe SessionManager.
 *
 * Es comprova que la sessió es desa i s'elimina correctament.
 *
 * @author Javier Santana Coronado
 */
public class SessionManagerTest {

    @Before
    public void setUp() {
        SessionManager.logout();
    }

    @After
    public void tearDown() {
        SessionManager.logout();
    }

    /**
     * Comprova que login desa correctament usuari i token.
     */
    @Test
    public void login_desaUsuariIToken() {
        User user = new User(1, "javier", Role.ADMIN);

        SessionManager.login(user, "token123");

        assertEquals(user, SessionManager.getCurrentUser());
        assertEquals("token123", SessionManager.getCurrentToken());
    }

    /**
     * Comprova que logout esborra la sessió actual.
     */
    @Test
    public void logout_esborraSessio() {
        User user = new User(1, "javier", Role.CLIENT);
        SessionManager.login(user, "token123");

        SessionManager.logout();

        assertNull(SessionManager.getCurrentUser());
        assertNull(SessionManager.getCurrentToken());
    }
}