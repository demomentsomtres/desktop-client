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
