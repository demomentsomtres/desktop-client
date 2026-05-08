package agrotech.dto.product;

/**
 * Representa un producte rebut del backend.
 *
 * <p>
 * Aquesta classe s'utilitza per deserialitzar la resposta JSON.</p>
 *
 * @author Javier Santana Coronado
 */
public class ManagedProductDto {

    private Long id;
    private String productCategory;
    private Double price;
    private Double cooperativeMargin;
    private Double tax;
    private String expirationDate;
    private Integer stock;
    private Long supplierId;

    /**
     * Retorna l'identificador del producte.
     *
     * @return identificador del producte
     */
    public Long getId() {
        return id;
    }

    /**
     * Retorna la categoria del producte.
     *
     * @return categoria del producte
     */
    public String getProductCategory() {
        return productCategory;
    }

    /**
     * Retorna el preu del producte.
     *
     * @return preu del producte
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Retorna el percentatge del marge cooperatiu.
     *
     * @return percentatge del marge cooperatiu
     */
    public Double getCooperativeMargin() {
        return cooperativeMargin;
    }

    /**
     * Retorna l'impost del producte.
     *
     * @return impost del producte
     */
    public Double getTax() {
        return tax;
    }

    /**
     * Retorna la data de caducitat.
     *
     * @return data de caducitat
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * Retorna l'estoc disponible.
     *
     * @return estoc disponible
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * Retorna l'identificador del proveïdor.
     *
     * @return identificador del proveïdor
     */
    public Long getSupplierId() {
        return supplierId;
    }

}
