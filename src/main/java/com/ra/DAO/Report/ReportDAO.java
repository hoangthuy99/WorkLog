package com.ra.DAO.Report;

import com.ra.DTO.report.ReportRowDTO;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class ReportDAO implements IReportDAO {

    @Override
    public List<ReportRowDTO> summaryByDept() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {

            String hql =
                    "SELECT new com.ra.DTO.report.ReportRowDTO(" +
                            "d.departmentCode, d.name, " +
                            "null, null, " +
                            "t.taskCode, t.name, " +
                            "SUM(w.workMinutes)) " +
                            "FROM WorkRecord w " +
                            "JOIN w.task t " +
                            "JOIN t.departments d " +
                            "GROUP BY d.departmentCode, d.name, t.taskCode, t.name";

            return s.createQuery(hql, ReportRowDTO.class).list();
        }
    }

    @Override
    public List<ReportRowDTO> summaryByProject() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {

            String hql =
                    "SELECT new com.ra.DTO.report.ReportRowDTO(" +
                            "null, null, " +
                            "p.projectCode, p.name, " +
                            "t.taskCode, t.name, " +
                            "SUM(w.workMinutes)) " +
                            "FROM WorkRecord w " +
                            "JOIN w.task t " +
                            "JOIN w.project p " +
                            "GROUP BY p.projectCode, p.name, t.taskCode, t.name";

            return s.createQuery(hql, ReportRowDTO.class).list();
        }
    }

    @Override
    public List<ReportRowDTO> summaryDeptProject() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {

            String hql =
                    "SELECT new com.ra.DTO.report.ReportRowDTO(" +
                            "d.departmentCode, d.name, " +
                            "p.projectCode, p.name, " +
                            "null, null, " +
                            "SUM(w.workMinutes)) " +
                            "FROM WorkRecord w " +
                            "JOIN w.project p " +
                            "JOIN p.departments d " +
                            "GROUP BY d.departmentCode, d.name, p.projectCode, p.name";

            return s.createQuery(hql, ReportRowDTO.class).list();
        }
    }
}
