package view;

import model.Book;

import javax.swing.*;
import java.awt.*;

public class BookComboBoxRenderer extends JLabel implements ListCellRenderer<Book> {
    public BookComboBoxRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Book> list, Book book, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setText(book != null ? book.toString() : "");
        return this;
    }
}
