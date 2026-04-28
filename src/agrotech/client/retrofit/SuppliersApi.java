package agrotech.client.retrofit;

import agrotech.dto.supplier.SaveSupplierRequestDto;
import agrotech.dto.supplier.SupplierResponseDto;
import agrotech.dto.supplier.SuppliersListResponseDto;
import agrotech.dto.user.ApiMessageResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Defineix les peticions del backend per a la gestió de proveïdors.
 *
 * @author Javier Santana Coronado
 */
public interface SuppliersApi {

    /**
     * Consulta tots els proveïdors.
     *
     * @return resposta amb la llista de proveïdors
     */
    @GET("api/suppliers/all")
    Call<SuppliersListResponseDto> getAllSuppliers();

    /**
     * Consulta un proveïdor pel seu nom.
     *
     * @param name nom del proveïdor
     * @return resposta amb el proveïdor consultat
     */
    @GET("api/suppliers/{name}")
    Call<SupplierResponseDto> getSupplierByName(@Path("name") String name);

    /**
     * Consulta proveïdors filtrant per població i telèfon.
     *
     * @param poblacio població a filtrar
     * @param telefon telèfon a filtrar
     * @return resposta amb la llista de proveïdors
     */
    @GET("api/suppliers/filter")
    Call<SuppliersListResponseDto> getSuppliersByFilters(
            @Query("poblacio") String poblacio,
            @Query("telefon") String telefon
    );

    /**
     * Consulta les dades del proveïdor autenticat.
     *
     * @return resposta amb les dades del proveïdor loguejat
     */
    @GET("api/suppliers/me")
    Call<SupplierResponseDto> getMySupplier();

    /**
     * Dona d'alta un proveïdor nou.
     *
     * @param request dades del proveïdor
     * @return resposta del backend
     */
    @POST("api/suppliers/create")
    Call<ApiMessageResponseDto> createSupplier(@Body SaveSupplierRequestDto request);

    /**
     * Modifica un proveïdor existent.
     *
     * @param username nom original del proveïdor
     * @param request dades actualitzades
     * @return resposta del backend
     */
    @PUT("api/suppliers/update/{username}")
    Call<ApiMessageResponseDto> updateSupplier(
            @Path("username") String username,
            @Body SaveSupplierRequestDto request
    );

    /**
     * Elimina un proveïdor existent.
     *
     * @param username nom del proveïdor
     * @return resposta del backend
     */
    @DELETE("api/suppliers/delete/{username}")
    Call<ApiMessageResponseDto> deleteSupplier(@Path("username") String username);
}
