package com.ra.Report;

import com.ra.DTO.report.ReportRowDTO;
import java.util.List;

public interface IReportDAO {
    List<ReportRowDTO> summaryByDept();
    List<ReportRowDTO> summaryByProject();
    List<ReportRowDTO> summaryDeptProject();
}
