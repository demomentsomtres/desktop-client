package agrotech.dto.supplier;

/**
 * Representa un proveïdor retornat pel backend.
 *
 * @author Javier Santana Coronado
 */
public class ManagedSupplierDto {

    private Long id;
    private String name;
    private String poblacio;
    private String email;
    private String telefon;

    /**
     * Retorna l'identificador del proveïdor.
     *
     * @return identificador del proveïdor
     */
    public Long getId() {
        return id;
    }

    /**
     * Retorna el nom del proveïdor.
     *
     * @return nom del proveïdor
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna la població del proveïdor.
     *
     * @return població del proveïdor
     */
    public String getPoblacio() {
        return poblacio;
    }

    /**
     * Retorna el correu electrònic del proveïdor.
     *
     * @return correu electrònic
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retorna el telèfon del proveïdor.
     *
     * @return telèfon del proveïdor
     */
    public String getTelefon() {
        return telefon;
    }
}
