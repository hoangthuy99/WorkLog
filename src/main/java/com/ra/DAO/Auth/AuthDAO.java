package com.ra.DAO.Auth;

import com.ra.Model.Entity.Users;
import com.ra.Utils.HibernateUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;

public class AuthDAO implements IAuthDAO {
    @Override
    public Users findByUsername(String username) {
        //tìm kiếm người dùng theo username trong CSDL
       try (Session session = HibernateUtil.getSessionFactory().openSession()) {
           String hql = "FROM Users U WHERE U.username = :username";
           return (Users) session.createQuery(hql).setParameter("username", username).uniqueResult();
       }catch (Exception e){
              e.printStackTrace();
              return null;
       }
    }

}
