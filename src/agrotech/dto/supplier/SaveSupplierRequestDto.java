package agrotech.dto.supplier;

/**
 * Representa el cos de la petició per crear o modificar un proveïdor.
 *
 * @author Javier Santana Coronado
 */
public class SaveSupplierRequestDto {

    private String name;
    private String password;
    private String poblacio;
    private String email;
    private String telefon;

    /**
     * Crea una petició amb les dades del proveïdor.
     *
     * @param name nom del proveïdor
     * @param password contrasenya
     * @param poblacio població
     * @param email correu electrònic
     * @param telefon telèfon
     */
    public SaveSupplierRequestDto(String name, String password,
            String poblacio, String email, String telefon) {
        this.name = name;
        this.password = password;
        this.poblacio = poblacio;
        this.email = email;
        this.telefon = telefon;
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
     * Retorna la contrasenya.
     *
     * @return contrasenya
     */
    public String getPassword() {
        return password;
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
}
