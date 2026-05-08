package agrotech.view.panels;

import agrotech.client.ClientResponse;
import agrotech.model.ManagedProduct;
import agrotech.service.ProductsService;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import agrotech.model.Role;
import agrotech.session.SessionManager;
import javax.swing.DefaultComboBoxModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import agrotech.model.ManagedSupplier;
import agrotech.service.SuppliersService;

/**
 * Panell de gestió de productes de l'aplicació.
 *
 * <p>
 * Permet visualitzar i gestionar les dades dels productes disponibles.</p>
 *
 * @author Javier Santana Coronado
 */
public class ProductsPanel extends javax.swing.JPanel {

    private final ProductsService productsService;
    private final SuppliersService suppliersService;
    private DefaultTableModel tableModel;
    private Long selectedProductId;

    /**
     * Crea el panell de gestió de productes.
     */
    public ProductsPanel() {
        initComponents();
        configureTitleByRole();
        productsService = new ProductsService();
        suppliersService = new SuppliersService();
        initTableModel();
        configureSupplierColumnVisibility();
        initTableSelection();
        loadCategories();
        clearForm();
        loadProducts();
    }

    /**
     * Configura el títol del panell segons el rol de l'usuari.
     */
    private void configureTitleByRole() {
        Role currentRole = SessionManager.getCurrentUser().getRole();

        if (currentRole == Role.SUPPLIER) {
            lblTitle.setText("Gestió dels meus productes");
        } else if (currentRole == Role.CUSTOMER) {
            lblTitle.setText("Productes disponibles");
        } else {
            lblTitle.setText("Gestió de productes");
        }
    }

    /**
     * Inicialitza el model de la taula de productes.
     */
    private void initTableModel() {
        tableModel = (DefaultTableModel) tblProducts.getModel();
    }

    /**
     * Inicialitza la selecció de la taula de productes.
     */
    private void initTableSelection() {
        tblProducts.getSelectionModel().addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting()) {
                loadSelectedProduct();
            }
        });
    }

    /**
     * Carrega el producte seleccionat al formulari.
     */
    private void loadSelectedProduct() {
        int selectedRow = tblProducts.getSelectedRow();

        if (selectedRow == -1) {
            selectedProductId = null;
            configurePermissionsByRole();
            return;
        }

        int modelRow = tblProducts.convertRowIndexToModel(selectedRow);

        Object idValue = tableModel.getValueAt(modelRow, 0);
        Object categoryValue = tableModel.getValueAt(modelRow, 1);
        Object priceValue = tableModel.getValueAt(modelRow, 3);
        Object stockValue = tableModel.getValueAt(modelRow, 4);
        Object marginValue = tableModel.getValueAt(modelRow, 5);
        Object expirationValue = tableModel.getValueAt(modelRow, 6);
        Object taxValue = tableModel.getValueAt(modelRow, 7);

        selectedProductId = Long.valueOf(idValue.toString());
        cmbCategory.setSelectedItem(categoryValue.toString());
        txtPrice.setText(priceValue.toString());
        txtStock.setText(stockValue.toString());
        txtCooperativeMargin.setText(marginValue.toString());
        txtExpirationDate.setText(expirationValue.toString());
        txtTax.setText(taxValue.toString());

        configurePermissionsByRole();
    }

    /**
     * Neteja el formulari i reinicia la selecció.
     */
    private void clearForm() {
        selectedProductId = null;
        cmbCategory.setSelectedIndex(-1);
        txtPrice.setText("");
        txtCooperativeMargin.setText("");
        txtStock.setText("");
        txtExpirationDate.setText("");
        txtTax.setText("");
        tblProducts.clearSelection();
        configurePermissionsByRole();
        if (txtPrice.isEnabled()) {
            txtPrice.requestFocus();
        }
    }

    /**
     * Carrega la llista de productes a la taula.
     */
    private void loadProducts() {
        ClientResponse<List<ManagedProduct>> response;
        Map<Long, String> supplierNames = new HashMap<>();

        if (SessionManager.getCurrentUser() != null
                && SessionManager.getCurrentUser().getRole() == Role.SUPPLIER) {

            ClientResponse<ManagedSupplier> supplierResponse = suppliersService.getMySupplier();

            if (!supplierResponse.isSuccess()) {
                JOptionPane.showMessageDialog(
                        this,
                        supplierResponse.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            ManagedSupplier currentSupplier = supplierResponse.getData();
            Long supplierId = currentSupplier.getId();

            supplierNames.put(supplierId, currentSupplier.getName());
            response = productsService.getProductsBySupplierId(supplierId);

        } else {
            Role currentRole = SessionManager.getCurrentUser().getRole();

            if (currentRole == Role.ADMIN) {
                supplierNames = getSupplierNamesById();
                response = productsService.getAllProducts();
            } else if (currentRole == Role.CUSTOMER) {
                response = productsService.getAvailableProducts();
            } else {
                response = productsService.getAllProducts();
            }
        }

        if (!response.isSuccess()) {
            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        fillProductsTable(response.getData(), supplierNames);
    }

    /**
     * Carrega les categories disponibles des del backend als desplegables.
     */
    private void loadCategories() {
        ClientResponse<List<String>> response = productsService.getAllCategories();

        if (!response.isSuccess()) {
            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        DefaultComboBoxModel<String> formModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> filterModel = new DefaultComboBoxModel<>();

        filterModel.addElement("Totes");

        for (String category : response.getData()) {
            formModel.addElement(category);
            filterModel.addElement(category);
        }

        cmbCategory.setModel(formModel);
        cmbCategory.setSelectedIndex(-1);

        cmbFilterCategory.setModel(filterModel);
        cmbFilterCategory.setSelectedIndex(0);
    }

    /**
     * Activa o desactiva els camps del formulari.
     *
     * @param editable true si els camps s'han de poder editar; false en cas
     * contrari
     */
    private void setFormEditable(boolean editable) {
        cmbCategory.setEnabled(editable);

        txtPrice.setEditable(editable);
        txtPrice.setEnabled(editable);

        txtCooperativeMargin.setEditable(editable);
        txtCooperativeMargin.setEnabled(editable);

        txtStock.setEditable(editable);
        txtStock.setEnabled(editable);

        txtExpirationDate.setEditable(editable);
        txtExpirationDate.setEnabled(editable);

        txtTax.setEditable(editable);
        txtTax.setEnabled(editable);
    }

    /**
     * Configura els permisos visuals segons el rol de l'usuari autenticat.
     */
    private void configurePermissionsByRole() {
        if (SessionManager.getCurrentUser() == null) {
            btnNew.setVisible(false);
            btnSave.setVisible(false);
            btnDelete.setVisible(false);
            setFormEditable(false);
            return;
        }

        Role currentRole = SessionManager.getCurrentUser().getRole();

        switch (currentRole) {
            case ADMIN -> {
                btnNew.setVisible(false);

                btnSave.setVisible(true);
                btnDelete.setVisible(true);

                btnSave.setText("Desa");
                btnSave.setEnabled(selectedProductId != null);
                btnDelete.setEnabled(selectedProductId != null);

                setFormEditable(true);
            }

            case SUPPLIER -> {
                btnNew.setVisible(true);

                btnSave.setVisible(true);
                btnDelete.setVisible(true);

                btnSave.setEnabled(true);
                btnDelete.setEnabled(selectedProductId != null);

                if (selectedProductId == null) {
                    btnSave.setText("Crea");
                } else {
                    btnSave.setText("Desa");
                }

                setFormEditable(true);
            }

            case CUSTOMER -> {
                btnNew.setVisible(false);
                btnSave.setVisible(false);
                btnDelete.setVisible(false);

                setFormEditable(false);
            }

            default -> {
                btnNew.setVisible(false);
                btnSave.setVisible(false);
                btnDelete.setVisible(false);

                setFormEditable(false);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        pnlFilter = new javax.swing.JPanel();
        lblFilterProduct = new javax.swing.JLabel();
        lblFilterCategory = new javax.swing.JLabel();
        cmbFilterCategory = new javax.swing.JComboBox<>();
        sepProduct = new javax.swing.JSeparator();
        lblFilterPriceTitle = new javax.swing.JLabel();
        lblFilterMinPrice = new javax.swing.JLabel();
        txtFilterMinPrice = new javax.swing.JTextField();
        lblFilterMaxPrice = new javax.swing.JLabel();
        txtFilterMaxPrice = new javax.swing.JTextField();
        sepPrice = new javax.swing.JSeparator();
        lblFilterMarginTitle = new javax.swing.JLabel();
        lblFilterMinMargin = new javax.swing.JLabel();
        txtFilterMinMargin = new javax.swing.JTextField();
        lblFilterMaxMargin = new javax.swing.JLabel();
        txtFilterMaxMargin = new javax.swing.JTextField();
        sepMargin = new javax.swing.JSeparator();
        lblFilterStockTitle = new javax.swing.JLabel();
        lblFilterMinStock = new javax.swing.JLabel();
        txtFilterMinStock = new javax.swing.JTextField();
        sepStock = new javax.swing.JSeparator();
        lblFilterExpirationTitle = new javax.swing.JLabel();
        lblFilterExpirationAfter = new javax.swing.JLabel();
        txtFilterExpirationAfter = new javax.swing.JTextField();
        lblFilterExpirationBefore = new javax.swing.JLabel();
        txtFilterExpirationBefore = new javax.swing.JTextField();
        sepExpiration = new javax.swing.JSeparator();
        lblFilterTaxTitle = new javax.swing.JLabel();
        lblFilterTax = new javax.swing.JLabel();
        txtFilterTax = new javax.swing.JTextField();
        btnApplyFilters = new javax.swing.JButton();
        btnClearFilters = new javax.swing.JButton();
        spProducts = new javax.swing.JScrollPane();
        tblProducts = new javax.swing.JTable();
        pnlForm = new javax.swing.JPanel();
        lblCategory = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox<>();
        lblPrice = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        lblCooperativeMargin = new javax.swing.JLabel();
        txtCooperativeMargin = new javax.swing.JTextField();
        lblStock = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        lblExpirationDate = new javax.swing.JLabel();
        txtExpirationDate = new javax.swing.JTextField();
        lblTax = new javax.swing.JLabel();
        txtTax = new javax.swing.JTextField();
        pnlButtons = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 247, 248));

        lblTitle.setBackground(new java.awt.Color(255, 255, 255));
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(30, 111, 168));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Gestió de productes");

        pnlFilter.setBackground(new java.awt.Color(255, 255, 255));
        pnlFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)), "Filtre"));

        lblFilterProduct.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFilterProduct.setForeground(new java.awt.Color(30, 111, 168));
        lblFilterProduct.setText("Producte");

        lblFilterCategory.setText("Categoria");

        cmbFilterCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        sepProduct.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblFilterPriceTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFilterPriceTitle.setForeground(new java.awt.Color(30, 111, 168));
        lblFilterPriceTitle.setText("Preu €");

        lblFilterMinPrice.setText("Mínim");

        lblFilterMaxPrice.setText("Màxim");

        sepPrice.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblFilterMarginTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFilterMarginTitle.setForeground(new java.awt.Color(30, 111, 168));
        lblFilterMarginTitle.setText("Marge cooperativa (%)");

        lblFilterMinMargin.setText("Mínim");

        lblFilterMaxMargin.setText("Màxim");

        sepMargin.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblFilterStockTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFilterStockTitle.setForeground(new java.awt.Color(30, 111, 168));
        lblFilterStockTitle.setText("Estoc");

        lblFilterMinStock.setText("Mínim");

        sepStock.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblFilterExpirationTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFilterExpirationTitle.setForeground(new java.awt.Color(30, 111, 168));
        lblFilterExpirationTitle.setText("Caducitat");

        lblFilterExpirationAfter.setText("Des de (YYYY-MM-DD)");

        txtFilterExpirationAfter.setColumns(15);
        txtFilterExpirationAfter.setForeground(new java.awt.Color(33, 37, 41));

        lblFilterExpirationBefore.setText("Fins a (YYYY-MM-DD)");

        txtFilterExpirationBefore.setColumns(15);
        txtFilterExpirationBefore.setForeground(new java.awt.Color(33, 37, 41));

        sepExpiration.setOrientation(javax.swing.SwingConstants.VERTICAL);

        lblFilterTaxTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFilterTaxTitle.setForeground(new java.awt.Color(30, 111, 168));
        lblFilterTaxTitle.setText("Impost (%)");

        lblFilterTax.setText("Valor");

        btnApplyFilters.setForeground(new java.awt.Color(30, 111, 168));
        btnApplyFilters.setText("Filtra");
        btnApplyFilters.setFocusPainted(false);
        btnApplyFilters.setPreferredSize(new java.awt.Dimension(90, 30));
        btnApplyFilters.addActionListener(this::btnApplyFiltersActionPerformed);

        btnClearFilters.setForeground(new java.awt.Color(30, 111, 168));
        btnClearFilters.setText("Neteja");
        btnClearFilters.setFocusPainted(false);
        btnClearFilters.setPreferredSize(new java.awt.Dimension(90, 30));
        btnClearFilters.addActionListener(this::btnClearFiltersActionPerformed);

        javax.swing.GroupLayout pnlFilterLayout = new javax.swing.GroupLayout(pnlFilter);
        pnlFilter.setLayout(pnlFilterLayout);
        pnlFilterLayout.setHorizontalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFilterLayout.createSequentialGroup()
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblFilterCategory)
                            .addComponent(cmbFilterCategory, 0, 128, Short.MAX_VALUE)
                            .addComponent(lblFilterProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sepProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFilterLayout.createSequentialGroup()
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFilterMinPrice)
                                    .addComponent(txtFilterMinPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFilterMaxPrice)
                                    .addComponent(txtFilterMaxPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFilterLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblFilterPriceTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sepPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFilterMarginTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlFilterLayout.createSequentialGroup()
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFilterMinMargin)
                                    .addComponent(txtFilterMinMargin, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFilterMaxMargin)
                                    .addComponent(txtFilterMaxMargin, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sepMargin, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFilterMinStock)
                            .addComponent(lblFilterStockTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFilterMinStock, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sepStock, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlFilterLayout.createSequentialGroup()
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblFilterExpirationAfter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtFilterExpirationAfter, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblFilterExpirationBefore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtFilterExpirationBefore, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lblFilterExpirationTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sepExpiration, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblFilterTax)
                            .addComponent(txtFilterTax)
                            .addComponent(lblFilterTaxTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlFilterLayout.createSequentialGroup()
                        .addComponent(btnApplyFilters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClearFilters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlFilterLayout.setVerticalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFilterLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sepMargin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFilterLayout.createSequentialGroup()
                                .addComponent(lblFilterStockTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblFilterMinStock)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFilterMinStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sepExpiration, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(pnlFilterLayout.createSequentialGroup()
                                        .addComponent(lblFilterExpirationTitle)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblFilterExpirationAfter)
                                            .addComponent(lblFilterExpirationBefore))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtFilterExpirationAfter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtFilterExpirationBefore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(sepStock, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnlFilterLayout.createSequentialGroup()
                                    .addComponent(lblFilterTaxTitle)
                                    .addGap(13, 13, 13)
                                    .addComponent(lblFilterTax)
                                    .addGap(4, 4, 4)
                                    .addComponent(txtFilterTax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(20, 20, 20))
                    .addGroup(pnlFilterLayout.createSequentialGroup()
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFilterLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(lblFilterProduct)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblFilterCategory)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbFilterCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlFilterLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(sepProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlFilterLayout.createSequentialGroup()
                                        .addComponent(lblFilterMarginTitle)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblFilterMinMargin)
                                            .addComponent(lblFilterMaxMargin))
                                        .addGap(4, 4, 4)
                                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtFilterMinMargin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtFilterMaxMargin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(pnlFilterLayout.createSequentialGroup()
                                        .addComponent(lblFilterPriceTitle)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblFilterMinPrice)
                                            .addComponent(lblFilterMaxPrice))
                                        .addGap(4, 4, 4)
                                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtFilterMinPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtFilterMaxPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(sepPrice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(19, 19, 19)))
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApplyFilters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearFilters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        spProducts.setBackground(new java.awt.Color(255, 255, 255));
        spProducts.setBorder(javax.swing.BorderFactory.createTitledBorder("Productes"));

        tblProducts.setForeground(new java.awt.Color(33, 37, 41));
        tblProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Categoria", "Proveïdor", "Preu", "Estoc", "Marge (%)", "Caducitat", "Impost (%)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProducts.setFillsViewportHeight(true);
        tblProducts.setGridColor(new java.awt.Color(220, 220, 220));
        tblProducts.setRowHeight(26);
        tblProducts.setSelectionBackground(new java.awt.Color(232, 240, 254));
        tblProducts.setSelectionForeground(new java.awt.Color(33, 37, 41));
        tblProducts.setShowHorizontalLines(true);
        tblProducts.setShowVerticalLines(true);
        spProducts.setViewportView(tblProducts);

        pnlForm.setBackground(new java.awt.Color(255, 255, 255));
        pnlForm.setBorder(javax.swing.BorderFactory.createTitledBorder("Dades del producte"));
        pnlForm.setToolTipText("");

        lblCategory.setBackground(new java.awt.Color(255, 255, 255));
        lblCategory.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblCategory.setForeground(new java.awt.Color(73, 80, 87));
        lblCategory.setText("Categoria");

        cmbCategory.setForeground(new java.awt.Color(33, 37, 41));

        lblPrice.setBackground(new java.awt.Color(255, 255, 255));
        lblPrice.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblPrice.setForeground(new java.awt.Color(73, 80, 87));
        lblPrice.setText("Preu");

        lblCooperativeMargin.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblCooperativeMargin.setForeground(new java.awt.Color(73, 80, 87));
        lblCooperativeMargin.setText("Marge cooperativa (%)");

        lblStock.setBackground(new java.awt.Color(255, 255, 255));
        lblStock.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblStock.setForeground(new java.awt.Color(73, 80, 87));
        lblStock.setText("Estoc");

        lblExpirationDate.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblExpirationDate.setForeground(new java.awt.Color(73, 80, 87));
        lblExpirationDate.setText("Data de caducitat");

        lblTax.setBackground(new java.awt.Color(255, 255, 255));
        lblTax.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblTax.setForeground(new java.awt.Color(73, 80, 87));
        lblTax.setText("Impost (%)");

        pnlButtons.setBackground(new java.awt.Color(255, 255, 255));

        btnNew.setForeground(new java.awt.Color(30, 111, 168));
        btnNew.setText("Nou");
        btnNew.setFocusPainted(false);
        btnNew.setPreferredSize(new java.awt.Dimension(90, 30));
        btnNew.addActionListener(this::btnNewActionPerformed);

        btnSave.setBackground(new java.awt.Color(63, 175, 92));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("Desa");
        btnSave.setPreferredSize(new java.awt.Dimension(90, 30));
        btnSave.addActionListener(this::btnSaveActionPerformed);

        btnDelete.setForeground(new java.awt.Color(120, 120, 120));
        btnDelete.setText("Elimina");
        btnDelete.setPreferredSize(new java.awt.Dimension(90, 30));
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        javax.swing.GroupLayout pnlButtonsLayout = new javax.swing.GroupLayout(pnlButtons);
        pnlButtons.setLayout(pnlButtonsLayout);
        pnlButtonsLayout.setHorizontalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlButtonsLayout.setVerticalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlFormLayout = new javax.swing.GroupLayout(pnlForm);
        pnlForm.setLayout(pnlFormLayout);
        pnlFormLayout.setHorizontalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtExpirationDate, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtStock, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtPrice, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblCategory, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblStock, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblCooperativeMargin, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cmbCategory, javax.swing.GroupLayout.Alignment.LEADING, 0, 179, Short.MAX_VALUE)
                        .addComponent(lblTax, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblExpirationDate, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblPrice, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtCooperativeMargin))
                    .addComponent(txtTax, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnlButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlFormLayout.setVerticalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(lblPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(lblCooperativeMargin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCooperativeMargin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblStock)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblExpirationDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtExpirationDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTax)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 1128, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(spProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 808, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pnlForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Neteja el formulari per preparar l'alta d'un nou producte.
     *
     * @param evt esdeveniment del botó
     */
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        clearForm();
    }//GEN-LAST:event_btnNewActionPerformed

    /**
     * Desa el producte actual, ja sigui creant-lo o modificant-lo.
     *
     * @param evt esdeveniment del botó
     */
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            String productCategory = (String) cmbCategory.getSelectedItem();

            if (productCategory == null || productCategory.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Has de seleccionar una categoria.",
                        "Avís",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String expirationDate = txtExpirationDate.getText().trim();

            if (expirationDate.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Has d'introduir la data de caducitat.",
                        "Avís",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            LocalDate.parse(expirationDate);

            Double price = Double.valueOf(txtPrice.getText().trim());
            Double cooperativeMargin = Double.valueOf(txtCooperativeMargin.getText().trim());
            Integer stock = Integer.valueOf(txtStock.getText().trim());
            Double tax = Double.valueOf(txtTax.getText().trim());

            ClientResponse<Void> response = productsService.saveProduct(
                    selectedProductId,
                    productCategory,
                    price,
                    cooperativeMargin,
                    stock,
                    expirationDate,
                    tax
            );

            if (response.isSuccess()) {
                JOptionPane.showMessageDialog(
                        this,
                        response.getMessage(),
                        "Informació",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadProducts();
                clearForm();
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Preu, marge (%), estoc i impost (%) han de ser numèrics.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "La data de caducitat ha de tenir format yyyy-MM-dd.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    /**
     * Elimina el producte seleccionat després de confirmar l'acció.
     *
     * @param evt esdeveniment del botó
     */
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if (selectedProductId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Has de seleccionar un producte.",
                    "Avís",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int option = JOptionPane.showConfirmDialog(
                this,
                "Vols eliminar el producte seleccionat?",
                "Confirmació",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        ClientResponse<Void> response = productsService.deleteProduct(selectedProductId);

        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    "Informació",
                    JOptionPane.INFORMATION_MESSAGE
            );
            loadProducts();
            clearForm();
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                response.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnApplyFiltersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyFiltersActionPerformed
        applyFilters();
    }//GEN-LAST:event_btnApplyFiltersActionPerformed

    private void btnClearFiltersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFiltersActionPerformed
        clearFilters();
    }//GEN-LAST:event_btnClearFiltersActionPerformed

    /**
     * Aplica els filtres introduïts al panell.
     */
    private void applyFilters() {
        try {
            String category = getSelectedFilterCategory();

            Double minPrice = parseDoubleFilter(txtFilterMinPrice.getText());
            Double maxPrice = parseDoubleFilter(txtFilterMaxPrice.getText());

            Double minMargin = parseDoubleFilter(txtFilterMinMargin.getText());
            Double maxMargin = parseDoubleFilter(txtFilterMaxMargin.getText());

            Integer minStock = parseIntegerFilter(txtFilterMinStock.getText());

            String expirationAfter = parseDateFilter(txtFilterExpirationAfter.getText());
            String expirationBefore = parseDateFilter(txtFilterExpirationBefore.getText());

            Double tax = parseDoubleFilter(txtFilterTax.getText());

            if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
                JOptionPane.showMessageDialog(
                        this,
                        "El preu mínim no pot ser superior al preu màxim.",
                        "Avís",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (minMargin != null && maxMargin != null && minMargin > maxMargin) {
                JOptionPane.showMessageDialog(
                        this,
                        "El marge (%) mínim no pot ser superior al marge (%) màxim.",
                        "Avís",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (expirationAfter != null && expirationBefore != null
                    && LocalDate.parse(expirationAfter).isAfter(LocalDate.parse(expirationBefore))) {
                JOptionPane.showMessageDialog(
                        this,
                        "La data inicial no pot ser posterior a la data final.",
                        "Avís",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Long supplierId = null;
            Long userId = null;
            Map<Long, String> supplierNames = new HashMap<>();

            Role currentRole = SessionManager.getCurrentUser().getRole();

            if (currentRole == Role.SUPPLIER) {
                ClientResponse<ManagedSupplier> supplierResponse = suppliersService.getMySupplier();

                if (!supplierResponse.isSuccess()) {
                    JOptionPane.showMessageDialog(
                            this,
                            supplierResponse.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                ManagedSupplier currentSupplier = supplierResponse.getData();
                supplierId = currentSupplier.getId();
                supplierNames.put(supplierId, currentSupplier.getName());

                if (minStock == null) {
                    minStock = 0;
                }

                if (expirationAfter == null) {
                    expirationAfter = "1900-01-01";
                }

            } else if (currentRole == Role.ADMIN) {
                supplierNames = getSupplierNamesById();

                if (minStock == null) {
                    minStock = 0;
                }

                if (expirationAfter == null) {
                    expirationAfter = "1900-01-01";
                }

            } else if (currentRole == Role.CUSTOMER) {
                if (minStock == null || minStock < 1) {
                    minStock = 1;
                }

                LocalDate today = LocalDate.now();

                if (expirationAfter == null
                        || LocalDate.parse(expirationAfter).isBefore(today)) {
                    expirationAfter = today.toString();
                }
            }

            ClientResponse<List<ManagedProduct>> response = productsService.filterProducts(
                    category,
                    supplierId,
                    userId,
                    expirationAfter,
                    expirationBefore,
                    minStock,
                    minPrice,
                    maxPrice,
                    minMargin,
                    maxMargin,
                    tax
            );

            if (!response.isSuccess()) {
                JOptionPane.showMessageDialog(
                        this,
                        response.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            fillProductsTable(response.getData(), supplierNames);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Els camps numèrics han de tenir un format correcte.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Les dates han de tenir format yyyy-MM-dd.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Neteja els camps del filtre i recarrega els productes.
     */
    private void clearFilters() {
        cmbFilterCategory.setSelectedIndex(0);
        txtFilterMinPrice.setText("");
        txtFilterMaxPrice.setText("");
        txtFilterMinMargin.setText("");
        txtFilterMaxMargin.setText("");
        txtFilterMinStock.setText("");
        txtFilterExpirationAfter.setText("");
        txtFilterExpirationBefore.setText("");
        txtFilterTax.setText("");

        loadProducts();
    }

    /**
     * Retorna la categoria seleccionada al filtre.
     *
     * @return categoria seleccionada o null si no es filtra per categoria
     */
    private String getSelectedFilterCategory() {
        Object selectedCategory = cmbFilterCategory.getSelectedItem();

        if (selectedCategory == null) {
            return null;
        }

        String category = selectedCategory.toString().trim();

        if (category.isEmpty() || "Totes".equals(category)) {
            return null;
        }

        return category;
    }

    /**
     * Converteix un text del filtre a Double.
     *
     * @param value text introduït
     * @return valor numèric o null si està buit
     */
    private Double parseDoubleFilter(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return Double.valueOf(value.trim().replace(",", "."));
    }

    /**
     * Converteix un text del filtre a Integer.
     *
     * @param value text introduït
     * @return valor enter o null si està buit
     */
    private Integer parseIntegerFilter(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return Integer.valueOf(value.trim());
    }

    /**
     * Valida una data del filtre.
     *
     * @param value text introduït
     * @return data en format text o null si està buida
     */
    private String parseDateFilter(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String date = value.trim();
        LocalDate.parse(date);
        return date;
    }

    /**
     * Mostra els productes indicats a la taula.
     *
     * @param products llista de productes
     * @param supplierNames noms dels proveïdors per identificador
     */
    private void fillProductsTable(
            List<ManagedProduct> products,
            Map<Long, String> supplierNames
    ) {
        tableModel.setRowCount(0);

        for (ManagedProduct product : products) {
            tableModel.addRow(new Object[]{
                product.getId(),
                product.getProductCategory(),
                getSupplierName(product.getSupplierId(), supplierNames),
                product.getPrice(),
                product.getStock(),
                product.getCooperativeMargin(),
                product.getExpirationDate(),
                product.getTax()
            });
        }
    }

    /**
     * Carrega els noms dels proveïdors indexats pel seu identificador.
     *
     * @return mapa amb id de proveïdor i nom de proveïdor
     */
    private Map<Long, String> getSupplierNamesById() {
        Map<Long, String> supplierNames = new HashMap<>();

        ClientResponse<List<ManagedSupplier>> response = suppliersService.getAllSuppliers();

        if (!response.isSuccess() || response.getData() == null) {
            return supplierNames;
        }

        for (ManagedSupplier supplier : response.getData()) {
            if (supplier.getId() != null) {
                supplierNames.put(supplier.getId(), supplier.getName());
            }
        }

        return supplierNames;
    }

    /**
     * Retorna el nom del proveïdor associat a un producte.
     *
     * @param supplierId identificador del proveïdor
     * @param supplierNames mapa de noms de proveïdors
     * @return nom del proveïdor o text per defecte
     */
    private String getSupplierName(Long supplierId, Map<Long, String> supplierNames) {
        if (SessionManager.getCurrentUser() != null
                && SessionManager.getCurrentUser().getRole() == Role.CUSTOMER) {
            return "";
        }

        if (supplierId == null) {
            return "Sense proveïdor";
        }

        return supplierNames.getOrDefault(supplierId, "No disponible");
    }

    /**
     * Amaga la columna de proveïdor quan no aporta informació al rol actual.
     */
    private void configureSupplierColumnVisibility() {
        if (SessionManager.getCurrentUser() == null) {
            return;
        }

        Role currentRole = SessionManager.getCurrentUser().getRole();

        if (currentRole != Role.SUPPLIER && currentRole != Role.CUSTOMER) {
            return;
        }

        for (int i = 0; i < tblProducts.getColumnModel().getColumnCount(); i++) {
            Object header = tblProducts.getColumnModel().getColumn(i).getHeaderValue();

            if ("Proveïdor".equals(header)) {
                tblProducts.removeColumn(tblProducts.getColumnModel().getColumn(i));
                return;
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApplyFilters;
    private javax.swing.JButton btnClearFilters;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JComboBox<String> cmbFilterCategory;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblCooperativeMargin;
    private javax.swing.JLabel lblExpirationDate;
    private javax.swing.JLabel lblFilterCategory;
    private javax.swing.JLabel lblFilterExpirationAfter;
    private javax.swing.JLabel lblFilterExpirationBefore;
    private javax.swing.JLabel lblFilterExpirationTitle;
    private javax.swing.JLabel lblFilterMarginTitle;
    private javax.swing.JLabel lblFilterMaxMargin;
    private javax.swing.JLabel lblFilterMaxPrice;
    private javax.swing.JLabel lblFilterMinMargin;
    private javax.swing.JLabel lblFilterMinPrice;
    private javax.swing.JLabel lblFilterMinStock;
    private javax.swing.JLabel lblFilterPriceTitle;
    private javax.swing.JLabel lblFilterProduct;
    private javax.swing.JLabel lblFilterStockTitle;
    private javax.swing.JLabel lblFilterTax;
    private javax.swing.JLabel lblFilterTaxTitle;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlFilter;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JSeparator sepExpiration;
    private javax.swing.JSeparator sepMargin;
    private javax.swing.JSeparator sepPrice;
    private javax.swing.JSeparator sepProduct;
    private javax.swing.JSeparator sepStock;
    private javax.swing.JScrollPane spProducts;
    private javax.swing.JTable tblProducts;
    private javax.swing.JTextField txtCooperativeMargin;
    private javax.swing.JTextField txtExpirationDate;
    private javax.swing.JTextField txtFilterExpirationAfter;
    private javax.swing.JTextField txtFilterExpirationBefore;
    private javax.swing.JTextField txtFilterMaxMargin;
    private javax.swing.JTextField txtFilterMaxPrice;
    private javax.swing.JTextField txtFilterMinMargin;
    private javax.swing.JTextField txtFilterMinPrice;
    private javax.swing.JTextField txtFilterMinStock;
    private javax.swing.JTextField txtFilterTax;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtTax;
    // End of variables declaration//GEN-END:variables
}
