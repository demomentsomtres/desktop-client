package agrotech.service;

import agrotech.client.ClientResponse;
import agrotech.client.ProductApiClient;
import agrotech.model.ManagedProduct;
import java.util.List;
import agrotech.dto.product.SaveProductRequestDto;
import java.time.LocalDate;
import agrotech.model.Role;
import agrotech.model.User;
import agrotech.session.SessionManager;

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
     * Recupera els productes disponibles per a la compra.
     *
     * @return resposta amb la llista de productes disponibles
     */
    public ClientResponse<List<ManagedProduct>> getAvailableProducts() {
        return productApiClient.getAvailableProducts();
    }

    /**
     * Retorna els productes disponibles amb caducitat propera.
     *
     * @return resposta amb la llista de productes amb oferta automàtica
     */
    public ClientResponse<List<ManagedProduct>> getProductsWithExpirationOffer() {
        String expirationDateBefore = LocalDate.now().plusDays(3).toString();

        return productApiClient.filterProducts(
                null,
                null,
                null,
                null,
                expirationDateBefore,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Filtra els productes segons els paràmetres indicats.
     *
     * @param categoria categoria del producte
     * @param supplierId identificador del proveïdor
     * @param userId identificador de l'usuari proveïdor
     * @param expirationDateAfter data mínima de caducitat
     * @param expirationDateBefore data màxima de caducitat
     * @param minStock estoc mínim
     * @param minPrice preu mínim
     * @param maxPrice preu màxim
     * @param minCooperativeMargin marge mínim
     * @param maxCooperativeMargin marge màxim
     * @param tax impost
     * @return resposta amb la llista de productes filtrats
     */
    public ClientResponse<List<ManagedProduct>> filterProducts(
            String categoria,
            Long supplierId,
            Long userId,
            String expirationDateAfter,
            String expirationDateBefore,
            Integer minStock,
            Double minPrice,
            Double maxPrice,
            Double minCooperativeMargin,
            Double maxCooperativeMargin,
            Double tax
    ) {
        return productApiClient.filterProducts(
                categoria,
                supplierId,
                userId,
                expirationDateAfter,
                expirationDateBefore,
                minStock,
                minPrice,
                maxPrice,
                minCooperativeMargin,
                maxCooperativeMargin,
                tax
        );
    }

    /**
     * Retorna els productes del proveïdor autenticat.
     *
     * @param userId identificador de l'usuari autenticat
     * @return resposta amb la llista de productes del proveïdor
     */
    public ClientResponse<List<ManagedProduct>> getProductsBySupplierUserId(Long userId) {
        return productApiClient.getProductsBySupplierUserId(userId);
    }

    /**
     * Recupera els productes d'un proveïdor pel seu identificador.
     *
     * @param supplierId identificador del proveïdor
     * @return resposta amb la llista de productes del proveïdor
     */
    public ClientResponse<List<ManagedProduct>> getProductsBySupplierId(Long supplierId) {
        return productApiClient.getProductsBySupplierId(supplierId);
    }

    /**
     * Consulta els productes del proveïdor autenticat per mostrar-los al
     * dashboard.
     *
     * <p>
     * Aquest mètode utilitza l'usuari de la sessió actual per recuperar només
     * els productes associats al proveïdor autenticat.
     * </p>
     *
     * @return resposta amb la llista de productes del proveïdor actual
     */
    public ClientResponse<List<ManagedProduct>> getMyProductsForDashboard() {
        User currentUser = SessionManager.getCurrentUser();

        if (currentUser == null || currentUser.getRole() != Role.SUPPLIER) {
            return new ClientResponse<>(
                    false,
                    "Només els proveïdors poden consultar aquest dashboard.",
                    null
            );
        }

        return getProductsBySupplierUserId(currentUser.getId());
    }

    /**
     * Retorna totes les categories de producte disponibles.
     *
     * @return resposta amb la llista de categories
     */
    public ClientResponse<List<String>> getAllCategories() {
        return productApiClient.getAllCategories();
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
