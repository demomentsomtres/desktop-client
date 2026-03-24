package agrotech.main;

import agrotech.view.LoginView;
import javax.swing.SwingUtilities;

/**
 * Punt d'entrada del client escriptori AgroTech.
 *
 * <p>
 * Inicia l'aplicació obrint la finestra de login dins del fil d'interfície
 * gràfica de Swing.</p>
 *
 * @author Javier Santana Coronado
 */
public class MainApp {

    /**
     * Inicia l'aplicació i mostra la finestra de login.
     *
     * @param args arguments de línia d'ordres
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }

}
