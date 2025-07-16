package view;

import java.awt.*;
import javax.swing.*;

public class KasirView extends JPanel {
    private JLabel labelNotaId;
    private JTextField textFieldNotaId;
    private JButton buttonCari;
    private JTextArea textAreaOrder;
    private JButton buttonBayar;

    public KasirView() {
        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        labelNotaId = new JLabel("Masukkan ID Nota:");
        textFieldNotaId = new JTextField(15);
        buttonCari = new JButton("Cari Order");
        textAreaOrder = new JTextArea(10, 40);
        textAreaOrder.setEditable(false);
        buttonBayar = new JButton("Bayar");
        buttonBayar.setEnabled(false);
    }

    private void layoutComponents() {
        this.setLayout(new BorderLayout(10, 10));
        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(labelNotaId);
        inputPanel.add(textFieldNotaId);
        inputPanel.add(buttonCari);

        JScrollPane scrollPane = new JScrollPane(textAreaOrder);

        this.add(inputPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonBayar, BorderLayout.SOUTH);
    }

    public JTextField getTextFieldNotaId() {
        return textFieldNotaId;
    }

    public JButton getButtonCari() {
        return buttonCari;
    }

    public JTextArea getTextAreaOrder() {
        return textAreaOrder;
    }

    public JButton getButtonBayar() {
        return buttonBayar;
    }
}
