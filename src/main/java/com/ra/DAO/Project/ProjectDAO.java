package com.ra.DAO.Project;

import com.ra.Model.Entity.Project;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProjectDAO implements IProjectDAO{
    @Override
    public void create(Project project) {
        //TODO: Tạo mới project vào DB
        Transaction tx = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            // Thêm project vào DB
            session.save(project);
            // Commit transaction
            tx.commit();
            System.out.println("Project created successfully!");
        }catch (Exception e) {
            if (tx != null) {
                tx.rollback(); // rollback nếu lỗi
            }
            e.printStackTrace();
        }
    }

    @Override
    public void update(Project project) {
    // TODO:Cập nhật dữ liệu project
        Transaction tx = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(project);
            tx.commit();
            System.out.println("Project updated successfully!");
        }catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteFindById(int id) {
        //Xoas project theo id
        Transaction tx = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Project project = session.get(Project.class, id);
            if (project != null) {
                session.delete(project);
            }
            tx.commit();
            System.out.println("Project deleted successfully!");
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<Project> findAll() {
        return List.of();
    }

    @Override
    public List<Project> search(String keyword, int page, int size) {
        return List.of();
    }

    @Override
    public Project findFindById(int id) {
        return null;
    }
}
