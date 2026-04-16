package agrotech.view.panels;

import agrotech.client.ClientResponse;
import agrotech.model.ManagedProduct;
import agrotech.service.ProductsService;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import agrotech.model.Role;
import agrotech.session.SessionManager;
import javax.swing.DefaultComboBoxModel;

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
    private DefaultTableModel tableModel;
    private Long selectedProductId;

    /**
     * Crea el panell de gestió de productes.
     */
    public ProductsPanel() {
        initComponents();
        productsService = new ProductsService();
        initTableModel();
        initTableSelection();
        loadCategories(); 
        clearForm();
        loadProducts();
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
        Object priceValue = tableModel.getValueAt(modelRow, 2);
        Object stockValue = tableModel.getValueAt(modelRow, 3);
        Object marginValue = tableModel.getValueAt(modelRow, 4);
        Object expirationValue = tableModel.getValueAt(modelRow, 5);
        Object taxValue = tableModel.getValueAt(modelRow, 6);

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
        cmbCategory.setSelectedIndex(0);
        txtPrice.setText("");
        txtCooperativeMargin.setText("");
        txtStock.setText("");
        txtExpirationDate.setText("");
        txtTax.setText("");
        tblProducts.clearSelection();
        configurePermissionsByRole();
        txtPrice.requestFocus();
    }

    /**
     * Carrega la llista de productes a la taula.
     */
    private void loadProducts() {
        ClientResponse<List<ManagedProduct>> response = productsService.getAllProducts();

        if (!response.isSuccess()) {
            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        tableModel.setRowCount(0);

        for (ManagedProduct product : response.getData()) {
            tableModel.addRow(new Object[]{
                product.getId(),
                product.getProductCategory(),
                product.getPrice(),
                product.getStock(),
                product.getCooperativeMargin(),
                product.getExpirationDate(),
                product.getTax()
            });
        }
    }

    /**
     * Carrega les categories disponibles des del backend al desplegable.
     */
    private void loadCategories() {
        ClientResponse<List<String>> response = productsService.getAllCategories();

        if (!response.isSuccess()) {
            return;
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String category : response.getData()) {
            model.addElement(category);
        }
        cmbCategory.setModel(model);
    }

    /**
     * Configura els botons segons el rol de l'usuari autenticat.
     */
    private void configurePermissionsByRole() {
        Role currentRole = SessionManager.getCurrentUser().getRole();

        if (currentRole == Role.ADMIN) {
            btnNew.setVisible(false);
            btnSave.setText("Desa");
            btnSave.setEnabled(selectedProductId != null);
            btnDelete.setEnabled(selectedProductId != null);
            return;
        }

        btnNew.setVisible(true);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(selectedProductId != null);

        if (selectedProductId == null) {
            btnSave.setText("Crea");
        } else {
            btnSave.setText("Desa");
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

        spProducts.setBackground(new java.awt.Color(255, 255, 255));
        spProducts.setBorder(javax.swing.BorderFactory.createTitledBorder("Productes"));

        tblProducts.setForeground(new java.awt.Color(33, 37, 41));
        tblProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Categoria", "Preu", "Estoc", "Marge", "Caducitat", "Impost"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
        cmbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AIGUA", "OLI", "MELO", "SINDRIA", "TOMAQUET", "POMA" }));

        lblPrice.setBackground(new java.awt.Color(255, 255, 255));
        lblPrice.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblPrice.setForeground(new java.awt.Color(73, 80, 87));
        lblPrice.setText("Preu");

        lblCooperativeMargin.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblCooperativeMargin.setForeground(new java.awt.Color(73, 80, 87));
        lblCooperativeMargin.setText("Marge cooperativa");

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
        lblTax.setText("Impost");

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
                .addContainerGap(36, Short.MAX_VALUE))
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
            .addComponent(pnlButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(pnlButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(spProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pnlForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        clearForm();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            String productCategory = (String) cmbCategory.getSelectedItem();
            Double price = Double.valueOf(txtPrice.getText().trim());
            Double cooperativeMargin = Double.valueOf(txtCooperativeMargin.getText().trim());
            Integer stock = Integer.valueOf(txtStock.getText().trim());
            String expirationDate = txtExpirationDate.getText().trim();
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
                    "Preu, marge, estoc i impost han de ser numèrics.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnSaveActionPerformed

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblCooperativeMargin;
    private javax.swing.JLabel lblExpirationDate;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JScrollPane spProducts;
    private javax.swing.JTable tblProducts;
    private javax.swing.JTextField txtCooperativeMargin;
    private javax.swing.JTextField txtExpirationDate;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtTax;
    // End of variables declaration//GEN-END:variables
}
