package agrotech.client.retrofit;

import agrotech.dto.product.ProductCategoriesResponseDto;
import agrotech.dto.product.ProductsListResponseDto;
import agrotech.dto.product.SaveProductRequestDto;
import agrotech.dto.product.SaveProductResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Defineix els endpoints REST de productes.
 *
 * @author Javier Santana Coronado
 */
public interface ProductsApi {

    /**
     * Recupera tots els productes.
     *
     * @return crida amb la llista de productes
     */
    @GET("api/products/all")
    Call<ProductsListResponseDto> getAllProducts();

    /**
     * Filtra productes segons els paràmetres indicats.
     *
     * @param categoria categoria del producte
     * @param supplierId identificador del proveïdor
     * @param userId identificador de l'usuari associat al proveïdor
     * @param expirationDateAfter data mínima de caducitat
     * @param expirationDateBefore data màxima de caducitat
     * @param minStock estoc mínim
     * @param minPrice preu mínim
     * @param maxPrice preu màxim
     * @param minCooperativeMargin marge mínim
     * @param maxCooperativeMargin marge màxim
     * @param tax impost
     * @return crida amb la llista de productes filtrats
     */
    @GET("api/products/filter")
    Call<ProductsListResponseDto> filterProducts(
            @Query("categoria") String categoria,
            @Query("supplierId") Long supplierId,
            @Query("userId") Long userId,
            @Query("expirationDateAfter") String expirationDateAfter,
            @Query("expirationDateBefore") String expirationDateBefore,
            @Query("minStock") Integer minStock,
            @Query("minPrice") Double minPrice,
            @Query("maxPrice") Double maxPrice,
            @Query("minCooperativeMargin") Double minCooperativeMargin,
            @Query("maxCooperativeMargin") Double maxCooperativeMargin,
            @Query("tax") Double tax
    );

    /**
     * Recupera totes les categories de producte.
     *
     * @return crida amb la llista de categories
     */
    @GET("api/products/categories")
    Call<ProductCategoriesResponseDto> getAllCategories();

    /**
     * Crea un nou producte.
     *
     * @param request dades del producte
     * @return crida amb la resposta del backend
     */
    @POST("api/products/create")
    Call<SaveProductResponseDto> createProduct(@Body SaveProductRequestDto request);

    /**
     * Actualitza un producte existent.
     *
     * @param id identificador del producte
     * @param request dades actualitzades
     * @return crida amb la resposta del backend
     */
    @PUT("api/products/update/{id}")
    Call<SaveProductResponseDto> updateProduct(
            @Path("id") Long id,
            @Body SaveProductRequestDto request
    );

    /**
     * Elimina un producte existent.
     *
     * @param id identificador del producte
     * @return crida de l'operació
     */
    @DELETE("api/products/delete/{id}")
    Call<Void> deleteProduct(@Path("id") Long id);

}
