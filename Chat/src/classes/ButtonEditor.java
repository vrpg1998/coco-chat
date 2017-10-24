/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import chat.Principal;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author VRPG1
 */
public class ButtonEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private boolean isPushed;
    DataBaseConnection con;
    
    public ButtonEditor(JCheckBox checkBox, Principal parent) {
        super(checkBox);
        con = new DataBaseConnection();
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener((ActionEvent e) -> {
            String[] buttonData = button.getText().split(",");
            String actionString = buttonData[0];
            String requestString = buttonData[1];
            int response;
            if("Aceptar".equals(actionString)) {
                response = 1;
                JOptionPane.showMessageDialog(null, "Amigo agregado");
            } else {
                response = -1;
                JOptionPane.showMessageDialog(null, "Amigo rechazado");
            }
            con.acceptFriend(Integer.parseInt(requestString), response);
            parent.loadFriendRequests();
            parent.loadFriendRequestsTable();
            parent.setTabFristIndex();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }
}