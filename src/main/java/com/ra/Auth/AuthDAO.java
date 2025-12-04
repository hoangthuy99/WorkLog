package com.ra.Auth;

import com.ra.DAO.Auth.IAuthDAO;
import com.ra.Model.Entity.Roles;
import com.ra.Model.Entity.Users;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class AuthDAO implements IAuthDAO {
    @Override
    public Users findByUsername(String username) {
        //tìm kiếm người dùng theo username trong CSDL
       try (Session session = HibernateUtil.getSessionFactory().openSession()) {
           String hql = "FROM Users U WHERE U.userName = :username";
           return (Users) session.createQuery(hql).setParameter("username", username).uniqueResult();
       }catch (Exception e){
              e.printStackTrace();
              return null;
       }
    }

    @Override
    public List<Roles> findAllRoles() {
        //TODO: Lấy tất cả roles từ bảng role
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Roles", Roles.class).list(); // ✔ Không dùng parameter

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Optional<Roles> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Roles role = session
                    .createQuery("FROM Roles WHERE name = :name", Roles.class)
                    .setParameter("name", name)
                    .uniqueResult();

            return Optional.ofNullable(role);
        }
        catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
