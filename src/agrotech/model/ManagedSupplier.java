package agrotech.model;

/**
 * Representa un proveïdor gestionable des de l'aplicació.
 *
 * @author Javier Santana Coronado
 */
public class ManagedSupplier {

    private Long id;
    private String name;
    private String poblacio;
    private String email;
    private String telefon;

    /**
     * Crea un proveïdor amb les dades indicades.
     *
     * @param id identificador del proveïdor
     * @param name nom del proveïdor
     * @param poblacio població
     * @param email correu electrònic
     * @param telefon telèfon
     */
    public ManagedSupplier(Long id, String name, String poblacio,
            String email, String telefon) {
        this.id = id;
        this.name = name;
        this.poblacio = poblacio;
        this.email = email;
        this.telefon = telefon;
    }

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
     * Retorna la població.
     *
     * @return població
     */
    public String getPoblacio() {
        return poblacio;
    }

    /**
     * Retorna el correu electrònic.
     *
     * @return correu electrònic
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retorna el telèfon.
     *
     * @return telèfon
     */
    public String getTelefon() {
        return telefon;
    }

    @Override
    public String toString() {
        return "ManagedSupplier{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", poblacio='" + poblacio + '\''
                + ", email='" + email + '\''
                + ", telefon='" + telefon + '\''
                + '}';
    }
}
