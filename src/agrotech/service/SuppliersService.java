package agrotech.service;

import agrotech.client.ClientResponse;
import agrotech.client.SupplierApiClient;
import agrotech.dto.supplier.SaveSupplierRequestDto;
import agrotech.model.ManagedSupplier;
import java.util.List;

/**
 * Gestiona la lògica de negoci del mòdul de proveïdors.
 *
 * @author Javier Santana Coronado
 */
public class SuppliersService {

    private final SupplierApiClient supplierApiClient;

    /**
     * Crea el servei de gestió de proveïdors.
     */
    public SuppliersService() {
        this.supplierApiClient = new SupplierApiClient();
    }

    /**
     * Consulta tots els proveïdors del sistema.
     *
     * @return resposta amb la llista de proveïdors
     */
    public ClientResponse<List<ManagedSupplier>> getAllSuppliers() {
        return supplierApiClient.getAllSuppliers();
    }

    /**
     * Recupera les dades del proveïdor autenticat.
     *
     * @return resposta amb les dades del proveïdor loguejat
     */
    public ClientResponse<ManagedSupplier> getMySupplier() {
        return supplierApiClient.getMySupplier();
    }

    /**
     * Consulta un proveïdor pel seu nom.
     *
     * @param name nom del proveïdor
     * @return resposta amb el proveïdor consultat
     */
    public ClientResponse<ManagedSupplier> getSupplierByName(String name) {
        String normalizedName = normalize(name);

        if (normalizedName.isEmpty()) {
            return new ClientResponse<>(false,
                    "Cal indicar un nom de proveïdor.", null);
        }

        return supplierApiClient.getSupplierByName(normalizedName);
    }

    /**
     * Consulta proveïdors filtrant per població i telèfon.
     *
     * @param poblacio població a filtrar
     * @param telefon telèfon a filtrar
     * @return resposta amb la llista de proveïdors
     */
    public ClientResponse<List<ManagedSupplier>> getSuppliersByFilters(
            String poblacio,
            String telefon
    ) {
        String normalizedPoblacio = normalize(poblacio);
        String normalizedTelefon = normalize(telefon);

        if (normalizedPoblacio.isEmpty() && normalizedTelefon.isEmpty()) {
            return supplierApiClient.getAllSuppliers();
        }

        return supplierApiClient.getSuppliersByFilters(
                normalizedPoblacio,
                normalizedTelefon
        );
    }

    /**
     * Desa un proveïdor nou o modifica un existent.
     *
     * @param originalName nom original del proveïdor
     * @param name nom actual
     * @param password contrasenya
     * @param poblacio població
     * @param email correu electrònic
     * @param telefon telèfon
     * @return resposta de l'operació
     */
    public ClientResponse<Void> saveSupplier(
            String originalName,
            String name,
            String password,
            String poblacio,
            String email,
            String telefon
    ) {
        String normalizedOriginalName = normalize(originalName);
        String normalizedName = normalize(name);
        String normalizedPassword = normalize(password);
        String normalizedPoblacio = normalize(poblacio);
        String normalizedEmail = normalize(email);
        String normalizedTelefon = normalize(telefon);

        ClientResponse<Void> validationResponse = validateSupplierData(
                normalizedOriginalName,
                normalizedName,
                normalizedPassword,
                normalizedPoblacio,
                normalizedEmail,
                normalizedTelefon
        );

        if (!validationResponse.isSuccess()) {
            return validationResponse;
        }

        SaveSupplierRequestDto request = buildRequestDto(
                normalizedName,
                normalizedPassword,
                normalizedPoblacio,
                normalizedEmail,
                normalizedTelefon,
                isCreateMode(normalizedOriginalName)
        );

        if (isCreateMode(normalizedOriginalName)) {
            return supplierApiClient.createSupplier(request);
        }

        return supplierApiClient.updateSupplier(normalizedOriginalName, request);
    }

    /**
     * Elimina un proveïdor existent.
     *
     * @param name nom del proveïdor
     * @return resposta de l'operació
     */
    public ClientResponse<Void> deleteSupplier(String name) {
        String normalizedName = normalize(name);

        if (normalizedName.isEmpty()) {
            return new ClientResponse<>(false,
                    "Cal seleccionar un proveïdor.", null);
        }

        return supplierApiClient.deleteSupplier(normalizedName);
    }

    /**
     * Valida les dades del formulari de proveïdor.
     *
     * @param originalName nom original del proveïdor
     * @param name nom actual
     * @param password contrasenya
     * @param poblacio població
     * @param email correu electrònic
     * @param telefon telèfon
     * @return resposta de validació
     */
    private ClientResponse<Void> validateSupplierData(
            String originalName,
            String name,
            String password,
            String poblacio,
            String email,
            String telefon
    ) {
        if (name.isEmpty()) {
            return new ClientResponse<>(false,
                    "El nom del proveïdor és obligatori.", null);
        }

        if (isCreateMode(originalName) && password.isEmpty()) {
            return new ClientResponse<>(false,
                    "La contrasenya és obligatòria en l'alta.", null);
        }

        if (poblacio.isEmpty()) {
            return new ClientResponse<>(false,
                    "La població és obligatòria.", null);
        }

        if (email.isEmpty()) {
            return new ClientResponse<>(false,
                    "El correu electrònic és obligatori.", null);
        }

        if (telefon.isEmpty()) {
            return new ClientResponse<>(false,
                    "El telèfon és obligatori.", null);
        }

        return new ClientResponse<>(true, "Validació correcta.", null);
    }

    /**
     * Construeix el DTO de la petició de proveïdor.
     *
     * @param name nom del proveïdor
     * @param password contrasenya
     * @param poblacio població
     * @param email correu electrònic
     * @param telefon telèfon
     * @param createMode indica si és una alta
     * @return DTO amb les dades de la petició
     */
    private SaveSupplierRequestDto buildRequestDto(
            String name,
            String password,
            String poblacio,
            String email,
            String telefon,
            boolean createMode
    ) {
        if (createMode) {
            return new SaveSupplierRequestDto(
                    name, password, poblacio, email, telefon
            );
        }

        if (password.isEmpty()) {
            return new SaveSupplierRequestDto(
                    name, null, poblacio, email, telefon
            );
        }

        return new SaveSupplierRequestDto(
                name, password, poblacio, email, telefon
        );
    }

    /**
     * Indica si l'operació actual és una alta.
     *
     * @param originalName nom original del proveïdor
     * @return {@code true} si és una alta
     */
    private boolean isCreateMode(String originalName) {
        return originalName == null || originalName.isBlank();
    }

    /**
     * Normalitza un text del formulari.
     *
     * @param text text original
     * @return text netejat
     */
    private String normalize(String text) {
        if (text == null) {
            return "";
        }

        return text.trim();
    }
}
