package com.ra.Controller;


import com.ra.DAO.Auth.AuthDAO;
import com.ra.DAO.User.UserDAO;
import com.ra.Model.Entity.Users;

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
     * Hàm kiểm tra đăng nhập
     * @param username tên đăng nhập
     * @param password mật khẩu
     * @return Users nếu đăng nhập đúng, null nếu sai
     */
    public Users login(String username, String password) {
        if (username == null || username.trim().isEmpty()) return null;
        if (password == null || password.trim().isEmpty()) return null;

        // Gọi DAO để lấy user theo username
        Users user = authDAO.findByUsername(username);

        if (user == null) {
            System.out.println("User not found");
            return null; // Không có user
        }else{
            System.out.println("User found: " + user.getUserName());
            //TODO: Phân quyền ở đây cho dễ quản lý
            if(user.getRole() == null){
                System.out.println("User has no role assigned");
                return null; // Không có role
            }else {
                System.out.println("User role: " + user.getRole().getName());
            }
        }

        // Kiểm tra password (demo = password plaintext — nếu có hash thì đổi logic)
        if (!user.getPassword().equals(password)) {
            return null;
        }
        return user; // Đúng mật khẩu → trả về user
    }
    public Users createUser(Users user) {
        return userDAO.create(user);
    }

    public Users updateUser(Users user) {
        return userDAO.update(user);
    }

    public boolean deleteUser(int id) {
        return userDAO.deleteFindById(id);
    }

    public Optional<Users> findById(int id) {
        return userDAO.findById(id);
    }

    public List<Users> findAll(String keyword, int page, int size) {
        return userDAO.findAll(keyword, page, size);
    }

}
