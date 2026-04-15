package agrotech.dto.product;

import java.util.List;

/**
 * Representa la resposta del backend amb les categories de producte.
 *
 * @author Javier Santana Coronado
 */
public class ProductCategoriesResponseDto {

    private int status;
    private String message;
    private List<String> categories;

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
     * Retorna la llista de categories.
     *
     * @return llista de categories
     */
    public List<String> getCategories() {
        return categories;
    }

}
