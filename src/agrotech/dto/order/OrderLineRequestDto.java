package agrotech.dto.order;

/**
 * Representa una línia de comanda enviada al backend per crear una comanda.
 *
 * @author Javier Santana Coronado
 */
public class OrderLineRequestDto {

    private Long productId;
    private Integer quantity;

    /**
     * Crea una línia amb les dades indicades.
     *
     * @param productId identificador del producte
     * @param quantity quantitat demanada
     */
    public OrderLineRequestDto(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    /**
     * Retorna l'identificador del producte.
     *
     * @return identificador del producte
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Retorna la quantitat demanada.
     *
     * @return quantitat demanada
     */
    public Integer getQuantity() {
        return quantity;
    }
}
