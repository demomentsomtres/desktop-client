package agrotech.client;

import agrotech.client.retrofit.RetrofitProvider;
import agrotech.client.retrofit.SuppliersApi;
import agrotech.dto.supplier.ManagedSupplierDto;
import agrotech.dto.supplier.SaveSupplierRequestDto;
import agrotech.dto.supplier.SupplierResponseDto;
import agrotech.dto.supplier.SuppliersListResponseDto;
import agrotech.dto.user.ApiMessageResponseDto;
import agrotech.model.ManagedSupplier;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Gestiona les peticions de proveïdors contra el backend.
 *
 * @author Javier Santana Coronado
 */
public class SupplierApiClient {

    private final SuppliersApi suppliersApi;
    private final Gson gson;

    /**
     * Crea el client de gestió de proveïdors.
     */
    public SupplierApiClient() {
        this.suppliersApi = RetrofitProvider.getInstance().create(SuppliersApi.class);
        this.gson = new Gson();
    }

    /**
     * Recupera les dades del proveïdor autenticat.
     *
     * @return resposta amb les dades del proveïdor loguejat
     */
    public ClientResponse<ManagedSupplier> getMySupplier() {
        try {
            Response<SupplierResponseDto> response = suppliersApi.getMySupplier().execute();

            if (response.isSuccessful() && response.body() != null) {
                SupplierResponseDto body = response.body();
                ManagedSupplierDto dto = body.getSupplier();

                ManagedSupplier supplier = new ManagedSupplier(
                        dto.getId(),
                        dto.getName(),
                        dto.getPoblacio(),
                        dto.getEmail(),
                        dto.getTelefon()
                );

                return new ClientResponse<>(
                        true,
                        "Proveïdor carregat correctament.",
                        supplier
                );
            }

            return new ClientResponse<>(
                    false,
                    "No s'han pogut carregar les dades del proveïdor.",
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
     * Consulta tots els proveïdors del sistema.
     *
     * @return resposta amb la llista de proveïdors
     */
    public ClientResponse<List<ManagedSupplier>> getAllSuppliers() {
        try {
            Call<SuppliersListResponseDto> call = suppliersApi.getAllSuppliers();
            Response<SuppliersListResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                SuppliersListResponseDto body = response.body();

                if (body == null || body.getSuppliers() == null) {
                    return new ClientResponse<>(false,
                            "Resposta del servidor incompleta.", null);
                }

                List<ManagedSupplier> suppliers = mapSuppliers(body.getSuppliers());

                return new ClientResponse<>(true,
                        "Consulta de proveïdors correcta.", suppliers);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Consulta un proveïdor pel seu nom.
     *
     * @param name nom del proveïdor
     * @return resposta amb el proveïdor consultat
     */
    public ClientResponse<ManagedSupplier> getSupplierByName(String name) {
        try {
            Call<SupplierResponseDto> call = suppliersApi.getSupplierByName(name);
            Response<SupplierResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                SupplierResponseDto body = response.body();

                if (body == null || body.getSupplier() == null) {
                    return new ClientResponse<>(false,
                            "Resposta del servidor incompleta.", null);
                }

                ManagedSupplier supplier = mapSupplier(body.getSupplier());

                return new ClientResponse<>(true,
                        "Consulta de proveïdor correcta.", supplier);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Consulta proveïdors aplicant filtres.
     *
     * @param poblacio població a filtrar
     * @param telefon telèfon a filtrar
     * @return resposta amb la llista de proveïdors
     */
    public ClientResponse<List<ManagedSupplier>> getSuppliersByFilters(
            String poblacio,
            String telefon
    ) {
        try {
            Call<SuppliersListResponseDto> call
                    = suppliersApi.getSuppliersByFilters(poblacio, telefon);
            Response<SuppliersListResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                SuppliersListResponseDto body = response.body();

                if (body == null || body.getSuppliers() == null) {
                    return new ClientResponse<>(false,
                            "Resposta del servidor incompleta.", null);
                }

                List<ManagedSupplier> suppliers = mapSuppliers(body.getSuppliers());

                return new ClientResponse<>(true,
                        "Consulta de proveïdors correcta.", suppliers);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Dona d'alta un proveïdor nou.
     *
     * @param request dades del proveïdor
     * @return resposta de l'operació
     */
    public ClientResponse<Void> createSupplier(SaveSupplierRequestDto request) {
        try {
            Call<ApiMessageResponseDto> call = suppliersApi.createSupplier(request);
            Response<ApiMessageResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                String message = extractSuccessMessage(response.body(),
                        "Proveïdor creat correctament.");

                return new ClientResponse<>(true, message, null);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Modifica un proveïdor existent.
     *
     * @param originalName nom original del proveïdor
     * @param request dades actualitzades
     * @return resposta de l'operació
     */
    public ClientResponse<Void> updateSupplier(
            String originalName,
            SaveSupplierRequestDto request
    ) {
        try {
            Call<ApiMessageResponseDto> call
                    = suppliersApi.updateSupplier(originalName, request);
            Response<ApiMessageResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                String message = extractSuccessMessage(response.body(),
                        "Proveïdor modificat correctament.");

                return new ClientResponse<>(true, message, null);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Elimina un proveïdor existent.
     *
     * @param name nom del proveïdor
     * @return resposta de l'operació
     */
    public ClientResponse<Void> deleteSupplier(String name) {
        try {
            Call<ApiMessageResponseDto> call = suppliersApi.deleteSupplier(name);
            Response<ApiMessageResponseDto> response = call.execute();

            if (response.isSuccessful()) {
                String message = extractSuccessMessage(response.body(),
                        "Proveïdor eliminat correctament.");

                return new ClientResponse<>(true, message, null);
            }

            return new ClientResponse<>(false,
                    extractErrorMessage(response), null);

        } catch (IOException e) {
            e.printStackTrace();
            return new ClientResponse<>(false,
                    "No s'ha pogut connectar amb el servidor.", null);
        }
    }

    /**
     * Converteix una llista de DTOs a una llista de models del client.
     *
     * @param supplierDtos llista de DTOs de proveïdors
     * @return llista de models del client
     */
    private List<ManagedSupplier> mapSuppliers(List<ManagedSupplierDto> supplierDtos) {
        List<ManagedSupplier> suppliers = new ArrayList<>();

        for (ManagedSupplierDto dto : supplierDtos) {
            ManagedSupplier supplier = mapSupplier(dto);

            if (supplier != null) {
                suppliers.add(supplier);
            }
        }

        return suppliers;
    }

    /**
     * Converteix un DTO de proveïdor a un model del client.
     *
     * @param dto DTO retornat pel backend
     * @return model del client o {@code null} si no és vàlid
     */
    private ManagedSupplier mapSupplier(ManagedSupplierDto dto) {
        if (dto == null || dto.getId() == null || dto.getName() == null
                || dto.getName().isBlank()) {
            return null;
        }

        return new ManagedSupplier(
                dto.getId(),
                dto.getName(),
                dto.getPoblacio(),
                dto.getEmail(),
                dto.getTelefon()
        );
    }

    /**
     * Extreu el missatge d'èxit d'una resposta.
     *
     * @param body cos de la resposta
     * @param defaultMessage missatge per defecte
     * @return missatge de la resposta
     */
    private String extractSuccessMessage(ApiMessageResponseDto body, String defaultMessage) {
        if (body != null && body.getMessage() != null && !body.getMessage().isBlank()) {
            return body.getMessage();
        }

        return defaultMessage;
    }

    /**
     * Extreu el missatge d'error d'una resposta HTTP.
     *
     * @param response resposta HTTP
     * @return missatge d'error
     */
    private String extractErrorMessage(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                String errorJson = response.errorBody().string();

                if (errorJson != null && !errorJson.isBlank()) {
                    ApiMessageResponseDto errorResponse
                            = gson.fromJson(errorJson, ApiMessageResponseDto.class);

                    if (errorResponse != null
                            && errorResponse.getMessage() != null
                            && !errorResponse.getMessage().isBlank()) {
                        return errorResponse.getMessage();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Error del servidor. Codi HTTP: " + response.code();
    }
}
