package com.ra.View;

import javax.swing.*;
import java.awt.*;

// Tạo class trung gian để quản lý navigation
public class NavigationManager {
    private static CardLayout cardLayout;
    private static JPanel contentPanel;

    public static void initialize(CardLayout layout, JPanel panel) {
        cardLayout = layout;
        contentPanel = panel;
    }

    public static void showScreen(String screenName) {
        if (cardLayout != null && contentPanel != null) {
            cardLayout.show(contentPanel, screenName);
        }
    }

    public static void showScreen(String screenName, Object data) {
        // Có thể mở rộng để truyền dữ liệu giữa các màn hình
        showScreen(screenName);
    }
}