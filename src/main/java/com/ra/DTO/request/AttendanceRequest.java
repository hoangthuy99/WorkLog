package com.ra.DTO.request;

import com.ra.Model.Entity.Users;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttendanceRequest {
    private int id;

    private Users user;
    private LocalDate workDate;
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
