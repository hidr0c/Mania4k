import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    private Game game;

    public MainMenu(JFrame parentFrame) {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(800, 600));

        // Create a panel for the buttons with a BoxLayout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // Add some spacing at the top
        buttonPanel.add(Box.createVerticalStrut(150));

        // Create buttons
        JButton playButton = new JButton("Play");
        JButton optionsButton = new JButton("Options");
        JButton exitButton = new JButton("Exit");

        // Style buttons
        styleButton(playButton);
        styleButton(optionsButton);
        styleButton(exitButton);

        // Add action listeners
        playButton.addActionListener(e -> {
            game = new Game();
            parentFrame.getContentPane().removeAll();
            parentFrame.add(game);
            parentFrame.pack();
            parentFrame.revalidate();
            parentFrame.repaint();

            // Start the game loop in a separate thread
            new Thread(() -> game.gameLoop()).start();
        });

        optionsButton.addActionListener(e -> {
            // Options menu implementation
            JOptionPane.showMessageDialog(parentFrame, "Options menu coming soon!");
        });

        exitButton.addActionListener(e -> {
            System.exit(0);
        });

        // Center the buttons in the panel by adding invisible components
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add buttons to the panel with spacing
        buttonPanel.add(playButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(optionsButton);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(exitButton);

        // Add the button panel to the center of the main panel
        this.add(buttonPanel, BorderLayout.CENTER);

        // Add a title at the top
        JLabel titleLabel = new JLabel("Mania 4K", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        this.add(titleLabel, BorderLayout.NORTH);

        // Set background color
        this.setBackground(new Color(40, 40, 40));
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(80, 80, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Draw a gradient background
        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(25, 25, 50),
                0, getHeight(), new Color(10, 10, 20)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.dispose();
    }
}