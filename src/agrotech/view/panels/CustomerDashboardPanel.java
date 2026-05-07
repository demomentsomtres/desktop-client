package agrotech.view.panels;

import agrotech.client.ClientResponse;
import agrotech.model.ManagedProduct;
import agrotech.service.ProductsService;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;

/**
 *
 * @author Javier Santana Coronado
 */
public class CustomerDashboardPanel extends javax.swing.JPanel {

    private final ProductsService productsService;
    private DefaultTableModel offersTableModel;

    /**
     * Crea el panell de dashboard del client.
     */
    public CustomerDashboardPanel() {
        initComponents();
        productsService = new ProductsService();
        initDashboard();
        loadOffers();
    }

    /**
     * Inicialitza la configuració bàsica del dashboard.
     */
    private void initDashboard() {
        offersTableModel = (DefaultTableModel) tblOffers.getModel();
        offersTableModel.setRowCount(0);
        tblOffers.setRowHeight(24);
        tblOffers.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * Carrega els productes amb oferta automàtica per caducitat propera.
     */
    private void loadOffers() {
        ClientResponse<List<ManagedProduct>> response
                = productsService.getProductsWithExpirationOffer();

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

        fillOffersTable(products);
        updateSummary(products);
    }

    /**
     * Omple la taula amb els productes amb oferta automàtica.
     *
     * @param products llista de productes amb caducitat propera
     */
    private void fillOffersTable(List<ManagedProduct> products) {
        offersTableModel.setRowCount(0);

        for (ManagedProduct product : products) {
            offersTableModel.addRow(new Object[]{
                safeText(product.getProductCategory()),
                formatMoney(product.getPrice()),
                safeText(product.getStock()),
                safeText(product.getExpirationDate())
            });
        }
    }

    /**
     * Actualitza les targetes de resum del dashboard.
     *
     * @param products llista de productes amb oferta automàtica
     */
    private void updateSummary(List<ManagedProduct> products) {
        int offersCount = products.size();

        lblOffersValue.setText(String.valueOf(offersCount));
        lblUrgentProductsValue.setText(String.valueOf(offersCount));
        lblUrgentOfferValue.setText(getUrgentOfferText(products));
    }

    /**
     * Retorna el text de l'oferta més urgent.
     *
     * <p>
     * Si només hi ha un producte amb la data de caducitat més propera, mostra
     * el nom del producte. Si n'hi ha més d'un amb la mateixa data, mostra el
     * nombre de productes urgents.
     * </p>
     *
     * @param products llista de productes amb oferta
     * @return text de l'oferta urgent o guió si no n'hi ha
     */
    private String getUrgentOfferText(List<ManagedProduct> products) {
        LocalDate nearestExpirationDate = findNearestExpirationDate(products);

        if (nearestExpirationDate == null) {
            return "—";
        }

        int urgentProductsCount = countProductsByExpirationDate(
                products,
                nearestExpirationDate
        );

        if (urgentProductsCount > 1) {
            return urgentProductsCount + " productes";
        }

        ManagedProduct urgentProduct = findProductByExpirationDate(
                products,
                nearestExpirationDate
        );

        if (urgentProduct == null) {
            return "—";
        }

        return safeText(urgentProduct.getProductCategory());
    }

    /**
     * Cerca la data de caducitat més propera entre els productes amb oferta.
     *
     * @param products llista de productes amb oferta
     * @return data de caducitat més propera o null si no es pot determinar
     */
    private LocalDate findNearestExpirationDate(List<ManagedProduct> products) {
        LocalDate nearestExpirationDate = null;

        for (ManagedProduct product : products) {
            LocalDate expirationDate = parseExpirationDate(product);

            if (expirationDate != null
                    && (nearestExpirationDate == null
                    || expirationDate.isBefore(nearestExpirationDate))) {
                nearestExpirationDate = expirationDate;
            }
        }

        return nearestExpirationDate;
    }

    /**
     * Compta quants productes tenen una data de caducitat concreta.
     *
     * @param products llista de productes amb oferta
     * @param expirationDate data de caducitat que es vol comptar
     * @return nombre de productes amb aquesta data
     */
    private int countProductsByExpirationDate(
            List<ManagedProduct> products,
            LocalDate expirationDate
    ) {
        int count = 0;

        for (ManagedProduct product : products) {
            LocalDate productExpirationDate = parseExpirationDate(product);

            if (expirationDate.equals(productExpirationDate)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Cerca el primer producte que té una data de caducitat concreta.
     *
     * @param products llista de productes amb oferta
     * @param expirationDate data de caducitat cercada
     * @return producte trobat o null si no n'hi ha cap
     */
    private ManagedProduct findProductByExpirationDate(
            List<ManagedProduct> products,
            LocalDate expirationDate
    ) {
        for (ManagedProduct product : products) {
            LocalDate productExpirationDate = parseExpirationDate(product);

            if (expirationDate.equals(productExpirationDate)) {
                return product;
            }
        }

        return null;
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
     * Neteja les dades visibles del dashboard.
     */
    private void clearDashboard() {
        if (offersTableModel != null) {
            offersTableModel.setRowCount(0);
        }

        lblOffersValue.setText("0");
        lblUrgentProductsValue.setText("0");
        lblUrgentOfferValue.setText("—");
    }

    /**
     * Dona format monetari a un import.
     *
     * @param amount import del producte
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
        pnlCardOffers = new javax.swing.JPanel();
        lblOffersIcon = new javax.swing.JLabel();
        lblOffersTitle = new javax.swing.JLabel();
        lblOffersValue = new javax.swing.JLabel();
        pnlCardUrgentProducts = new javax.swing.JPanel();
        lblUrgentProductsIcon = new javax.swing.JLabel();
        lblUrgentProductsTitle = new javax.swing.JLabel();
        lblUrgentProductsValue = new javax.swing.JLabel();
        pnlCardUrgentOffer = new javax.swing.JPanel();
        lblUrgentOfferIcon = new javax.swing.JLabel();
        lblUrgentOfferTitle = new javax.swing.JLabel();
        lblUrgentOfferValue = new javax.swing.JLabel();
        pnlOffersTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOffers = new javax.swing.JTable();

        setBackground(new java.awt.Color(245, 247, 250));

        pnlHeader.setBackground(new java.awt.Color(255, 255, 255));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(0, 102, 170));
        lblTitle.setText("Ofertes disponibles");

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

        pnlCardOffers.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardOffers.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        lblOffersIcon.setFont(new java.awt.Font("Segoe UI Emoji", 0, 28)); // NOI18N
        lblOffersIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOffersIcon.setText("🏷");

        lblOffersTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblOffersTitle.setText("Ofertes");

        lblOffersValue.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblOffersValue.setForeground(new java.awt.Color(0, 102, 170));
        lblOffersValue.setText("—");

        javax.swing.GroupLayout pnlCardOffersLayout = new javax.swing.GroupLayout(pnlCardOffers);
        pnlCardOffers.setLayout(pnlCardOffersLayout);
        pnlCardOffersLayout.setHorizontalGroup(
            pnlCardOffersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardOffersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblOffersIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCardOffersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOffersTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(lblOffersValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCardOffersLayout.setVerticalGroup(
            pnlCardOffersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardOffersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardOffersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblOffersIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCardOffersLayout.createSequentialGroup()
                        .addComponent(lblOffersTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOffersValue, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCardUrgentProducts.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardUrgentProducts.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        lblUrgentProductsIcon.setFont(new java.awt.Font("Segoe UI Emoji", 0, 28)); // NOI18N
        lblUrgentProductsIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUrgentProductsIcon.setText("📅");

        lblUrgentProductsTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblUrgentProductsTitle.setText("Productes urgents");

        lblUrgentProductsValue.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblUrgentProductsValue.setForeground(new java.awt.Color(0, 102, 170));
        lblUrgentProductsValue.setText("—");

        javax.swing.GroupLayout pnlCardUrgentProductsLayout = new javax.swing.GroupLayout(pnlCardUrgentProducts);
        pnlCardUrgentProducts.setLayout(pnlCardUrgentProductsLayout);
        pnlCardUrgentProductsLayout.setHorizontalGroup(
            pnlCardUrgentProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardUrgentProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUrgentProductsIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCardUrgentProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUrgentProductsTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(lblUrgentProductsValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCardUrgentProductsLayout.setVerticalGroup(
            pnlCardUrgentProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardUrgentProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardUrgentProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblUrgentProductsIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCardUrgentProductsLayout.createSequentialGroup()
                        .addComponent(lblUrgentProductsTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUrgentProductsValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCardUrgentOffer.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardUrgentOffer.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        lblUrgentOfferIcon.setFont(new java.awt.Font("Segoe UI Emoji", 0, 28)); // NOI18N
        lblUrgentOfferIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUrgentOfferIcon.setText("!");

        lblUrgentOfferTitle.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblUrgentOfferTitle.setText("Oferta urgent");

        lblUrgentOfferValue.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblUrgentOfferValue.setForeground(new java.awt.Color(46, 125, 50));
        lblUrgentOfferValue.setText("—");

        javax.swing.GroupLayout pnlCardUrgentOfferLayout = new javax.swing.GroupLayout(pnlCardUrgentOffer);
        pnlCardUrgentOffer.setLayout(pnlCardUrgentOfferLayout);
        pnlCardUrgentOfferLayout.setHorizontalGroup(
            pnlCardUrgentOfferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCardUrgentOfferLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUrgentOfferIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlCardUrgentOfferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUrgentOfferTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(lblUrgentOfferValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCardUrgentOfferLayout.setVerticalGroup(
            pnlCardUrgentOfferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCardUrgentOfferLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCardUrgentOfferLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblUrgentOfferIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlCardUrgentOfferLayout.createSequentialGroup()
                        .addComponent(lblUrgentOfferTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUrgentOfferValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlSummaryLayout = new javax.swing.GroupLayout(pnlSummary);
        pnlSummary.setLayout(pnlSummaryLayout);
        pnlSummaryLayout.setHorizontalGroup(
            pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlCardOffers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlCardUrgentProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlCardUrgentOffer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlSummaryLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {pnlCardOffers, pnlCardUrgentOffer, pnlCardUrgentProducts});

        pnlSummaryLayout.setVerticalGroup(
            pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlCardUrgentOffer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCardOffers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCardUrgentProducts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlOffersTable.setBackground(new java.awt.Color(255, 255, 255));
        pnlOffersTable.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Productes amb oferta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 102, 170))); // NOI18N

        tblOffers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Producte", "Preu", "Estoc", "Caducitat"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblOffers);

        javax.swing.GroupLayout pnlOffersTableLayout = new javax.swing.GroupLayout(pnlOffersTable);
        pnlOffersTable.setLayout(pnlOffersTableLayout);
        pnlOffersTableLayout.setHorizontalGroup(
            pnlOffersTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOffersTableLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        pnlOffersTableLayout.setVerticalGroup(
            pnlOffersTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOffersTableLayout.createSequentialGroup()
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
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlOffersTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlSummary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlOffersTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Actualitza manualment les ofertes del dashboard.
     *
     * @param evt esdeveniment del botó
     */
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadOffers();
    }//GEN-LAST:event_btnRefreshActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblOffersIcon;
    private javax.swing.JLabel lblOffersTitle;
    private javax.swing.JLabel lblOffersValue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUrgentOfferIcon;
    private javax.swing.JLabel lblUrgentOfferTitle;
    private javax.swing.JLabel lblUrgentOfferValue;
    private javax.swing.JLabel lblUrgentProductsIcon;
    private javax.swing.JLabel lblUrgentProductsTitle;
    private javax.swing.JLabel lblUrgentProductsValue;
    private javax.swing.JPanel pnlCardOffers;
    private javax.swing.JPanel pnlCardUrgentOffer;
    private javax.swing.JPanel pnlCardUrgentProducts;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlOffersTable;
    private javax.swing.JPanel pnlSummary;
    private javax.swing.JTable tblOffers;
    // End of variables declaration//GEN-END:variables
}
