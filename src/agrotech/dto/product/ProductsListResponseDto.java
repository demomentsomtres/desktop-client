package agrotech.dto.product;

import java.util.List;

/**
 * Representa la resposta del backend amb la llista de productes.
 *
 * @author Javier Santana Coronado
 */
public class ProductsListResponseDto {

    private int status;
    private String message;
    private List<ManagedProductDto> products;

    /**
     * Retorna el codi d'estat.
     *
     * @return codi d'estat
     */
    public int getStatus() {
        return status;
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
     * Retorna la llista de productes.
     *
     * @return llista de productes
     */
    public List<ManagedProductDto> getProducts() {
        return products;
    }

}
