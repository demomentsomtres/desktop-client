package agrotech.client;

import agrotech.client.retrofit.ProductsApi;
import agrotech.client.retrofit.RetrofitProvider;
import agrotech.dto.product.ManagedProductDto;
import agrotech.dto.product.ProductCategoriesResponseDto;
import agrotech.dto.product.ProductsListResponseDto;
import agrotech.dto.product.SaveProductRequestDto;
import agrotech.dto.product.SaveProductResponseDto;
import agrotech.model.ManagedProduct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Response;

/**
 * Gestiona les crides HTTP relacionades amb productes.
 *
 * @author Javier Santana Coronado
 */
public class ProductApiClient {

    private final ProductsApi productsApi;

    /**
     * Crea el client d'API de productes.
     */
    public ProductApiClient() {
        productsApi = RetrofitProvider.getInstance().create(ProductsApi.class);
    }

    /**
     * Recupera tots els productes del backend.
     *
     * @return resposta amb la llista de productes
     */
    public ClientResponse<List<ManagedProduct>> getAllProducts() {
        try {
            Response<ProductsListResponseDto> response = productsApi.getAllProducts().execute();

            if (response.isSuccessful() && response.body() != null) {
                ProductsListResponseDto body = response.body();
                List<ManagedProduct> products = mapProducts(body.getProducts());

                return new ClientResponse<>(
                        true,
                        safeMessage(body.getMessage(), "Productes carregats correctament."),
                        products
                );
            }

            return new ClientResponse<>(
                    false,
                    "No s'han pogut carregar els productes.",
                    null
            );
        } catch (IOException ex) {
            return new ClientResponse<>(
                    false,
                    "Error de connexió amb el servidor.",
                    null
            );
        }
    }

    /**
     * Recupera totes les categories del backend.
     *
     * @return resposta amb la llista de categories
     */
    public ClientResponse<List<String>> getAllCategories() {
        try {
            Response<ProductCategoriesResponseDto> response = productsApi.getAllCategories().execute();

            if (response.isSuccessful() && response.body() != null) {
                ProductCategoriesResponseDto body = response.body();

                return new ClientResponse<>(
                        true,
                        safeMessage(body.getMessage(), "Categories carregades correctament."),
                        body.getCategories()
                );
            }

            return new ClientResponse<>(
                    false,
                    "No s'han pogut carregar les categories.",
                    null
            );
        } catch (IOException ex) {
            return new ClientResponse<>(
                    false,
                    "Error de connexió amb el servidor.",
                    null
            );
        }
    }

    /**
     * Crea un nou producte.
     *
     * @param request dades del producte
     * @return resposta de l'operació
     */
    public ClientResponse<Void> createProduct(SaveProductRequestDto request) {
        try {
            Response<SaveProductResponseDto> response = productsApi.createProduct(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                return new ClientResponse<>(
                        true,
                        safeMessage(response.body().getMessage(), "Producte creat correctament."),
                        null
                );
            }

            return new ClientResponse<>(
                    false,
                    "No s'ha pogut crear el producte.",
                    null
            );
        } catch (IOException ex) {
            return new ClientResponse<>(
                    false,
                    "Error de connexió amb el servidor.",
                    null
            );
        }
    }

    /**
     * Actualitza un producte existent.
     *
     * @param id identificador del producte
     * @param request dades actualitzades
     * @return resposta de l'operació
     */
    public ClientResponse<Void> updateProduct(Long id, SaveProductRequestDto request) {
        try {
            Response<SaveProductResponseDto> response = productsApi.updateProduct(id, request).execute();

            if (response.isSuccessful() && response.body() != null) {
                return new ClientResponse<>(
                        true,
                        safeMessage(response.body().getMessage(), "Producte actualitzat correctament."),
                        null
                );
            }

            return new ClientResponse<>(
                    false,
                    "No s'ha pogut actualitzar el producte.",
                    null
            );
        } catch (IOException ex) {
            return new ClientResponse<>(
                    false,
                    "Error de connexió amb el servidor.",
                    null
            );
        }
    }

    /**
     * Elimina un producte existent.
     *
     * @param id identificador del producte
     * @return resposta de l'operació
     */
    public ClientResponse<Void> deleteProduct(Long id) {
        try {
            Response<Void> response = productsApi.deleteProduct(id).execute();

            if (response.isSuccessful()) {
                return new ClientResponse<>(
                        true,
                        "Producte eliminat correctament.",
                        null
                );
            }

            return new ClientResponse<>(
                    false,
                    "No s'ha pogut eliminar el producte.",
                    null
            );
        } catch (IOException ex) {
            return new ClientResponse<>(
                    false,
                    "Error de connexió amb el servidor.",
                    null
            );
        }
    }

    /**
     * Converteix una llista de DTO a model de client.
     *
     * @param dtos llista de DTO de producte
     * @return llista de productes del client
     */
    private List<ManagedProduct> mapProducts(List<ManagedProductDto> dtos) {
        List<ManagedProduct> products = new ArrayList<>();

        if (dtos == null) {
            return products;
        }

        for (ManagedProductDto dto : dtos) {
            products.add(mapProduct(dto));
        }

        return products;
    }

    /**
     * Converteix un DTO de producte a model de client.
     *
     * @param dto DTO de producte
     * @return producte del client
     */
    private ManagedProduct mapProduct(ManagedProductDto dto) {
        return new ManagedProduct(
                dto.getId(),
                dto.getProductCategory(),
                dto.getPrice(),
                dto.getCooperativeMargin(),
                dto.getTax(),
                dto.getExpirationDate(),
                dto.getStock(),
                dto.getSupplierId()
        );
    }

    /**
     * Retorna un missatge segur per mostrar.
     *
     * @param message missatge rebut
     * @param defaultMessage missatge per defecte
     * @return missatge final
     */
    private String safeMessage(String message, String defaultMessage) {
        if (message == null || message.isBlank()) {
            return defaultMessage;
        }

        return message;
    }

}
