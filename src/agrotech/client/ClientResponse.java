package agrotech.client;

/**
 * Representa una resposta interna del client.
 *
 * <p>
 * Inclou l'estat de l'operació, un missatge i les dades retornades.</p>
 *
 * @param <T> tipus de dades retornades
 *
 * @author Javier Santana Coronado
 */
public class ClientResponse<T> {

    private boolean success;
    private String message;
    private T data;

    /**
     * Crea una resposta interna del client.
     *
     * @param success indica si l'operació ha anat bé
     * @param message missatge associat a la resposta
     * @param data dades retornades
     */
    public ClientResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * Indica si l'operació ha estat correcta.
     *
     * @return {@code true} si ha anat bé; {@code false} altrament
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Retorna el missatge de la resposta.
     *
     * @return missatge de la resposta
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retorna les dades de la resposta.
     *
     * @return dades retornades
     */
    public T getData() {
        return data;
    }
}
