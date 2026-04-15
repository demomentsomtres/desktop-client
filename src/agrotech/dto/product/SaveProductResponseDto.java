package agrotech.dto.product;

import com.google.gson.annotations.SerializedName;

/**
 * Representa la resposta del backend després de desar un producte.
 *
 * @author Javier Santana Coronado
 */
public class SaveProductResponseDto {

    private int status;
    private String message;

    @SerializedName("producteId")
    private Long productId;

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
     * Retorna l'identificador del producte.
     *
     * @return identificador del producte
     */
    public Long getProductId() {
        return productId;
    }

}
