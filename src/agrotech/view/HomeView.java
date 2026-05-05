package agrotech.view;

import agrotech.service.AuthService;
import agrotech.model.User;
import agrotech.session.SessionManager;
import javax.swing.JOptionPane;

import javax.swing.ImageIcon;
import java.awt.Image;

import agrotech.model.Role;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import agrotech.view.panels.UsersPanel;
import agrotech.view.panels.ProductsPanel;
import agrotech.view.panels.SuppliersPanel;
import agrotech.view.panels.OrdersPanel;
import agrotech.view.panels.ProfilePanel;

/**
 * Pantalla principal provisional del client escriptori.
 *
 * <p>
 * Mostra la informació bàsica de la sessió activa i permet tancar-la mitjançant
 * l'opció de logout.</p>
 *
 * @author Javier Santana Coronado
 */
public class HomeView extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(HomeView.class.getName());
    private final AuthService authService = new AuthService();

    /**
     * Crea la pantalla principal i inicialitza la interfície.
     */
    public HomeView() {
        initComponents();
        loadLogo();
        loadUserData();
        initContentPanel();
        configureViewByRole();
        showDefaultContent();
        setLocationRelativeTo(null);
    }

    /**
     * Carrega el logotip de l'aplicació i l'ajusta a la mida de la vista.
     */
    private void loadLogo() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/agrotech/resources/logo.png"));
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(scaledImage));
        lblLogo.setText("");
    }

    /**
     * Carrega les dades de l'usuari autenticat a la interfície.
     */
    private void loadUserData() {
        User currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {
            lblWelcome.setText("Benvingut/da, " + currentUser.getUsername());
            lblRole.setText("Rol: " + currentUser.getRole());
        } else {
            lblWelcome.setText("Benvingut/da");
            lblRole.setText("Rol: ---");
        }
    }

    /**
     * Prepara el panell central per carregar mòduls.
     */
    private void initContentPanel() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
    }

    /**
     * Configura la vista segons el rol de l'usuari autenticat.
     */
    private void configureViewByRole() {
        User currentUser = SessionManager.getCurrentUser();

        if (currentUser == null) {
            btnUsers.setVisible(false);
            btnSuppliers.setVisible(false);
            btnProducts.setVisible(false);
            btnOrders.setVisible(false);
            return;
        }

        Role role = currentUser.getRole();

        configureProductsButtonText(role);

        btnUsers.setVisible(
                role == Role.ADMIN);

        btnSuppliers.setVisible(
                role == Role.ADMIN);

        btnProducts.setVisible(
                role == Role.ADMIN
                || role == Role.SUPPLIER
                || role == Role.CUSTOMER
        );

        btnOrders.setVisible(
                role == Role.ADMIN
                || role == Role.CUSTOMER
        );

        btnOrders.setVisible(
                role == Role.ADMIN
                || role == Role.CUSTOMER);
    }

    /**
     * Configura el text del botó de productes segons el rol de l'usuari.
     *
     * @param role rol de l'usuari autenticat
     */
    private void configureProductsButtonText(Role role) {
        if (role == Role.SUPPLIER) {
            btnProducts.setText("Els meus productes");
        } else if (role == Role.CUSTOMER) {
            btnProducts.setText("Productes disponibles");
        } else {
            btnProducts.setText("Gestió de productes");
        }
    }

    /**
     * Mostra el contingut inicial de la pantalla principal.
     */
    private void showDefaultContent() {
        showPlaceholderPanel("Pantalla principal provisional");
    }

    /**
     * Mostra un panell temporal amb un missatge informatiu.
     *
     * @param message text que es mostrarà al centre del panell
     */
    private void showPlaceholderPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 248));

        JLabel label = new JLabel(message, javax.swing.SwingConstants.CENTER);
        label.setFont(new java.awt.Font("Segoe UI", 0, 16));
        label.setForeground(new Color(150, 150, 150));

        panel.add(label, BorderLayout.CENTER);

        showPanel(panel);
    }

    /**
     * Carrega un panell dins de la zona central de contingut.
     *
     * @param panel panell que es vol mostrar
     */
    private void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblWelcome = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        btnUsers = new javax.swing.JButton();
        btnProducts = new javax.swing.JButton();
        btnSuppliers = new javax.swing.JButton();
        btnOrders = new javax.swing.JButton();
        btnProfile = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();
        lblContentPlaceholder = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AgroTech - Home");
        setBackground(new java.awt.Color(245, 247, 248));
        setResizable(false);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setText("LOGO");
        lblLogo.setMaximumSize(new java.awt.Dimension(80, 80));
        lblLogo.setMinimumSize(new java.awt.Dimension(80, 80));
        lblLogo.setPreferredSize(new java.awt.Dimension(80, 80));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(30, 111, 168));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("AgroTech");

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(26, 78, 134));
        lblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWelcome.setText("Benvingut/da");

        lblRole.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblRole.setForeground(new java.awt.Color(90, 90, 90));
        lblRole.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRole.setText("Rol: ---");

        btnUsers.setText("Gestió d'usuaris");
        btnUsers.setPreferredSize(new java.awt.Dimension(140, 30));
        btnUsers.addActionListener(this::btnUsersActionPerformed);

        btnProducts.setText("Gestió de productes");
        btnProducts.setPreferredSize(new java.awt.Dimension(140, 30));
        btnProducts.addActionListener(this::btnProductsActionPerformed);

        btnSuppliers.setText("Gestió de proveïdors");
        btnSuppliers.setPreferredSize(new java.awt.Dimension(140, 30));
        btnSuppliers.addActionListener(this::btnSuppliersActionPerformed);

        btnOrders.setText("Comandes");
        btnOrders.setPreferredSize(new java.awt.Dimension(140, 30));
        btnOrders.addActionListener(this::btnOrdersActionPerformed);

        btnProfile.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnProfile.setForeground(new java.awt.Color(30, 111, 168));
        btnProfile.setText("El meu perfil");
        btnProfile.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(30, 111, 168)));
        btnProfile.setPreferredSize(new java.awt.Dimension(117, 30));
        btnProfile.addActionListener(this::btnProfileActionPerformed);

        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(30, 111, 168));
        btnLogout.setText("Tanca sessió");
        btnLogout.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(30, 111, 168)));
        btnLogout.setPreferredSize(new java.awt.Dimension(117, 30));
        btnLogout.addActionListener(this::btnLogoutActionPerformed);

        contentPanel.setBackground(new java.awt.Color(245, 247, 248));
        contentPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        lblContentPlaceholder.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblContentPlaceholder.setForeground(new java.awt.Color(150, 150, 150));
        lblContentPlaceholder.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblContentPlaceholder.setText("Pantalla principal provisional");
        lblContentPlaceholder.setPreferredSize(new java.awt.Dimension(1110, 430));

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblContentPlaceholder, javax.swing.GroupLayout.DEFAULT_SIZE, 1053, Short.MAX_VALUE)
                .addContainerGap())
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblContentPlaceholder, javax.swing.GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contentPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(btnUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOrders, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblWelcome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblRole, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTitle)
                .addGap(10, 10, 10)
                .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRole, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnUsers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnOrders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Gestiona l'acció del botó de logout.
     *
     * <p>
     * Si el procés finalitza correctament, torna a la finestra de login. Si
     * falla, mostra un missatge d'error i manté l'usuari a la mateixa
     * vista.</p>
     *
     * @param evt esdeveniment del botó
     */
    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        boolean logoutOk = authService.logout();

        if (logoutOk) {
            LoginView loginView = new LoginView();
            loginView.setLocationRelativeTo(null);
            loginView.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No s'ha pogut tancar la sessió al servidor.",
                    "Error de logout",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    /**
     * Mostra el mòdul d'usuaris.
     *
     * @param evt esdeveniment del botó
     */
    private void btnUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsersActionPerformed
        showPanel(new UsersPanel());
    }//GEN-LAST:event_btnUsersActionPerformed

    private void btnProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductsActionPerformed
        showPanel(new ProductsPanel());
    }//GEN-LAST:event_btnProductsActionPerformed

    /**
     * Mostra el mòdul de proveïdors.
     *
     * @param evt esdeveniment del botó
     */
    private void btnSuppliersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuppliersActionPerformed
        showPanel(new SuppliersPanel());
    }//GEN-LAST:event_btnSuppliersActionPerformed

    /**
     * Mostra el mòdul de comandes.
     *
     * @param evt esdeveniment del botó
     */
    private void btnOrdersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdersActionPerformed
        showPanel(new OrdersPanel());
    }//GEN-LAST:event_btnOrdersActionPerformed

    private void btnProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfileActionPerformed
        showPanel(new ProfilePanel());
    }//GEN-LAST:event_btnProfileActionPerformed

    /**
     * Mètode auxiliar per executar la pantalla principal de manera independent.
     *
     * @param args arguments de línia d'ordres
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new HomeView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnOrders;
    private javax.swing.JButton btnProducts;
    private javax.swing.JButton btnProfile;
    private javax.swing.JButton btnSuppliers;
    private javax.swing.JButton btnUsers;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel lblContentPlaceholder;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
}
