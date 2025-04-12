import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class AssetTestPanel extends JPanel {
    private BufferedImage noteAsset;

    public AssetTestPanel() {
        try {
            noteAsset = ImageIO.read(new File("assets/skins/default/note.png"));
        } catch (IOException e) {
            System.out.println("Error loading asset: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Define the coordinates and size for the asset area (in a circular form)
        int assetX = 50, assetY = 50, assetDiameter = 100;
        Graphics2D g2d = (Graphics2D) g;

        if (noteAsset != null) {
            // Clip the drawing area to a circle where the asset will be drawn
            Shape oldClip = g2d.getClip();
            g2d.setClip(new Ellipse2D.Double(assetX, assetY, assetDiameter, assetDiameter));
            // Draw the image scaled to the circle's dimensions
            g2d.drawImage(noteAsset, assetX, assetY, assetDiameter, assetDiameter, null);
            // Restore the original clip
            g2d.setClip(oldClip);
        } else {
            // If asset is not found, draw a gray circle with a message
            g.setColor(Color.GRAY);
            g.fillOval(assetX, assetY, assetDiameter, assetDiameter);
            g.setColor(Color.BLACK);
            g.drawString("Asset not found", assetX + 10, assetY + assetDiameter / 2);
        }

        // Draw an independent red input circle for testing purposes
        g.setColor(Color.RED);
        // The red circle is drawn at (200, 50) with the same diameter
        g.fillOval(200, 50, assetDiameter, assetDiameter);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Asset Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.add(new AssetTestPanel());
        frame.setVisible(true);
    }
}