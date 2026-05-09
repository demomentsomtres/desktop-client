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
import java.time.LocalDate;

/**
 * Gestiona les crides HTTP relacionades amb productes.
 *
 * @author Javier Santana Coronado
 */
public class ProductApiClient {
    
    private static final String MIN_EXPIRATION_DATE = "1900-01-01";
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
     * Recupera els productes disponibles per a la compra.
     *
     * @return resposta amb la llista de productes disponibles
     */
    public ClientResponse<List<ManagedProduct>> getAvailableProducts() {
        try {
            Response<ProductsListResponseDto> response = productsApi.filterProducts(
                    null,
                    null,
                    null,
                    LocalDate.now().toString(),
                    null,
                    1,
                    null,
                    null,
                    null,
                    null,
                    null
            ).execute();

            if (response.isSuccessful() && response.body() != null) {
                ProductsListResponseDto body = response.body();
                List<ManagedProduct> products = mapProducts(body.getProducts());

                return new ClientResponse<>(
                        true,
                        safeMessage(body.getMessage(), "Productes disponibles carregats correctament."),
                        products
                );
            }

            return new ClientResponse<>(
                    false,
                    "No s'han pogut carregar els productes disponibles.",
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
        try {
            Response<ProductsListResponseDto> response = productsApi.filterProducts(
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
            ).execute();

            if (response.isSuccessful() && response.body() != null) {
                ProductsListResponseDto body = response.body();
                List<ManagedProduct> products = mapProducts(body.getProducts());

                return new ClientResponse<>(
                        true,
                        safeMessage(body.getMessage(), "Productes filtrats correctament."),
                        products
                );
            }

            return new ClientResponse<>(
                    false,
                    "No s'han pogut filtrar els productes.",
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
     * Recupera els productes d'un proveïdor pel seu identificador.
     *
     * @param supplierId identificador del proveïdor
     * @return resposta amb la llista de productes del proveïdor
     */
    public ClientResponse<List<ManagedProduct>> getProductsBySupplierId(Long supplierId) {
        if (supplierId == null) {
            return new ClientResponse<>(
                    false,
                    "No s'ha pogut identificar el proveïdor.",
                    null
            );
        }

        try {
            Response<ProductsListResponseDto> response = productsApi.filterProducts(
                    null,
                    supplierId,
                    null,
                    MIN_EXPIRATION_DATE,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null
            ).execute();

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
                    "No s'han pogut carregar els productes del proveïdor.",
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
     * Recupera els productes associats a l'usuari d'un proveïdor.
     *
     * @param userId identificador de l'usuari autenticat
     * @return resposta amb la llista de productes del proveïdor
     */
    public ClientResponse<List<ManagedProduct>> getProductsBySupplierUserId(Long userId) {
        if (userId == null) {
            return new ClientResponse<>(
                    false,
                    "No s'ha pogut identificar l'usuari actual.",
                    null
            );
        }

        try {
            Response<ProductsListResponseDto> response = productsApi.filterProducts(
                    null,
                    null,
                    userId,
                    MIN_EXPIRATION_DATE,
                    null,
                    0,
                    null,
                    null,
                    null,
                    null,
                    null
            ).execute();

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
                    "No s'han pogut carregar els productes del proveïdor.",
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

            String errorMessage = "No s'ha pogut crear el producte.";
            if (response.errorBody() != null) {
                try {
                    String body = response.errorBody().string();
                    if (body != null && !body.isBlank()) {
                        errorMessage = body;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return new ClientResponse<>(false, errorMessage, null);

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

            String errorMessage = "No s'ha pogut eliminar el producte.";
            if (response.errorBody() != null) {
                try {
                    String body = response.errorBody().string();
                    if (body != null && !body.isBlank()) {
                        errorMessage = body;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return new ClientResponse<>(false, errorMessage, null);

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
            ManagedProduct product = mapProduct(dto);

            if (product != null) {
                products.add(product);
            }
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
        if (dto == null || dto.getId() == null) {
            return null;
        }

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
