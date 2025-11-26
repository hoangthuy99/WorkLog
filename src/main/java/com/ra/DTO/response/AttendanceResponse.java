package com.ra.DTO.response;

import com.ra.Model.Entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttendanceResponse {
    private int id;

    private Users user;
    private Date workDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Double totalHours;
    private Double overtimeHours;
    private int isHoliday; // thông tin ngày nghỉ
    private int status;// đã xác nhận - từ chối - chờ duyệt
    private LocalTime breakTime; // thời gian nghỉ trưa
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
