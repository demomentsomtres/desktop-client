package agrotech.view.panels;

import agrotech.client.ClientResponse;
import agrotech.dto.order.OrderLineDto;
import agrotech.model.ManagedOrder;
import agrotech.service.OrdersService;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Javier Santana Coronado
 */
public class AdminDashboardPanel extends javax.swing.JPanel {

    private final OrdersService ordersService;
    private DefaultTableModel salesTableModel;

    /**
     * Creates new form AdminDashboardPanel
     */
    public AdminDashboardPanel() {
        initComponents();
        ordersService = new OrdersService();
        initDashboard();
        loadSales();
    }

    /**
     * Inicialitza la configuració bàsica del dashboard.
     */
    private void initDashboard() {
        salesTableModel = (DefaultTableModel) tblSales.getModel();
        salesTableModel.setRowCount(0);
        tblSales.setRowHeight(24);
        tblSales.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * Carrega les comandes registrades per mostrar el resum de vendes.
     */
    private void loadSales() {
        ClientResponse<List<ManagedOrder>> response
                = ordersService.getAllOrdersForDashboard();

        if (!response.isSuccess()) {
            clearDashboard();

            JOptionPane.showMessageDialog(
                    this,
                    response.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        List<ManagedOrder> orders = response.getData();

        if (orders == null) {
            orders = Collections.emptyList();
        }

        fillSalesTable(orders);
        updateSummary(orders);
    }

    /**
     * Omple la taula amb les línies de venda de les comandes.
     *
     * @param orders llista de comandes carregades
     */
    private void fillSalesTable(List<ManagedOrder> orders) {
        salesTableModel.setRowCount(0);

        for (ManagedOrder order : orders) {
            if (order.getOrderLines() == null || order.getOrderLines().isEmpty()) {
                continue;
            }

            for (OrderLineDto line : order.getOrderLines()) {
                salesTableModel.addRow(new Object[]{
                    safeText(order.getId()),
                    safeText(order.getDate()),
                    safeText(order.getCustomerId()),
                    safeText(order.getPoblacioDesti()),
                    safeText(line.getProductCategory()),
                    safeText(line.getQuantity()),
                    formatMoney(line.getTotalPrice())
                });
            }
        }
    }

    /**
     * Actualitza les targetes de resum del dashboard.
     *
     * @param orders llista de comandes carregades
     */
    private void updateSummary(List<ManagedOrder> orders) {
        int ordersCount = orders.size();
        int soldProductsCount = calculateSoldProductsCount(orders);
        double totalSales = calculateTotalSales(orders);

        lblOrdersValue.setText(String.valueOf(ordersCount));
        lblSoldProductsValue.setText(String.valueOf(soldProductsCount));
        lblTotalSalesValue.setText(formatMoney(totalSales));
    }

    /**
     * Calcula el nombre total d'unitats venudes.
     *
     * @param orders llista de comandes carregades
     * @return total d'unitats venudes
     */
    private int calculateSoldProductsCount(List<ManagedOrder> orders) {
        int total = 0;

        for (ManagedOrder order : orders) {
            if (order.getOrderLines() == null) {
                continue;
            }

            for (OrderLineDto line : order.getOrderLines()) {
                if (line.getQuantity() != null) {
                    total += line.getQuantity();
                }
            }
        }

        return total;
    }

    /**
     * Calcula l'import total de vendes.
     *
     * @param orders llista de comandes carregades
     * @return import total de vendes
     */
    private double calculateTotalSales(List<ManagedOrder> orders) {
        double total = 0.0;

        for (ManagedOrder order : orders) {
            if (order.getTotalOrderPrice() != null) {
                total += order.getTotalOrderPrice();
            }
        }

        return total;
    }

    /**
     * Neteja les dades visibles del dashboard.
     */
    private void clearDashboard() {
        if (salesTableModel != null) {
            salesTableModel.setRowCount(0);
        }

        lblOrdersValue.setText("0");
        lblSoldProductsValue.setText("0");
        lblTotalSalesValue.setText("0,00 €");
    }

    /**
     * Dona format monetari a un import.
     *
     * @param amount import numèric
     * @return import formatat amb euros
     */
    private String formatMoney(Double amount) {
        if (amount == null) {
            return "0,00 €";
        }

        return String.format("%.2f €", amount);
    }

    /**
     * Dona format monetari a un import primitiu.
     *
     * @param amount import numèric
     * @return import formatat amb euros
     */
    private String formatMoney(double amount) {
        return String.format("%.2f €", amount);
    }

    /**
     * Converteix un valor en text segur per mostrar-lo a la interfície.
     *
     * @param value valor que es vol mostrar
     * @return text segur
     */
    private String safeText(Object value) {
        if (value == null) {
            return "—";
        }

        return String.valueOf(value);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        pnlSummary = new javax.swing.JPanel();
        pnlCardOrders = new javax.swing.JPanel();
        lblOrdersIcon = new javax.swing.JLabel();
        lblOrdersTitle = new javax.swing.JLabel();
        lblOrdersValue = new javax.swing.JLabel();
        pnlCardSoldProducts = new javax.swing.JPanel();
        lblSoldProductsIcon = new javax.swing.JLabel();
        lblSoldProductsTitle = new javax.swing.JLabel();
        lblSoldProductsValue = new javax.swing.JLabel();
        pnlCardTotalSales = new javax.swing.JPanel();
        lblTotalSalesIcon = new javax.swing.JLabel();
        lblTotalSalesTitle = new javax.swing.JLabel();
        lblTotalSalesValue = new javax.swing.JLabel();
        pnlSalesTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSales = new javax.swing.JTable();

        setBackground(new java.awt.Color(245, 247, 250));

        pnlHeader.setBackground(new java.awt.Color(255, 255, 255));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(0, 102, 170));
        lblTitle.setText("Vendes i activitat");

        btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRefresh.setText("Actualitza");
        btnRefresh.addActionListener(this::btnRefreshActionPerformed);

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(lblTitle))
                .addContainerGap())
        );

        pnlSummary.setBackground(new java.awt.Color(255, 255, 255));
        pnlSummary.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resum", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 102, 170))); // NOI18N

        pnlCardOrders.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardOrders.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        lblOrdersIcon.setFont(new java.awt.Font("Segoe UI Emoji", 0, 28)); // NOI18N
        lblOrdersIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOrdersIcon.setText("#");

        lblOrdersTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblOrdersTitle.setText("Comandes");

        lblOrdersValue.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblOrdersValue.setForeground(new java.awt.Color(0, 102, 170));
        lblOrdersValue.setText("—");

        javax.swing.GroupLayout pnlCardOrdersLayout = new javax.swing.GroupLayout(pnlCardOrders);
        pnlCardOrders.setLayout(pnlCardOrdersLayout);
        pnlCardOrdersLayout.setHorizontalGroup(
            pnlCardOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardOrdersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblOrdersIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCardOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOrdersTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(lblOrdersValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCardOrdersLayout.setVerticalGroup(
            pnlCardOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardOrdersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblOrdersIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCardOrdersLayout.createSequentialGroup()
                        .addComponent(lblOrdersTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOrdersValue, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCardSoldProducts.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardSoldProducts.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        lblSoldProductsIcon.setFont(new java.awt.Font("Segoe UI Emoji", 0, 28)); // NOI18N
        lblSoldProductsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSoldProductsIcon.setText("P");

        lblSoldProductsTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSoldProductsTitle.setText("Productes venuts");

        lblSoldProductsValue.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblSoldProductsValue.setForeground(new java.awt.Color(0, 102, 170));
        lblSoldProductsValue.setText("—");

        javax.swing.GroupLayout pnlCardSoldProductsLayout = new javax.swing.GroupLayout(pnlCardSoldProducts);
        pnlCardSoldProducts.setLayout(pnlCardSoldProductsLayout);
        pnlCardSoldProductsLayout.setHorizontalGroup(
            pnlCardSoldProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardSoldProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSoldProductsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCardSoldProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSoldProductsTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(lblSoldProductsValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCardSoldProductsLayout.setVerticalGroup(
            pnlCardSoldProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardSoldProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardSoldProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblSoldProductsIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCardSoldProductsLayout.createSequentialGroup()
                        .addComponent(lblSoldProductsTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSoldProductsValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCardTotalSales.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardTotalSales.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        lblTotalSalesIcon.setFont(new java.awt.Font("Segoe UI Emoji", 0, 28)); // NOI18N
        lblTotalSalesIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalSalesIcon.setText("€");

        lblTotalSalesTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotalSalesTitle.setText("Total vendes");

        lblTotalSalesValue.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTotalSalesValue.setForeground(new java.awt.Color(46, 125, 50));
        lblTotalSalesValue.setText("—");

        javax.swing.GroupLayout pnlCardTotalSalesLayout = new javax.swing.GroupLayout(pnlCardTotalSales);
        pnlCardTotalSales.setLayout(pnlCardTotalSalesLayout);
        pnlCardTotalSalesLayout.setHorizontalGroup(
            pnlCardTotalSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardTotalSalesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTotalSalesIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCardTotalSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalSalesTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(lblTotalSalesValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCardTotalSalesLayout.setVerticalGroup(
            pnlCardTotalSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardTotalSalesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardTotalSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTotalSalesIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCardTotalSalesLayout.createSequentialGroup()
                        .addComponent(lblTotalSalesTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalSalesValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlSummaryLayout = new javax.swing.GroupLayout(pnlSummary);
        pnlSummary.setLayout(pnlSummaryLayout);
        pnlSummaryLayout.setHorizontalGroup(
            pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCardOrders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlCardSoldProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlCardTotalSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSummaryLayout.setVerticalGroup(
            pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlCardTotalSales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCardOrders, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCardSoldProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlSalesTable.setBackground(new java.awt.Color(255, 255, 255));
        pnlSalesTable.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Vendes registrades", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 102, 170))); // NOI18N

        tblSales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Comanda", "Data", "Client ID", "Població", "Producte", "Quantitat", "Total €"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblSales);

        javax.swing.GroupLayout pnlSalesTableLayout = new javax.swing.GroupLayout(pnlSalesTable);
        pnlSalesTable.setLayout(pnlSalesTableLayout);
        pnlSalesTableLayout.setHorizontalGroup(
            pnlSalesTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSalesTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        pnlSalesTableLayout.setVerticalGroup(
            pnlSalesTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSalesTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSalesTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlSummary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSalesTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Actualitza manualment les dades del dashboard.
     *
     * @param evt esdeveniment del botó
     */
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadSales();
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblOrdersIcon;
    private javax.swing.JLabel lblOrdersTitle;
    private javax.swing.JLabel lblOrdersValue;
    private javax.swing.JLabel lblSoldProductsIcon;
    private javax.swing.JLabel lblSoldProductsTitle;
    private javax.swing.JLabel lblSoldProductsValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalSalesIcon;
    private javax.swing.JLabel lblTotalSalesTitle;
    private javax.swing.JLabel lblTotalSalesValue;
    private javax.swing.JPanel pnlCardOrders;
    private javax.swing.JPanel pnlCardSoldProducts;
    private javax.swing.JPanel pnlCardTotalSales;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlSalesTable;
    private javax.swing.JPanel pnlSummary;
    private javax.swing.JTable tblSales;
    // End of variables declaration//GEN-END:variables
}
