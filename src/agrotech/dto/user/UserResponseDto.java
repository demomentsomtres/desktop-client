package agrotech.dto.user;

/**
 * Representa la resposta del backend en la consulta d'un usuari.
 *
 * <p>
 * Inclou l'estat de la petició i l'usuari retornat.</p>
 *
 * @author Javier Santana Coronado
 */
public class UserResponseDto {

    private int status;
    private ManagedUserDto user;

    /**
     * Retorna l'estat de la resposta.
     *
     * @return codi d'estat
     */
    public int getStatus() {
        return status;
    }

    /**
     * Retorna l'usuari consultat.
     *
     * @return usuari retornat pel backend
     */
    public ManagedUserDto getUser() {
        return user;
    }
}
