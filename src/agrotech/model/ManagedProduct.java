package agrotech.model;

/**
 * Representa un producte gestionat des de l'aplicació.
 *
 * <p>
 * Inclou les dades principals retornades pel backend.</p>
 *
 * @author Javier Santana Coronado
 */
public class ManagedProduct {

    private Long id;
    private String productCategory;
    private Double price;
    private Double cooperativeMargin;
    private Double tax;
    private String expirationDate;
    private Integer stock;
    private Long supplierId;

    /**
     * Crea un producte amb les dades indicades.
     *
     * @param id identificador del producte
     * @param productCategory categoria del producte
     * @param price preu del producte
     * @param cooperativeMargin marge de la cooperativa
     * @param tax impost del producte
     * @param expirationDate data de caducitat
     * @param stock estoc disponible
     * @param supplierId identificador del proveïdor
     */
    public ManagedProduct(Long id, String productCategory, Double price,
            Double cooperativeMargin, Double tax, String expirationDate,
            Integer stock, Long supplierId) {
        this.id = id;
        this.productCategory = productCategory;
        this.price = price;
        this.cooperativeMargin = cooperativeMargin;
        this.tax = tax;
        this.expirationDate = expirationDate;
        this.stock = stock;
        this.supplierId = supplierId;
    }

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
     * Retorna el marge de la cooperativa.
     *
     * @return marge de la cooperativa
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

    @Override
    public String toString() {
        return "ManagedProduct{"
                + "id=" + id
                + ", productCategory='" + productCategory + '\''
                + ", price=" + price
                + ", cooperativeMargin=" + cooperativeMargin
                + ", tax=" + tax
                + ", expirationDate='" + expirationDate + '\''
                + ", stock=" + stock
                + ", supplierId=" + supplierId
                + '}';
    }

}
