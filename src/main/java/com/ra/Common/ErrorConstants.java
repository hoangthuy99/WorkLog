package com.ra.Common;

import java.awt.Color;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * Class chứa các hằng số liên quan đến cấu hình lỗi (Font, Color, Border, Messages)
 * để tái sử dụng trong các màn hình giao diện.
 */
public final class ErrorConstants {

    // Ngăn không cho tạo đối tượng của lớp tiện ích này
    private ErrorConstants() {}

    // --- CẤU HÌNH FONT ---
    public static final Font MAIN_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    public static final Font ERROR_FONT = new Font(Font.SANS_SERIF, Font.ITALIC, 12);
    public static final Font MESSAGE_FONT = new Font(Font.SANS_SERIF, Font.ITALIC, 13);


    // --- CẤU HÌNH MÀU ---
    public static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    public static final Color TEXT_COLOR = new Color(44, 62, 80);
    public static final Color BORDER_COLOR = new Color(200, 200, 200); // Màu viền mặc định
    public static final Color ERROR_COLOR = new Color(231, 76, 60); // Đỏ tươi

    // --- CẤU HÌNH VIỀN LỖI ---
    public static final Border DEFAULT_BORDER = new LineBorder(BORDER_COLOR);
    public static final Border ERROR_BORDER = new LineBorder(ERROR_COLOR, 2);

    // --- CÁC ERROR MESSAGE ĐƯỢC DÙNG TRONG MÀN LoginScreen ---
    public static final String ERROR_MESSAGE_REQUIRED_USERNAME = "ユーザーIDを入力してください!";
    public static final String ERROR_MESSAGE_REQUIRED_PASSWORD = "パスワードを入力してください!";
    public static final String ERROR_MESSAGE_INPUT_MISSING = "全部の 情報を入力してください!!!";
    public static final String ERROR_MESSAGE_AUTH_FAILED = "ユーザーIDまたはパスワードが正しくありません!";
    public static final String ERROR_MESSAGE_LOGIN_FAILED = "ログインに失敗しました!";

    // --- CÁC ERROR MESSAGE ĐƯỢC DÙNG TRONG MÀN CreateNewUser ---

    // Thông báo chung khi thiếu một hoặc nhiều trường bắt buộc
    public static final String ERROR_MESSAGE_CREATE_USER_MISSING_FIELDS = "ユーザー作成に必要な情報を入力してください!";

    // Thông báo lỗi cụ thể cho từng trường
    public static final String ERROR_MESSAGE_REQUIRED_STAFF_ID = "社員IDを入力してください!";
    public static final String ERROR_MESSAGE_REQUIRED_USER_ID = "User IDを入力してください!";
    public static final String ERROR_MESSAGE_REQUIRED_USER_PASSWORD = "Passwordを入力してください!";
    public static final String ERROR_MESSAGE_REQUIRED_PROJECT_ID = "Project IDを選択/入力してください!";
    public static final String ERROR_MESSAGE_REQUIRED_ROLE = "ロールを選択してください!";

    // Thông báo liên quan đến logic tra cứu 社員ID
    public static final String ERROR_MESSAGE_STAFF_ID_NOT_FOUND = "該当社員IDの情報が見つかりません!";
    public static final String ERROR_MESSAGE_STAFF_ID_FORMAT_INVALID = "社員IDの形式が正しくありません!";
}


