/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package agrotech.main;

import agrotech.view.LoginView;
import javax.swing.SwingUtilities;

/**
 *
 * @author jsant
 */
public class MainApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setLocationRelativeTo(null);
            loginView.setVisible(true);
        });
    }
    
}
