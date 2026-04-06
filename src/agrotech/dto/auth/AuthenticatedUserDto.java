package agrotech.dto.auth;

import agrotech.model.Role;
import com.google.gson.annotations.SerializedName;

/**
 * Representa l'usuari retornat pel backend després del login.
 *
 * <p>
 * Inclou l'identificador, el nom d'usuari i el rol associat.</p>
 *
 * @author Javier Santana Coronado
 */
public class AuthenticatedUserDto {

    private Long id;
    private String username;

    @SerializedName("rol")
    private Role role;

    /**
     * Retorna l'identificador de l'usuari.
     *
     * @return identificador de l'usuari
     */
    public Long getId() {
        return id;
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
     * Retorna el rol de l'usuari.
     *
     * @return rol de l'usuari
     */
    public Role getRole() {
        return role;
    }
}
