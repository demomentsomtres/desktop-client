package agrotech.view.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

// Panell personalitzat que dibuixa el fons decoratiu del login
public class WaveBackgroundPanel extends JPanel {

    private final ImageIcon waveIcon;

    public WaveBackgroundPanel() {
        waveIcon = new ImageIcon(getClass().getResource("/agrotech/resources/wave_bg.png"));
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        // Fons general del login
        g2.setColor(new Color(245, 247, 248));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Dibuixar les onades a baix a l'esquerra
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
