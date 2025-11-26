package com.ra.DAO.Task;

import com.ra.Model.Entity.Tasks;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class TaskDAO implements ITaskDAO{
    @Override
    public void create(Tasks task) {
        //TODO: Tạo nội dung công việc vào DB
        Transaction transaction = null;
        // try-with-resources để tự động đóng session
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            // Thêm task vào DB
            session.save(task);
            // Commit transaction
            transaction.commit();
            System.out.println("Task created successfully!");
        }catch (Exception e){
            if (transaction != null){
                transaction.rollback(); // rollback nếu lỗi
            }
            e.printStackTrace();
        }
    }

    @Override
    public void update(Tasks task) {

    }

    @Override
    public boolean deleteFindById(int id) {
        return false;
    }

    @Override
    public List<Tasks> search(String keyword) {
        return List.of();
    }

    @Override
    public Optional<Tasks> findFindById(int id) {
        return Optional.empty();
    }
}
