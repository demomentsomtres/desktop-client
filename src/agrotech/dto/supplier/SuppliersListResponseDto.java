package agrotech.dto.supplier;

import java.util.List;

/**
 * Representa la resposta del backend en la consulta de proveïdors.
 *
 * @author Javier Santana Coronado
 */
public class SuppliersListResponseDto {

    private int status;
    private int total;
    private List<ManagedSupplierDto> suppliers;

    /**
     * Retorna l'estat de la resposta.
     *
     * @return codi d'estat
     */
    public int getStatus() {
        return status;
    }

    /**
     * Retorna el total de proveïdors.
     *
     * @return total de proveïdors
     */
    public int getTotal() {
        return total;
    }

    /**
     * Retorna la llista de proveïdors.
     *
     * @return llista de proveïdors
     */
    public List<ManagedSupplierDto> getSuppliers() {
        return suppliers;
    }
}
