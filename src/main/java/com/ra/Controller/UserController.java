package com.ra.Controller;

import com.ra.DAO.Auth.AuthDAO;
import com.ra.DAO.User.UserDAO;
import com.ra.Model.Entity.Users;
import com.ra.Sercurity.PasswordHash;

import java.util.List;
import java.util.Optional;

public class UserController {

    private AuthDAO authDAO;
    private UserDAO userDAO;

    public UserController() {
        this.authDAO = new AuthDAO();
        this.userDAO = new UserDAO();
    }

    /**
     * Đăng nhập có kiểm tra mật khẩu đã HASH bằng BCrypt
     */
    public Users login(String username, String password) {

        if (username == null || username.trim().isEmpty()) return null;
        if (password == null || password.trim().isEmpty()) return null;

        // Lấy user từ DB
        Users user = authDAO.findByUsername(username);

        if (user == null) {
            System.out.println("User not found");
            return null;
        }

        System.out.println("User found: " + user.getUserName());

        // Kiểm tra role
        if (user.getRole() == null) {
            System.out.println("User has no role assigned");
            return null;
        }

        System.out.println("User role: " + user.getRole().getName());

        // Kiểm tra mật khẩu đã HASH
        boolean pwMatch = PasswordHash.verifyPassword(password, user.getPassword());

        if (!pwMatch) {
            System.out.println("Password mismatch");
            return null;
        }

        return user;
    }

    /**
     * Tạo user mới → HASH password trước khi lưu
     */
    public Users createUser(Users user) {

        if (user == null) {
            throw new IllegalArgumentException("ユーザー情報が不正です。");
        }

        // ✅ normalize username (trim + lower) để chặn cả khác hoa/thường
        String raw = user.getUserName();
        String normalized = (raw == null) ? "" : raw.trim().toLowerCase();
        user.setUserName(normalized);

        System.out.println("[DEBUG] createUser called. username=" + normalized);

        // ✅ chặn trùng username (đang active)
        Optional<Users> existed = userDAO.findByUsername(normalized);
        if (existed.isPresent()) {
            throw new IllegalArgumentException("ユーザー名は既に存在します。別のユーザー名を入力してください。");
        }

        // Hash password trước khi lưu
        String hashed = PasswordHash.hashPassword(user.getPassword());
        user.setPassword(hashed);

        try {
            return userDAO.create(user);
        } catch (org.hibernate.exception.ConstraintViolationException ex) {
            // ✅ phòng trường hợp DB vẫn báo unique (hoặc lỗi khác) => vẫn hiện tiếng Nhật
            throw new IllegalArgumentException("ユーザー名は既に存在します。別のユーザー名を入力してください。");
        }
    }



    public Users updateUser(Users user) {

        if (user == null) throw new IllegalArgumentException("ユーザー情報が不正です。");

        // normalize
        String normalized = user.getUserName() == null ? "" : user.getUserName().trim().toLowerCase();
        user.setUserName(normalized);

        // check trùng nhưng phải loại trừ chính nó
        Optional<Users> existed = userDAO.findByUsername(normalized);
        if (existed.isPresent() && existed.get().getId() != user.getId()) {
            throw new IllegalArgumentException("ユーザー名は既に存在します。別のユーザー名を入力してください。");
        }

        if (user.getPassword() != null && user.getPassword().length() < 60) {
            String hashed = PasswordHash.hashPassword(user.getPassword());
            user.setPassword(hashed);
        }

        return userDAO.update(user);
    }


    public boolean deleteUser(int id) {
        return userDAO.deleteFindById(id);
    }

    public Users findById(int id) {
        return userDAO.findById(id);
    }

    public List<Users> findAll(String keyword, int page, int size) {
        return userDAO.findAll(keyword, page, size);
    }

    public List<Users> findAll() {
        return userDAO.findAll();
    }

    public Optional<Users> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}
