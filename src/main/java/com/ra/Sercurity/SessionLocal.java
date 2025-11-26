package com.ra.Sercurity;

import com.ra.Model.Entity.Users;

import java.util.*;
//　Lý do tạo class này là do muốn lưu trữ thông tin phiên làm việc của người dùng trong một ứng dụng đa luồng
// Trong Java, mỗi luồng có thể có trạng thái và dữ liệu riêng biệt. Sử dụng ThreadLocal cho phép mỗi luồng lưu trữ và truy xuất dữ liệu của riêng nó mà không bị ảnh hưởng bởi các luồng khác.
//　Ở　String Boot thì có thể sử dụng @SessionScope hoặc @RequestScope để quản lý trạng thái phiên làm việc của người dùng trong các ứng dụng web.Còn trong Java Swing thì không có sẵn các annotation như vậy để quản lý trạng thái phiên làm việc của người dùng.
public class SessionLocal {
    private static final ThreadLocal<Map<String, Object>> session =
            ThreadLocal.withInitial(HashMap::new);

    public static void set(String key, Object value) {
        session.get().put(key, value);
    }

    public static Object get(String key) {
        return session.get().get(key);
    }

    public static void remove(String key) {
        session.get().remove(key);
    }

    public static void clear() {
        session.get().clear();
    }

    public static void put(String currentUser, Users user) {
        session.get().put(currentUser, user);
    }
}
