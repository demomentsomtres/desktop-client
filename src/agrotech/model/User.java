package agrotech.model;

/**
 * Representa un usuari autenticat del sistema.
 *
 * <p>
 * Inclou l'identificador, el nom d'usuari i el rol associat.</p>
 *
 * @author Javier Santana Coronado
 */
public class User {

    private Long id;
    private String username;
    private Role role;

    /**
     * Crea un usuari amb les dades indicades.
     *
     * @param id identificador de l'usuari
     * @param username nom d'usuari
     * @param role rol assignat a l'usuari
     */
    public User(Long id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

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

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role=" + role + "}";
    }

}
