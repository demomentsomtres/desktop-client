package agrotech.main;

import agrotech.view.LoginView;
import javax.swing.SwingUtilities;

// Punt d'entrada de l'aplicació. Obre la finestra de login.
public class MainApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
    
}
