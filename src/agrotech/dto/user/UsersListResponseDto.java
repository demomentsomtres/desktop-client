package agrotech.dto.user;

import java.util.List;

/**
 * Representa la resposta del backend en la consulta d'usuaris.
 *
 * <p>
 * Inclou l'estat de la petició, el total d'usuaris i la llista retornada.</p>
 *
 * @author Javier Santana Coronado
 */
public class UsersListResponseDto {

    private int status;
    private int total;
    private List<ManagedUserDto> users;

    /**
     * Retorna l'estat de la resposta.
     *
     * @return codi d'estat
     */
    public int getStatus() {
        return status;
    }

    /**
     * Retorna el total d'usuaris.
     *
     * @return total d'usuaris
     */
    public int getTotal() {
        return total;
    }

    /**
     * Retorna la llista d'usuaris.
     *
     * @return llista d'usuaris
     */
    public List<ManagedUserDto> getUsers() {
        return users;
    }
}
