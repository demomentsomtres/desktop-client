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
        lblDiscountValue.setText("—");
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
                formatMoney(order.getTotalOrderPrice())
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
        lblTotalValue.setText(formatMoney(order.getTotalOrderPrice()));
        lblTaxValue.setText(formatMoney(order.getTotalOrderTax()));
        lblMarginValue.setText(formatMoney(order.getTotalOrderCooperativeMargin()));
        lblDiscountValue.setText(formatMoney(order.getTotalDiscount()));

        linesTableModel.setRowCount(0);

        if (order.getOrderLines() != null) {
            for (agrotech.dto.order.OrderLineDto line : order.getOrderLines()) {
                linesTableModel.addRow(new Object[]{
                    line.getProductCategory(),
                    line.getQuantity(),
                    formatMoney(line.getUnitPrice()),
                    formatMoney(line.getTotalDiscount()),
                    formatMoney(line.getTotalPrice())
                });
            }
        }
    }

    /**
     * Dona format monetari a un import.
     *
     * @param amount import numèric
     * @return import formatat amb dos decimals i símbol d'euro
     */
    private String formatMoney(Double amount) {
        if (amount == null) {
            return "—";
        }

        return String.format("%.2f €", amount);
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
        lblSectionOrder = new javax.swing.JLabel();
        sepOrder = new javax.swing.JSeparator();
        lblFilterCustomerId = new javax.swing.JLabel();
        txtFilterCustomerId = new javax.swing.JTextField();
        lblFilterPoblacio = new javax.swing.JLabel();
        txtFilterPoblacio = new javax.swing.JTextField();
        lblFilterStartDate = new javax.swing.JLabel();
        txtFilterStartDate = new javax.swing.JTextField();
        lblFilterEndDate = new javax.swing.JLabel();
        txtFilterEndDate = new javax.swing.JTextField();
        lblSectionTotal = new javax.swing.JLabel();
        sepTotal = new javax.swing.JSeparator();
        lblFilterMinTotal = new javax.swing.JLabel();
        txtFilterMinTotal = new javax.swing.JTextField();
        lblFilterMaxTotal = new javax.swing.JLabel();
        txtFilterMaxTotal = new javax.swing.JTextField();
        lblSectionMargin = new javax.swing.JLabel();
        sepMargin = new javax.swing.JSeparator();
        lblFilterMinMargin = new javax.swing.JLabel();
        txtFilterMinMargin = new javax.swing.JTextField();
        lblFilterMaxMargin = new javax.swing.JLabel();
        txtFilterMaxMargin = new javax.swing.JTextField();
        lblSectionTax = new javax.swing.JLabel();
        sepTax = new javax.swing.JSeparator();
        lblFilterMinTax = new javax.swing.JLabel();
        txtFilterMinTax = new javax.swing.JTextField();
        lblFilterMaxTax = new javax.swing.JLabel();
        txtFilterMaxTax = new javax.swing.JTextField();
        btnFilter = new javax.swing.JButton();
        btnClearFilter = new javax.swing.JButton();
        spOrders = new javax.swing.JScrollPane();
        tblOrders = new javax.swing.JTable();
        pnlForm = new javax.swing.JPanel();
        lblId = new javax.swing.JLabel();
        lblIdValue = new javax.swing.JLabel();
        lblPoblacio = new javax.swing.JLabel();
        lblPoblacioValue = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        lblDateValue = new javax.swing.JLabel();
        lblCustomerId = new javax.swing.JLabel();
        lblCustomerIdValue = new javax.swing.JLabel();
        lblTax = new javax.swing.JLabel();
        lblTaxValue = new javax.swing.JLabel();
        lblMargin = new javax.swing.JLabel();
        lblMarginValue = new javax.swing.JLabel();
        lblDiscount = new javax.swing.JLabel();
        lblDiscountValue = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblTotalValue = new javax.swing.JLabel();
        lblLines = new javax.swing.JLabel();
        spLines = new javax.swing.JScrollPane();
        tblLines = new javax.swing.JTable();
        btnNewOrder = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setBackground(new java.awt.Color(245, 247, 248));

        lblTitle.setBackground(new java.awt.Color(255, 255, 255));
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(30, 111, 168));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Les meves comandes");

        pnlFilter.setBackground(new java.awt.Color(255, 255, 255));
        pnlFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)), "Filtre"));

        lblSectionOrder.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSectionOrder.setForeground(new java.awt.Color(30, 111, 168));
        lblSectionOrder.setText("Comanda");

        lblFilterCustomerId.setText("Client ID:");

        txtFilterCustomerId.setColumns(15);
        txtFilterCustomerId.setForeground(new java.awt.Color(33, 37, 41));

        lblFilterPoblacio.setText("Població");

        txtFilterPoblacio.setColumns(15);
        txtFilterPoblacio.setForeground(new java.awt.Color(33, 37, 41));

        lblFilterStartDate.setText("Des de (YYYY-MM-DD)");

        txtFilterStartDate.setColumns(15);
        txtFilterStartDate.setForeground(new java.awt.Color(33, 37, 41));

        lblFilterEndDate.setText("Fins a (YYYY-MM-DD)");

        txtFilterEndDate.setColumns(15);
        txtFilterEndDate.setForeground(new java.awt.Color(33, 37, 41));

        lblSectionTotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSectionTotal.setForeground(new java.awt.Color(30, 111, 168));
        lblSectionTotal.setText("Total €");

        lblFilterMinTotal.setText("Mínim");

        lblFilterMaxTotal.setText("Màxim");

        lblSectionMargin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSectionMargin.setForeground(new java.awt.Color(30, 111, 168));
        lblSectionMargin.setText("Marge cooperativa €");

        lblFilterMinMargin.setText("Mínim");

        lblFilterMaxMargin.setText("Màxim");

        lblSectionTax.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSectionTax.setForeground(new java.awt.Color(30, 111, 168));
        lblSectionTax.setText("Impostos €");

        lblFilterMinTax.setText("Mínim");

        lblFilterMaxTax.setText("Màxim");

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
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(sepOrder)
                    .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblSectionOrder)
                        .addGroup(pnlFilterLayout.createSequentialGroup()
                            .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblFilterCustomerId)
                                .addComponent(txtFilterCustomerId, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblFilterPoblacio)
                                .addComponent(txtFilterPoblacio, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblFilterStartDate)
                                .addComponent(txtFilterStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtFilterEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblFilterEndDate)))
                        .addGroup(pnlFilterLayout.createSequentialGroup()
                            .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sepTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlFilterLayout.createSequentialGroup()
                                    .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFilterMinTotal)
                                        .addComponent(txtFilterMinTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFilterMaxTotal)
                                        .addComponent(txtFilterMaxTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(lblSectionTotal))
                            .addGap(18, 18, 18)
                            .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sepMargin, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlFilterLayout.createSequentialGroup()
                                    .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFilterMinMargin)
                                        .addComponent(txtFilterMinMargin, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFilterMaxMargin)
                                        .addComponent(txtFilterMaxMargin, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(lblSectionMargin))
                            .addGap(18, 18, 18)
                            .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sepTax, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlFilterLayout.createSequentialGroup()
                                    .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFilterMinTax)
                                        .addComponent(txtFilterMinTax, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFilterMaxTax)
                                        .addComponent(txtFilterMaxTax, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(lblSectionTax)))
                        .addGroup(pnlFilterLayout.createSequentialGroup()
                            .addComponent(btnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnClearFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlFilterLayout.setVerticalGroup(
            pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilterLayout.createSequentialGroup()
                .addComponent(lblSectionOrder)
                .addGap(2, 2, 2)
                .addComponent(sepOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlFilterLayout.createSequentialGroup()
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlFilterLayout.createSequentialGroup()
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblFilterCustomerId)
                                    .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblFilterStartDate)
                                        .addComponent(lblFilterEndDate)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtFilterCustomerId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFilterPoblacio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFilterStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFilterEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlFilterLayout.createSequentialGroup()
                                .addComponent(lblFilterPoblacio)
                                .addGap(28, 28, 28)))
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFilterLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(lblSectionTotal)
                                .addGap(2, 2, 2)
                                .addComponent(sepTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFilterMaxTotal, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblFilterMinTotal))
                                .addGap(4, 4, 4)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtFilterMinTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFilterMaxTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFilterLayout.createSequentialGroup()
                                .addComponent(lblSectionTax)
                                .addGap(2, 2, 2)
                                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pnlFilterLayout.createSequentialGroup()
                                        .addComponent(sepTax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblFilterMinTax)
                                        .addGap(4, 4, 4)
                                        .addComponent(txtFilterMinTax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlFilterLayout.createSequentialGroup()
                                        .addComponent(lblFilterMaxTax)
                                        .addGap(4, 4, 4)
                                        .addComponent(txtFilterMaxTax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(pnlFilterLayout.createSequentialGroup()
                        .addComponent(lblSectionMargin)
                        .addGap(2, 2, 2)
                        .addComponent(sepMargin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFilterMaxMargin, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblFilterMinMargin))
                        .addGap(4, 4, 4)
                        .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFilterMinMargin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFilterMaxMargin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(pnlFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
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

        lblPoblacio.setBackground(new java.awt.Color(255, 255, 255));
        lblPoblacio.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblPoblacio.setForeground(new java.awt.Color(73, 80, 87));
        lblPoblacio.setText("Població:");

        lblPoblacioValue.setText("—");

        lblDate.setBackground(new java.awt.Color(255, 255, 255));
        lblDate.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDate.setForeground(new java.awt.Color(73, 80, 87));
        lblDate.setText("Data:");

        lblDateValue.setText("—");

        lblCustomerId.setBackground(new java.awt.Color(255, 255, 255));
        lblCustomerId.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblCustomerId.setForeground(new java.awt.Color(73, 80, 87));
        lblCustomerId.setText("Client ID:");

        lblCustomerIdValue.setText("—");

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

        lblDiscount.setBackground(new java.awt.Color(255, 255, 255));
        lblDiscount.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDiscount.setForeground(new java.awt.Color(73, 80, 87));
        lblDiscount.setText("Descompte:");

        lblDiscountValue.setText("—");

        lblTotal.setBackground(new java.awt.Color(255, 255, 255));
        lblTotal.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(73, 80, 87));
        lblTotal.setText("Total €:");

        lblTotalValue.setText("—");

        lblLines.setBackground(new java.awt.Color(255, 255, 255));
        lblLines.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblLines.setForeground(new java.awt.Color(73, 80, 87));
        lblLines.setText("Línies:");

        tblLines.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Categoria", "Quantitat", "Preu/u €", "Descompte €", "Total €"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spLines.setViewportView(tblLines);

        btnNewOrder.setForeground(new java.awt.Color(30, 111, 168));
        btnNewOrder.setText("Nova comanda");
        btnNewOrder.setFocusPainted(false);
        btnNewOrder.setPreferredSize(new java.awt.Dimension(90, 30));
        btnNewOrder.addActionListener(this::btnNewOrderActionPerformed);

        btnDelete.setForeground(new java.awt.Color(120, 120, 120));
        btnDelete.setText("Elimina");
        btnDelete.setPreferredSize(new java.awt.Dimension(90, 30));
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        javax.swing.GroupLayout pnlFormLayout = new javax.swing.GroupLayout(pnlForm);
        pnlForm.setLayout(pnlFormLayout);
        pnlFormLayout.setHorizontalGroup(
            pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spLines, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addComponent(btnNewOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTax, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(lblIdValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTaxValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblPoblacio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPoblacioValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMargin, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                            .addComponent(lblMarginValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDateValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDiscountValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCustomerId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCustomerIdValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTotalValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(lblLines))
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
                        .addComponent(lblTax)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTaxValue))
                    .addGroup(pnlFormLayout.createSequentialGroup()
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addComponent(lblPoblacio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPoblacioValue))
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addComponent(lblDate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDateValue))
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addComponent(lblCustomerId)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCustomerIdValue)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addComponent(lblMargin)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblMarginValue))
                            .addGroup(pnlFormLayout.createSequentialGroup()
                                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblDiscount)
                                    .addComponent(lblTotal))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDiscountValue))
                            .addComponent(lblTotalValue, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(18, 18, 18)
                .addComponent(lblLines)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spLines, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNewOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
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
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spOrders)
                    .addComponent(pnlForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Gestiona el botó "Neteja": neteja el filtre i torna a carregar totes les
     * comandes.
     *
     * @param evt esdeveniment del botó
     */
    private void btnClearFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFilterActionPerformed
        txtFilterCustomerId.setText("");
        txtFilterPoblacio.setText("");
        txtFilterStartDate.setText("");
        txtFilterEndDate.setText("");
        txtFilterMinTotal.setText("");
        txtFilterMaxTotal.setText("");
        txtFilterMinMargin.setText("");
        txtFilterMaxMargin.setText("");
        txtFilterMinTax.setText("");
        txtFilterMaxTax.setText("");
        loadOrders();
        clearDetail();
    }//GEN-LAST:event_btnClearFilterActionPerformed

    /**
     * Gestiona el botó "Filtra": filtra comandes amb els criteris indicats.
     *
     * @param evt esdeveniment del botó
     */
    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        Long customerId = null;
        Double minTotal = null;
        Double maxTotal = null;
        Double minMargin = null;
        Double maxMargin = null;
        Double minTax = null;
        Double maxTax = null;

        try {
            String customerIdText = txtFilterCustomerId.getText().trim();
            if (!customerIdText.isEmpty()) {
                customerId = Long.parseLong(customerIdText);
            }

            String minTotalText = txtFilterMinTotal.getText().trim();
            if (!minTotalText.isEmpty()) {
                minTotal = Double.parseDouble(minTotalText);
            }

            String maxTotalText = txtFilterMaxTotal.getText().trim();
            if (!maxTotalText.isEmpty()) {
                maxTotal = Double.parseDouble(maxTotalText);
            }

            String minMarginText = txtFilterMinMargin.getText().trim();
            if (!minMarginText.isEmpty()) {
                minMargin = Double.parseDouble(minMarginText);
            }

            String maxMarginText = txtFilterMaxMargin.getText().trim();
            if (!maxMarginText.isEmpty()) {
                maxMargin = Double.parseDouble(maxMarginText);
            }

            String minTaxText = txtFilterMinTax.getText().trim();
            if (!minTaxText.isEmpty()) {
                minTax = Double.parseDouble(minTaxText);
            }

            String maxTaxText = txtFilterMaxTax.getText().trim();
            if (!maxTaxText.isEmpty()) {
                maxTax = Double.parseDouble(maxTaxText);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Els camps numèrics han de contenir valors vàlids.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String poblacio = txtFilterPoblacio.getText().trim();
        String startDate = txtFilterStartDate.getText().trim();
        String endDate = txtFilterEndDate.getText().trim();

        ClientResponse<List<ManagedOrder>> response = ordersService.getOrdersByFilters(
                customerId,
                poblacio.isEmpty() ? null : poblacio,
                startDate.isEmpty() ? null : startDate,
                endDate.isEmpty() ? null : endDate,
                minTotal, maxTotal,
                minMargin, maxMargin,
                minTax, maxTax
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

        populateTable(response.getData());
        clearDetail();
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
    private javax.swing.JLabel lblDiscount;
    private javax.swing.JLabel lblDiscountValue;
    private javax.swing.JLabel lblFilterCustomerId;
    private javax.swing.JLabel lblFilterEndDate;
    private javax.swing.JLabel lblFilterMaxMargin;
    private javax.swing.JLabel lblFilterMaxTax;
    private javax.swing.JLabel lblFilterMaxTotal;
    private javax.swing.JLabel lblFilterMinMargin;
    private javax.swing.JLabel lblFilterMinTax;
    private javax.swing.JLabel lblFilterMinTotal;
    private javax.swing.JLabel lblFilterPoblacio;
    private javax.swing.JLabel lblFilterStartDate;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblIdValue;
    private javax.swing.JLabel lblLines;
    private javax.swing.JLabel lblMargin;
    private javax.swing.JLabel lblMarginValue;
    private javax.swing.JLabel lblPoblacio;
    private javax.swing.JLabel lblPoblacioValue;
    private javax.swing.JLabel lblSectionMargin;
    private javax.swing.JLabel lblSectionOrder;
    private javax.swing.JLabel lblSectionTax;
    private javax.swing.JLabel lblSectionTotal;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel lblTaxValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalValue;
    private javax.swing.JPanel pnlFilter;
    private javax.swing.JPanel pnlForm;
    private javax.swing.JSeparator sepMargin;
    private javax.swing.JSeparator sepOrder;
    private javax.swing.JSeparator sepTax;
    private javax.swing.JSeparator sepTotal;
    private javax.swing.JScrollPane spLines;
    private javax.swing.JScrollPane spOrders;
    private javax.swing.JTable tblLines;
    private javax.swing.JTable tblOrders;
    private javax.swing.JTextField txtFilterCustomerId;
    private javax.swing.JTextField txtFilterEndDate;
    private javax.swing.JTextField txtFilterMaxMargin;
    private javax.swing.JTextField txtFilterMaxTax;
    private javax.swing.JTextField txtFilterMaxTotal;
    private javax.swing.JTextField txtFilterMinMargin;
    private javax.swing.JTextField txtFilterMinTax;
    private javax.swing.JTextField txtFilterMinTotal;
    private javax.swing.JTextField txtFilterPoblacio;
    private javax.swing.JTextField txtFilterStartDate;
    // End of variables declaration//GEN-END:variables
}
