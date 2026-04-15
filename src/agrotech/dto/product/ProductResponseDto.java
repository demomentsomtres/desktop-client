package agrotech.dto.product;

/**
 * Representa les dades d'un producte rebudes del backend.
 *
 * @author Javier Santana Coronado
 */
public class ProductResponseDto {

    private Long id;
    private String productCategory;
    private Double price;
    private Double cooperativeMargin;
    private Double tax;
    private String expirationDate;
    private Integer stock;
    private Long supplierId;

    /**
     * Constructor buit necessari per Gson.
     */
    public ProductResponseDto() {
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
     * Defineix l'identificador del producte.
     *
     * @param id identificador del producte
     */
    public void setId(Long id) {
        this.id = id;
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
     * Defineix la categoria del producte.
     *
     * @param productCategory categoria del producte
     */
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
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
     * Defineix el preu del producte.
     *
     * @param price preu del producte
     */
    public void setPrice(Double price) {
        this.price = price;
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
     * Defineix el marge de la cooperativa.
     *
     * @param cooperativeMargin marge de la cooperativa
     */
    public void setCooperativeMargin(Double cooperativeMargin) {
        this.cooperativeMargin = cooperativeMargin;
    }

    /**
     * Retorna l'impost aplicat.
     *
     * @return impost aplicat
     */
    public Double getTax() {
        return tax;
    }

    /**
     * Defineix l'impost aplicat.
     *
     * @param tax impost aplicat
     */
    public void setTax(Double tax) {
        this.tax = tax;
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
     * Defineix la data de caducitat.
     *
     * @param expirationDate data de caducitat
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
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
     * Defineix l'estoc disponible.
     *
     * @param stock estoc disponible
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * Retorna l'identificador del proveïdor.
     *
     * @return identificador del proveïdor
     */
    public Long getSupplierId() {
        return supplierId;
    }

    /**
     * Defineix l'identificador del proveïdor.
     *
     * @param supplierId identificador del proveïdor
     */
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

}
