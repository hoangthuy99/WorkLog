package com.ra.Controller;

import com.ra.DAO.Report.ReportDAO;
import com.ra.DAO.Report.IReportDAO;
import com.ra.DTO.report.ReportRowDTO;

import java.util.List;

public class ReportController {

    private final IReportDAO reportDAO = new ReportDAO();

    public List<ReportRowDTO> summaryByDept() {
        return reportDAO.summaryByDept();
    }

    public List<ReportRowDTO> summaryByProject() {
        return reportDAO.summaryByProject();
    }

    public List<ReportRowDTO> summaryDeptProject() {
        return reportDAO.summaryDeptProject();
    }
}
