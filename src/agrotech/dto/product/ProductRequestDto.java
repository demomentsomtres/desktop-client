package agrotech.dto.product;

/**
 * Representa les dades enviades al backend per crear o modificar un producte.
 *
 * @author Javier Santana Coronado
 */
public class ProductRequestDto {

    private String productCategory;
    private Double price;
    private Double cooperativeMargin;
    private Double tax;
    private String expirationDate;
    private Integer stock;

    /**
     * Constructor buit necessari per Gson.
     */
    public ProductRequestDto() {
    }

    /**
     * Crea un DTO amb les dades del producte.
     *
     * @param productCategory categoria del producte
     * @param price preu del producte
     * @param cooperativeMargin marge de la cooperativa
     * @param tax impost aplicat
     * @param expirationDate data de caducitat
     * @param stock estoc disponible
     */
    public ProductRequestDto(String productCategory, Double price,
            Double cooperativeMargin, Double tax, String expirationDate,
            Integer stock) {
        this.productCategory = productCategory;
        this.price = price;
        this.cooperativeMargin = cooperativeMargin;
        this.tax = tax;
        this.expirationDate = expirationDate;
        this.stock = stock;
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

}
