package agrotech.dto.auth;

/**
 * Representa el cos de la petició de login.
 *
 * <p>
 * Inclou el nom d'usuari i la contrasenya enviats al backend.</p>
 *
 * @author Javier Santana Coronado
 */
public class LoginRequestDto {

    private String username;
    private String password;

    /**
     * Crea una petició de login amb les dades indicades.
     *
     * @param username nom d'usuari
     * @param password contrasenya
     */
    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Retorna el nom d'usuari.
     *
     * @return nom d'usuari
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retorna la contrasenya.
     *
     * @return contrasenya
     */
    public String getPassword() {
        return password;
    }
}
