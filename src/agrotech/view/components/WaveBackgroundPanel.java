package agrotech.view.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panell personalitzat que dibuixa el fons decoratiu del login.
 *
 * <p>
 * Pinta un color de fons uniforme i, a sobre, una imatge amb forma d'onades
 * situada a la part inferior esquerra.</p>
 *
 * @author Javier Santana Coronado
 */
public class WaveBackgroundPanel extends JPanel {

    private final ImageIcon waveIcon;

    /**
     * Crea el panell de fons i carrega la imatge decorativa.
     */
    public WaveBackgroundPanel() {
        waveIcon = new ImageIcon(getClass().getResource("/agrotech/resources/wave_bg.png"));
        setOpaque(true);
    }

    /**
     * Pinta el fons del panell i la imatge decorativa d'onades.
     *
     * @param g context gràfic del component
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(new Color(245, 247, 248));
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (waveIcon != null) {
            int imgW = (int) (getWidth() * 1.10);
            int imgH = imgW * waveIcon.getIconHeight() / waveIcon.getIconWidth();

            int x = -80;
            int y = getHeight() - imgH + 60;

            g2.drawImage(waveIcon.getImage(), x, y, imgW, imgH, this);
        }

        g2.dispose();
    }
}
