package agrotech.dto.user;

import agrotech.model.Role;
import com.google.gson.annotations.SerializedName;

/**
 * Representa el cos de les peticions d'alta i modificació d'usuaris.
 *
 * <p>
 * Inclou el nom d'usuari, la contrasenya i el rol que s'envien al backend.</p>
 *
 * @author Javier Santana Coronado
 */
public class UserRequestDto {

    private String username;
    private String password;

    @SerializedName("rol")
    private Role role;

    /**
     * Crea una petició amb les dades indicades.
     *
     * @param username nom d'usuari
     * @param password contrasenya
     * @param role rol assignat a l'usuari
     */
    public UserRequestDto(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
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

    /**
     * Retorna el rol de l'usuari.
     *
     * @return rol de l'usuari
     */
    public Role getRole() {
        return role;
    }
}
