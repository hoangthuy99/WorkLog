package com.ra.View.button;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

// Kế thừa JButton để sử dụng nó làm Component vẽ
public class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer(String text) {
        setText(text); // Thiết lập văn bản cho nút ("編集" hoặc "削除")
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        // Đặt màu nền tùy theo trạng thái được chọn (isSelected)
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            // Đặt màu nền cho nút (tùy chọn: có thể để mặc định hoặc tùy chỉnh)
            setBackground(javax.swing.UIManager.getColor("Button.background"));
        }

        // Trả về chính component này (JButton) để nó được vẽ trong ô
        return this;
    }
}

