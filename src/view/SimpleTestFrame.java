package view;

import javax.swing.*;

public class SimpleTestFrame extends JFrame {
    public SimpleTestFrame() {
        setTitle("Test Frame");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JLabel("Hello World", JLabel.CENTER));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimpleTestFrame().setVisible(true);
        });
    }
}
