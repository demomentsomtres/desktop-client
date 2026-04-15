package agrotech.service;

import agrotech.client.ClientResponse;
import agrotech.client.ProductApiClient;
import agrotech.model.ManagedProduct;
import java.util.List;
import agrotech.dto.product.SaveProductRequestDto;

/**
 * Gestiona la lògica bàsica dels productes.
 *
 * <p>
 * Fa de pont entre el panell i el client d'API de productes.
 * </p>
 *
 * @author Javier Santana Coronado
 */
public class ProductsService {

    private final ProductApiClient productApiClient;

    /**
     * Crea el servei de productes.
     */
    public ProductsService() {
        productApiClient = new ProductApiClient();
    }

    /**
     * Retorna tots els productes disponibles.
     *
     * @return resposta amb la llista de productes
     */
    public ClientResponse<List<ManagedProduct>> getAllProducts() {
        return productApiClient.getAllProducts();
    }
    

    /**
     * Desa un producte nou o existent.
     *
     * @param productId identificador del producte, o null si és nou
     * @param productCategory categoria del producte
     * @param price preu del producte
     * @param cooperativeMargin marge de la cooperativa
     * @param stock estoc disponible
     * @param expirationDate data de caducitat
     * @param tax impost aplicat
     * @return resultat de l'operació
     */
    public ClientResponse<Void> saveProduct(
            Long productId,
            String productCategory,
            Double price,
            Double cooperativeMargin,
            Integer stock,
            String expirationDate,
            Double tax
    ) {
        SaveProductRequestDto request = new SaveProductRequestDto(
                productCategory,
                price,
                cooperativeMargin,
                tax,
                expirationDate,
                stock
        );

        if (productId == null) {
            return productApiClient.createProduct(request);
        }

        return productApiClient.updateProduct(productId, request);
    }

    /**
     * Elimina un producte pel seu identificador.
     *
     * @param productId identificador del producte
     * @return resultat de l'operació
     */
    public ClientResponse<Void> deleteProduct(Long productId) {
        return productApiClient.deleteProduct(productId);
    }
}
