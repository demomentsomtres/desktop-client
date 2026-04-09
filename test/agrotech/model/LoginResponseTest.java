package agrotech.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Proves unitàries de la classe LoginResponse.
 *
 * Es comprova que el constructor desa correctament les dades i que els getters
 * les retornen bé.
 *
 * @author Javier Santana Coronado
 */
public class LoginResponseTest {

    /**
     * Comprova que els getters retornen correctament les dades passades al
     * constructor.
     */
    @Test
    public void constructor_iGetters_funcionenCorrectament() {
        User user = new User(1L, "javier", Role.ADMIN);
        LoginResponse response = new LoginResponse(true, "OK", user, "token123");

        assertTrue(response.isSuccess());
        assertEquals("OK", response.getMessage());
        assertEquals(user, response.getUser());
        assertEquals("token123", response.getToken());
    }
}
