package agrotech.view.panels;

import agrotech.client.ClientResponse;
import agrotech.model.ManagedProduct;
import agrotech.service.ProductsService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Panell de resum principal per a l'usuari proveïdor.
 *
 * @author Javier Santana Coronado
 */
public class SupplierDashboardPanel extends javax.swing.JPanel {

    private final ProductsService productsService;
    private DefaultTableModel productsTableModel;

    /**
     * Crea el panell de dashboard del proveïdor.
     */
    public SupplierDashboardPanel() {
        initComponents();
        productsService = new ProductsService();
        initDashboard();
        loadProducts();
    }

    /**
     * Inicialitza la configuració bàsica del dashboard.
     */
    private void initDashboard() {
        productsTableModel = (DefaultTableModel) tblProducts.getModel();
        productsTableModel.setRowCount(0);
        tblProducts.setRowHeight(24);
        tblProducts.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * Carrega els productes del proveïdor autenticat.
     */
    private void loadProducts() {
        ClientResponse<List<ManagedProduct>> response
                = productsService.getMyProductsForDashboard();

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

        List<ManagedProduct> products = response.getData();

        if (products == null) {
            products = Collections.emptyList();
        }

        fillProductsTable(products);
        updateSummary(products);
    }

    /**
     * Omple la taula amb els productes del proveïdor.
     *
     * @param products llista de productes carregats
     */
    private void fillProductsTable(List<ManagedProduct> products) {
        productsTableModel.setRowCount(0);

        for (ManagedProduct product : products) {
            productsTableModel.addRow(new Object[]{
                safeText(product.getProductCategory()),
                formatMoney(product.getPrice()),
                safeText(product.getStock()),
                safeText(product.getExpirationDate()),
                getProductStatus(product)
            });
        }
    }

    /**
     * Actualitza les targetes de resum del dashboard.
     *
     * @param products llista de productes carregats
     */
    private void updateSummary(List<ManagedProduct> products) {
        lblProductsValue.setText(String.valueOf(products.size()));
        lblOutOfStockValue.setText(String.valueOf(countOutOfStockProducts(products)));
        lblExpiredProductsValue.setText(String.valueOf(countExpiredProducts(products)));
    }

    /**
     * Compta els productes sense estoc.
     *
     * @param products llista de productes carregats
     * @return nombre de productes sense estoc
     */
    private int countOutOfStockProducts(List<ManagedProduct> products) {
        int count = 0;

        for (ManagedProduct product : products) {
            if (product.getStock() != null && product.getStock() <= 0) {
                count++;
            }
        }

        return count;
    }

    /**
     * Compta els productes caducats.
     *
     * @param products llista de productes carregats
     * @return nombre de productes caducats
     */
    private int countExpiredProducts(List<ManagedProduct> products) {
        int count = 0;

        for (ManagedProduct product : products) {
            LocalDate expirationDate = parseExpirationDate(product);

            if (expirationDate != null && expirationDate.isBefore(LocalDate.now())) {
                count++;
            }
        }

        return count;
    }

    /**
     * Converteix la data de caducitat d'un producte a LocalDate.
     *
     * @param product producte consultat
     * @return data de caducitat o null si no és vàlida
     */
    private LocalDate parseExpirationDate(ManagedProduct product) {
        if (product == null || product.getExpirationDate() == null) {
            return null;
        }

        try {
            return LocalDate.parse(product.getExpirationDate());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retorna l'estat visible d'un producte.
     *
     * @param product producte consultat
     * @return estat del producte
     */
    private String getProductStatus(ManagedProduct product) {
        if (product.getStock() != null && product.getStock() <= 0) {
            return "Sense estoc";
        }

        LocalDate expirationDate = parseExpirationDate(product);

        if (expirationDate != null && expirationDate.isBefore(LocalDate.now())) {
            return "Caducat";
        }

        if (expirationDate != null
                && !expirationDate.isAfter(LocalDate.now().plusDays(3))) {
            return "Caducitat propera";
        }

        return "Disponible";
    }

    /**
     * Neteja les dades visibles del dashboard.
     */
    private void clearDashboard() {
        if (productsTableModel != null) {
            productsTableModel.setRowCount(0);
        }

        lblProductsValue.setText("0");
        lblOutOfStockValue.setText("0");
        lblExpiredProductsValue.setText("0");
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
        pnlCardProducts = new javax.swing.JPanel();
        lblProductsIcon = new javax.swing.JLabel();
        lblProductsTitle = new javax.swing.JLabel();
        lblProductsValue = new javax.swing.JLabel();
        pnlCardOutOfStock = new javax.swing.JPanel();
        lblOutOfStockIcon = new javax.swing.JLabel();
        lblOutOfStockTitle = new javax.swing.JLabel();
        lblOutOfStockValue = new javax.swing.JLabel();
        pnlCardExpiredProducts = new javax.swing.JPanel();
        lblExpiredProductsIcon = new javax.swing.JLabel();
        lblExpiredProductsTitle = new javax.swing.JLabel();
        lblExpiredProductsValue = new javax.swing.JLabel();
        pnlProductsTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducts = new javax.swing.JTable();

        setBackground(new java.awt.Color(245, 247, 250));

        pnlHeader.setBackground(new java.awt.Color(255, 255, 255));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(0, 102, 170));
        lblTitle.setText("Resum dels meus productes");

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

        pnlCardProducts.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardProducts.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        lblProductsIcon.setFont(new java.awt.Font("Segoe UI Emoji", 0, 28)); // NOI18N
        lblProductsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProductsIcon.setText("#");

        lblProductsTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblProductsTitle.setText("Productes");

        lblProductsValue.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblProductsValue.setForeground(new java.awt.Color(0, 102, 170));
        lblProductsValue.setText("—");

        javax.swing.GroupLayout pnlCardProductsLayout = new javax.swing.GroupLayout(pnlCardProducts);
        pnlCardProducts.setLayout(pnlCardProductsLayout);
        pnlCardProductsLayout.setHorizontalGroup(
            pnlCardProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblProductsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCardProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProductsTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(lblProductsValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCardProductsLayout.setVerticalGroup(
            pnlCardProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblProductsIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCardProductsLayout.createSequentialGroup()
                        .addComponent(lblProductsTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblProductsValue, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCardOutOfStock.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardOutOfStock.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        lblOutOfStockIcon.setFont(new java.awt.Font("Segoe UI Emoji", 0, 28)); // NOI18N
        lblOutOfStockIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOutOfStockIcon.setText("!");

        lblOutOfStockTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblOutOfStockTitle.setText("Sense estoc");

        lblOutOfStockValue.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblOutOfStockValue.setForeground(new java.awt.Color(0, 102, 170));
        lblOutOfStockValue.setText("—");

        javax.swing.GroupLayout pnlCardOutOfStockLayout = new javax.swing.GroupLayout(pnlCardOutOfStock);
        pnlCardOutOfStock.setLayout(pnlCardOutOfStockLayout);
        pnlCardOutOfStockLayout.setHorizontalGroup(
            pnlCardOutOfStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardOutOfStockLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblOutOfStockIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCardOutOfStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOutOfStockTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(lblOutOfStockValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCardOutOfStockLayout.setVerticalGroup(
            pnlCardOutOfStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardOutOfStockLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardOutOfStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblOutOfStockIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCardOutOfStockLayout.createSequentialGroup()
                        .addComponent(lblOutOfStockTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOutOfStockValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCardExpiredProducts.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardExpiredProducts.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        lblExpiredProductsIcon.setFont(new java.awt.Font("Segoe UI Emoji", 0, 28)); // NOI18N
        lblExpiredProductsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblExpiredProductsIcon.setText("!");

        lblExpiredProductsTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblExpiredProductsTitle.setText("Caducats");

        lblExpiredProductsValue.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblExpiredProductsValue.setForeground(new java.awt.Color(46, 125, 50));
        lblExpiredProductsValue.setText("—");

        javax.swing.GroupLayout pnlCardExpiredProductsLayout = new javax.swing.GroupLayout(pnlCardExpiredProducts);
        pnlCardExpiredProducts.setLayout(pnlCardExpiredProductsLayout);
        pnlCardExpiredProductsLayout.setHorizontalGroup(
            pnlCardExpiredProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardExpiredProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblExpiredProductsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCardExpiredProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblExpiredProductsTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(lblExpiredProductsValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCardExpiredProductsLayout.setVerticalGroup(
            pnlCardExpiredProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardExpiredProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardExpiredProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblExpiredProductsIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCardExpiredProductsLayout.createSequentialGroup()
                        .addComponent(lblExpiredProductsTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblExpiredProductsValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlSummaryLayout = new javax.swing.GroupLayout(pnlSummary);
        pnlSummary.setLayout(pnlSummaryLayout);
        pnlSummaryLayout.setHorizontalGroup(
            pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCardProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlCardOutOfStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlCardExpiredProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSummaryLayout.setVerticalGroup(
            pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlCardExpiredProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCardProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCardOutOfStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlProductsTable.setBackground(new java.awt.Color(255, 255, 255));
        pnlProductsTable.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Els meus productes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 102, 170))); // NOI18N

        tblProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Producte", "Preu", "Estoc", "Caducitat", "Estat"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblProducts);

        javax.swing.GroupLayout pnlProductsTableLayout = new javax.swing.GroupLayout(pnlProductsTable);
        pnlProductsTable.setLayout(pnlProductsTableLayout);
        pnlProductsTableLayout.setHorizontalGroup(
            pnlProductsTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductsTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        pnlProductsTableLayout.setVerticalGroup(
            pnlProductsTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlProductsTableLayout.createSequentialGroup()
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
                    .addComponent(pnlProductsTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(pnlProductsTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Actualitza manualment les dades del dashboard.
     *
     * @param evt esdeveniment del botó
     */
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadProducts();
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblExpiredProductsIcon;
    private javax.swing.JLabel lblExpiredProductsTitle;
    private javax.swing.JLabel lblExpiredProductsValue;
    private javax.swing.JLabel lblOutOfStockIcon;
    private javax.swing.JLabel lblOutOfStockTitle;
    private javax.swing.JLabel lblOutOfStockValue;
    private javax.swing.JLabel lblProductsIcon;
    private javax.swing.JLabel lblProductsTitle;
    private javax.swing.JLabel lblProductsValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlCardExpiredProducts;
    private javax.swing.JPanel pnlCardOutOfStock;
    private javax.swing.JPanel pnlCardProducts;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlProductsTable;
    private javax.swing.JPanel pnlSummary;
    private javax.swing.JTable tblProducts;
    // End of variables declaration//GEN-END:variables
}
