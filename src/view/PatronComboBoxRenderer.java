package view;

import model.Patron;

import javax.swing.*;
import java.awt.*;

public class PatronComboBoxRenderer extends JLabel implements ListCellRenderer<Patron> {
    public PatronComboBoxRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Patron> list, Patron patron, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setText(patron != null ? patron.toString() : "");
        return this;
    }
}
