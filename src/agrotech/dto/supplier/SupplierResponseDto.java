package agrotech.dto.supplier;

/**
 * Representa la resposta del backend en la consulta d'un proveïdor.
 *
 * @author Javier Santana Coronado
 */
public class SupplierResponseDto {

    private int status;
    private ManagedSupplierDto supplier;

    /**
     * Retorna l'estat de la resposta.
     *
     * @return codi d'estat
     */
    public int getStatus() {
        return status;
    }

    /**
     * Retorna el proveïdor consultat.
     *
     * @return proveïdor retornat
     */
    public ManagedSupplierDto getSupplier() {
        return supplier;
    }
}
