package com.ra.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;
    private Date workDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Double totalHours;
    private Double overtimeHours;
    private int isHoliday; // thông tin ngày nghỉ
    private int status;// 0 = chưa check-in, 1 = đang làm, 2 = đã check-out, 3 = nghỉ phép
    private LocalTime breakTime; // thời gian nghỉ trưa
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
