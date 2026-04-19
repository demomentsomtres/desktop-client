package agrotech.view.panels;

import agrotech.client.ClientResponse;
import agrotech.model.ManagedOrder;
import agrotech.service.OrdersService;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import agrotech.model.Role;
import agrotech.session.SessionManager;
import javax.swing.SwingUtilities;

/**
 * Panell de gestió de comandes de l'aplicació.
 *
 * <p>
 * Permet consultar i gestionar comandes. Accessible per a usuaris amb rol ADMIN
 * o CUSTOMER amb funcionalitats diferents segons el rol.</p>
 *
 * @author Javier Santana Coronado
 */
public class OrdersPanel extends javax.swing.JPanel {

    private final OrdersService ordersService;
    private DefaultTableModel tableModel;
    private Long selectedOrderId;
    private DefaultTableModel linesTableModel;

    /**
     * Crea el panell de gestió de comandes.
     */
    public OrdersPanel() {
        initComponents();
        configureViewByRole();
        ordersService = new OrdersService();
        initTableModel();
        initTableSelection();
        clearDetail();
        loadOrders();
    }

    /**
     * Configura la vista segons el rol de l'usuari autenticat.
     */
    private void configureViewByRole() {
        Role role = SessionManager.getCurrentUser().getRole();

        if (role == Role.ADMIN) {
            lblTitle.setText("Gestió de comandes");
            pnlFilter.setVisible(true);
            btnNewOrder.setVisible(false);
            lblCustomerId.setVisible(true);
            lblCustomerIdValue.setVisible(true);
        } else {
            lblTitle.setText("Les meves comandes");
            pnlFilter.setVisible(false);
            btnNewOrder.setVisible(true);
            lblCustomerId.setVisible(false);
            lblCustomerIdValue.setVisible(false);
        }
    }

    /**
     * Inicialitza el model de la taula de comandes.
     */
    private void initTableModel() {
        tableModel = (DefaultTableModel) tblOrders.getModel();
        linesTableModel = (DefaultTableModel) tblLines.getModel();
    }

    /**
     * Neteja el detall i reinicia la selecció.
     */
    private void clearDetail() {
        selectedOrderId = null;
        tblOrders.clearSelection();

        lblIdValue.setText("—");
        lblCustomerIdValue.setText("—");
        lblDateValue.setText("—");
        lblPoblacioValue.setText("—");
        lblTotalValue.setText("—");
        lblTaxValue.setText("—");
        lblMarginValue.setText("—");
        linesTableModel.setRowCount(0);
    }

    /**
     * Carrega les comandes a la taula segons el rol de l'usuari.
     */
    private void loadOrders() {
        Role role = SessionManager.getCurrentUser().getRole();
        ClientResponse<List<ManagedOrder>> response;

        if (role == Role.ADMIN) {
            response = ordersService.getOrdersByFilters(
                    null, null, null, null,
                    null, null, null, null, null, null
            );
        } else {
            response = ordersService.getMyOrders();
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

        populateTable(response.getData());
    }

    /**
     * Omple la taula amb la llista de comandes indicada.
     *
     * @param orders llista de comandes a mostrar
     */
    private void populateTable(List<ManagedOrder> orders) {
        tableModel.setRowCount(0);

        for (ManagedOrder order : orders) {
            tableModel.addRow(new Object[]{
                order.getId(),
                order.getDate(),
                order.getPoblacioDesti(),
                order.getTotalOrderPrice()
            });
        }
    }

    /**
     * Inicialitza la selecció de la taula.
     */
    private void initTableSelection() {
        tblOrders.getSelectionModel().addListSelectionListener(evt -> {
            if (!evt.getValueIsAdjusting()) {
                loadSelectedOrder();
            }
        });
    }

    /**
     * Carrega la comanda seleccionada al detall.
     */
    private void loadSelectedOrder() {
        int selectedRow = tblOrders.getSelectedRow();

        if (selectedRow == -1) {
            selectedOrderId = null;
            clearDetail();
            return;
        }

        int modelRow = tblOrders.convertRowIndexToModel(selectedRow);
        Object idValue = tableModel.getValueAt(modelRow, 0);
        selectedOrderId = Long.valueOf(idValue.toString());

        ClientResponse<ManagedOrder> response = ordersService.getOrder(selectedOrderId);

        if (!response.isSuccess()) {
            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        ManagedOrder order = response.getData();

        lblIdValue.setText(order.getId().toString());
        lblCustomerIdValue.setText(order.getCustomerId() != null ? order.getCustomerId() : "—");
        lblDateValue.setText(order.getDate() != null ? order.getDate() : "—");
        lblPoblacioValue.setText(order.getPoblacioDesti() != null ? order.getPoblacioDesti() : "—");
        lblTotalValue.setText(order.getTotalOrderPrice() != null ? order.getTotalOrderPrice() + " €" : "—");
        lblTaxValue.setText(order.getTotalOrderTax() != null ? order.getTotalOrderTax() + " €" : "—");
        lblMarginValue.setText(order.getTotalOrderCooperativeMargin() != null ? order.getTotalOrderCooperativeMargin() + " €" : "—");

        linesTableModel.setRowCount(0);

        if (order.getOrderLines() != null) {
            for (agrotech.dto.order.OrderLineDto line : order.getOrderLines()) {
                linesTableModel.addRow(new Object[]{
                    line.getProductCategory(),
                    line.getQuantity(),
                    line.getUnitPrice() + " €",
                    line.getTotalPrice() + " €"
                });
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
        lblFilterPoblacio = new javax.swing.JLabel();
        txtFilterPoblacio = new javax.swing.JTextField();
        lblFilterTelefon = new javax.swing.JLabel();
        txtFilterTelefon = new javax.swing.JTextField();
        btnFilter = new javax.swing.JButton();
        btnClearFilter = new javax.swing.JButton();
        spOrders = new javax.swing.JScrollPane();
        tblOrders = new javax.swing.JTable();
        pnlForm = new javax.swing.JPanel();
        lblId = new javax.swing.JLabel();
        lblIdValue = new javax.swing.JLabel();
        lblCustomerId = new javax.swing.JLabel();
        lblCustomerIdValue = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblDateValue = new javax.swing.JLabel();
        lblPoblacio = new javax.swing.JLabel();
        lblPoblacioValue = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblTotalValue = new javax.swing.JLabel();
        btnNewOrder = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblTax = new javax.swing.JLabel();
        lblTaxValue = new javax.swing.JLabel();
        lblMargin = new javax.swing.JLabel();
        lblMarginValue = new javax.swing.JLabel();
        lblLines = new javax.swing.JLabel();
        spLines = new javax.swing.JScrollPane();
        tblLines = new javax.swing.JTable();

        setBackground(new java.awt.Color(245, 247, 248));

        lblTitle.setBackground(new java.awt.Color(255, 255, 255));
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(30, 111, 168));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Les meves comandes");

        pnlFilter.setBackground(new java.awt.Color(255, 255, 255));
        pnlFilter.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtre"));

        lblFilterPoblacio.setText("Població:");

        txtFilterPoblacio.setColumns(15);
        txtFilterPoblacio.setForeground(new java.awt.Color(33, 37, 41));

        lblFilterTelefon.setText("Telèfon:");

        txtFilterTelefon.setColumns(15);
        txtFilterTelefon.setForeground(new java.awt.Color(33, 37, 41));

        btnFilter.setForeground(new java.awt.Color(30, 111, 168));
        btnFilter.setText("Filtra");
        btnFilter.setFocusPainted(false);
        btnFilter.setPreferredSize(new java.awt.Dimension(90, 30));
        btnFilter.addActionListener(this::btnFilterActionPerformed);

        btnClearFilter.setForeground(new java.awt.Color(30, 111, 168));
        btnClearFilter.setText("Neteja");
        btnClearFilter.setFocusPainted(false);
        btnClearFilter.setPreferredSize(new java.awt.Dimension(90, 30));
        btnClearFilter.addActionListener(this::btnClearFilterActionPerformed);

        javax.swing.GroupLayout pnlFilterLayout = new javax.swing.GroupLayout(pnlFilter);
        pnlFilter.setLayout(pnlFilterLayout);
        pnlFilterLayout.setHorizontalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFilterPoblacio, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFilterPoblacio)
                    .addComponent(lblFilterTelefon)
                    .addComponent(txtFilterTelefon, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlFilterLayout.createSequentialGroup()
                        .addComponent(btnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnClearFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlFilterLayout.setVerticalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addComponent(lblFilterPoblacio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFilterPoblacio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblFilterTelefon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFilterTelefon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClearFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        spOrders.setBackground(new java.awt.Color(255, 255, 255));
        spOrders.setBorder(javax.swing.BorderFactory.createTitledBorder("Comandes"));

        tblOrders.setForeground(new java.awt.Color(33, 37, 41));
        tblOrders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Data", "Població", "Total €"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblOrders.setFillsViewportHeight(true);
        tblOrders.setGridColor(new java.awt.Color(220, 220, 220));
        tblOrders.setRowHeight(26);
        tblOrders.setSelectionBackground(new java.awt.Color(232, 240, 254));
        tblOrders.setSelectionForeground(new java.awt.Color(33, 37, 41));
        tblOrders.setShowHorizontalLines(true);
        tblOrders.setShowVerticalLines(true);
        spOrders.setViewportView(tblOrders);

        pnlForm.setBackground(new java.awt.Color(255, 255, 255));
        pnlForm.setBorder(javax.swing.BorderFactory.createTitledBorder("Detall de la comanda"));
        pnlForm.setToolTipText("");

        lblId.setBackground(new java.awt.Color(255, 255, 255));
        lblId.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblId.setForeground(new java.awt.Color(73, 80, 87));
        lblId.setText("ID:");

        lblIdValue.setText("—");

        lblCustomerId.setBackground(new java.awt.Color(255, 255, 255));
        lblCustomerId.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblCustomerId.setForeground(new java.awt.Color(73, 80, 87));
        lblCustomerId.setText("Client ID:");

        lblCustomerIdValue.setText("—");

        lblDate.setBackground(new java.awt.Color(255, 255, 255));
        lblDate.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDate.setForeground(new java.awt.Color(73, 80, 87));
        lblDate.setText("Data:");

        lblDateValue.setText("—");

        lblPoblacio.setBackground(new java.awt.Color(255, 255, 255));
        lblPoblacio.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblPoblacio.setForeground(new java.awt.Color(73, 80, 87));
        lblPoblacio.setText("Població:");

        lblPoblacioValue.setText("—");

        lblTotal.setBackground(new java.awt.Color(255, 255, 255));
        lblTotal.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(73, 80, 87));
        lblTotal.setText("Total €:");

        lblTotalValue.setText("—");

        btnNewOrder.setForeground(new java.awt.Color(30, 111, 168));
        btnNewOrder.setText("Nova comanda");
        btnNewOrder.setFocusPainted(false);
        btnNewOrder.setPreferredSize(new java.awt.Dimension(90, 30));
        btnNewOrder.addActionListener(this::btnNewOrderActionPerformed);

        btnDelete.setForeground(new java.awt.Color(120, 120, 120));
        btnDelete.setText("Elimina");
        btnDelete.setPreferredSize(new java.awt.Dimension(90, 30));
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        lblTax.setBackground(new java.awt.Color(255, 255, 255));
        lblTax.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblTax.setForeground(new java.awt.Color(73, 80, 87));
        lblTax.setText("Impostos:");

        lblTaxValue.setText("—");

        lblMargin.setBackground(new java.awt.Color(255, 255, 255));
        lblMargin.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblMargin.setForeground(new java.awt.Color(73, 80, 87));
        lblMargin.setText("Marge coop:");

        lblMarginValue.setText("—");

        lblLines.setBackground(new java.awt.Color(255, 255, 255));
        lblLines.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblLines.setForeground(new java.awt.Color(73, 80, 87));
        lblLines.setText("Línies:");

        tblLines.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Categoria", "Quantitat", "Preu/u €", "Total €"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spLines.setViewportView(tblLines);

        javax.swing.GroupLayout pnlFormLayout = new javax.swing.GroupLayout(pnlForm);
        pnlForm.setLayout(pnlFormLayout);
        pnlFormLayout.setHorizontalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spLines, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPoblacio)
                            .addComponent(lblPoblacioValue)
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblId)
                                    .addComponent(lblIdValue)
                                    .addComponent(lblCustomerId)
                                    .addComponent(lblCustomerIdValue)
                                    .addComponent(lblDate)
                                    .addComponent(lblDateValue))
                                .addGap(55, 55, 55)
                                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTotal)
                                    .addComponent(lblTotalValue)
                                    .addComponent(lblTax)
                                    .addComponent(lblTaxValue)
                                    .addComponent(lblMargin)
                                    .addComponent(lblMarginValue)))
                            .addComponent(lblLines))
                        .addGap(0, 124, Short.MAX_VALUE))
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addComponent(btnNewOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(28, 28, 28)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnlFormLayout.setVerticalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addComponent(lblId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblIdValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblCustomerId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCustomerIdValue))
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addComponent(lblTax)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTaxValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMargin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMarginValue)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addComponent(lblDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDateValue))
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addComponent(lblTotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalValue)))
                .addGap(18, 18, 18)
                .addComponent(lblPoblacio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPoblacioValue)
                .addGap(18, 18, 18)
                .addComponent(lblLines)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spLines, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNewOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(spOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spOrders)
                    .addComponent(pnlForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Neteja el filtre i torna a carregar tots els proveïdors.
     *
     * @param evt esdeveniment del botó
     */
    private void btnClearFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFilterActionPerformed

    }//GEN-LAST:event_btnClearFilterActionPerformed

    /**
     * Filtra proveïdors per població i/o telèfon.
     *
     * @param evt esdeveniment del botó
     */
    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed

    }//GEN-LAST:event_btnFilterActionPerformed

    /**
     * Gestiona el botó "Elimina": elimina la comanda seleccionada.
     *
     * @param evt esdeveniment del botó
     */
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if (selectedOrderId == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cal seleccionar una comanda.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int option = JOptionPane.showConfirmDialog(
                this,
                "Vols eliminar la comanda #" + selectedOrderId + "?",
                "Confirmació",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        ClientResponse<Void> response = ordersService.deleteOrder(selectedOrderId);

        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    "Informació",
                    JOptionPane.INFORMATION_MESSAGE
            );
            loadOrders();
            clearDetail();
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                response.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }//GEN-LAST:event_btnDeleteActionPerformed

    /**
     * Gestiona el botó "Nova comanda": obre el diàleg de creació.
     *
     * @param evt esdeveniment del botó
     */
    private void btnNewOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewOrderActionPerformed
        CreateOrderDialog dialog = new CreateOrderDialog(
                (java.awt.Frame) SwingUtilities.getWindowAncestor(this)
        );
        dialog.setVisible(true);

        if (dialog.isOrderConfirmed()) {
            loadOrders();
        }
    }//GEN-LAST:event_btnNewOrderActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearFilter;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnNewOrder;
    private javax.swing.JLabel lblCustomerId;
    private javax.swing.JLabel lblCustomerIdValue;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblDateValue;
    private javax.swing.JLabel lblFilterPoblacio;
    private javax.swing.JLabel lblFilterTelefon;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblIdValue;
    private javax.swing.JLabel lblLines;
    private javax.swing.JLabel lblMargin;
    private javax.swing.JLabel lblMarginValue;
    private javax.swing.JLabel lblPoblacio;
    private javax.swing.JLabel lblPoblacioValue;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel lblTaxValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalValue;
    private javax.swing.JPanel pnlFilter;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JScrollPane spLines;
    private javax.swing.JScrollPane spOrders;
    private javax.swing.JTable tblLines;
    private javax.swing.JTable tblOrders;
    private javax.swing.JTextField txtFilterPoblacio;
    private javax.swing.JTextField txtFilterTelefon;
    // End of variables declaration//GEN-END:variables
}
