/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.ra.View.holidays;

import com.ra.Controller.HolidayController;
import com.ra.DAO.Holiday.HolidayDAO;
import com.ra.Model.Entity.Holidays;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AllHoliday extends JPanel {

    private List<Holidays> holidayList;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public AllHoliday() {
        initComponents();

        HolidayController holidayController = new HolidayController(new HolidayDAO());
        holidayList = holidayController.findAll(); // Lấy tất cả holiday từ DB

        // Set ngày hôm nay
        jCalendar.setDate(new Date());

        // Highlight và tooltip các ngày nghỉ sau khi GUI render xong
        SwingUtilities.invokeLater(() -> addHolidayHighlightAndTooltip());

        // Khi đổi tháng/năm thì highlight lại
        jCalendar.getDayChooser().addPropertyChangeListener(evt -> {
            String prop = evt.getPropertyName();
            if ("month".equals(prop) || "year".equals(prop)) {
                SwingUtilities.invokeLater(() -> addHolidayHighlightAndTooltip());
            }
        });
    }

    // Lấy mảng JButton của các ngày
    private JButton[] getDayButtons() {
        try {
            JDayChooser dayChooser = jCalendar.getDayChooser();
            Field field = JDayChooser.class.getDeclaredField("day");
            field.setAccessible(true);
            return (JButton[]) field.get(dayChooser);
        } catch (Exception e) {
            e.printStackTrace();
            return new JButton[0];
        }
    }

    // Highlight + Tooltip cho các ngày nghỉ
    private void addHolidayHighlightAndTooltip() {
        JButton[] dayButtons = getDayButtons();
        if (dayButtons == null) return;

        Calendar cal = (Calendar) jCalendar.getCalendar().clone();

        for (JButton btn : dayButtons) {
            String text = btn.getText();
            if (text.isEmpty()) continue;

            int day = Integer.parseInt(text);
            cal.set(Calendar.DAY_OF_MONTH, day);
            String currentDate = sdf.format(cal.getTime());

            // Reset mặc định
            btn.setToolTipText(null);
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);

            // Highlight ngày nghỉ
            for (Holidays h : holidayList) {
                if (currentDate.equals(sdf.format(h.getDateHoliday()))) {
                    btn.setBackground(new Color(255, 153, 184)); // màu hồng
                    btn.setToolTipText("<html><b>" + h.getName() + "</b><br>" + h.getDescription() + "</html>");
                    break;
                }
            }

            // Highlight hôm nay
            String todayStr = sdf.format(new Date());
            if (currentDate.equals(todayStr)) {
                btn.setForeground(Color.RED);
                btn.setFont(btn.getFont().deriveFont(Font.BOLD));
            } else {
                btn.setFont(btn.getFont().deriveFont(Font.PLAIN));
            }
        }

        jCalendar.repaint();
    }

    // ---------- GUI Design (do NetBeans tạo) ----------
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel4 = new JLabel();
        jCalendar = new JCalendar();
        btnSearch = new JButton();

        jLabel4.setFont(new Font("Kannada MN", 1, 24)); // NOI18N
        jLabel4.setText("休日一覧");

        btnSearch.setBackground(new Color(102, 255, 255));
        btnSearch.setFont(new Font("Helvetica Neue", 1, 13)); // NOI18N
        btnSearch.setText("Search");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(249, 249, 249)
                                                .addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addComponent(jCalendar, GroupLayout.PREFERRED_SIZE, 649, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(14, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
                                .addGap(128, 128, 128))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jCalendar, GroupLayout.PREFERRED_SIZE, 313, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(30, Short.MAX_VALUE))
        );
    }

    private JButton btnSearch;
    private JCalendar jCalendar;
    private JLabel jLabel4;
<<<<<<< HEAD
}
=======
}
>>>>>>> a6f86596c1a64d3646c97c616c4b79b3c4a7e17c
