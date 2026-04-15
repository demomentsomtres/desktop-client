package agrotech.dto.product;

/**
 * Representa el cos de la petició per desar un producte.
 *
 * @author Javier Santana Coronado
 */
public class SaveProductRequestDto {

    private String productCategory;
    private Double price;
    private Double cooperativeMargin;
    private Double tax;
    private String expirationDate;
    private Integer stock;

    /**
     * Crea una petició amb les dades del producte.
     *
     * @param productCategory categoria del producte
     * @param price preu del producte
     * @param cooperativeMargin marge de la cooperativa
     * @param tax impost del producte
     * @param expirationDate data de caducitat
     * @param stock estoc disponible
     */
    public SaveProductRequestDto(String productCategory, Double price,
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

}
