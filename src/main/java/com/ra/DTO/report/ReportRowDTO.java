package com.ra.DTO.report;

public class ReportRowDTO {
    public String deptCode;
    public String deptName;

    public String projectCode;
    public String projectName;

    public String taskCode;
    public String taskName;

    public int workMinutes;

    public ReportRowDTO(String deptCode, String deptName,
                        String projectCode, String projectName,
                        String taskCode, String taskName,
                        Long workMinutes) {
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.taskCode = taskCode;
        this.taskName = taskName;
        this.workMinutes = workMinutes != null ? workMinutes.intValue() : 0;
    }
}
