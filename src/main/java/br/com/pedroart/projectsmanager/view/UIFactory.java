package br.com.pedroart.projectsmanager.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UIFactory {

    // Cores e Fontes
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    public static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    public static final Color ACCENT_COLOR = new Color(26, 188, 156);
    public static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    public static final Color CARD_COLOR = Color.WHITE;
    public static final Color TEXT_COLOR = new Color(44, 62, 80);
    public static final Color DELETE_COLOR = new Color(231, 76, 60);
    public static final Color EDIT_COLOR = new Color(243, 156, 18);
    
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 12);

    
     //Cria um botão estilizado padrão para a aplicação.
    public static JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(BOLD_FONT);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
            }
        });
        return button;
    }

    
     //Configura o estilo padrão para as tabelas.
    public static void configureTableStyle(JTable table) {
        table.setFont(MAIN_FONT);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(BOLD_FONT);
        table.getTableHeader().setBackground(SECONDARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE); 
    }

    public static DefaultTableModel createNonEditableTableModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
    
    public static void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(BOLD_FONT);
        panel.add(labelComponent, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }
}