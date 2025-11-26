package com.ra.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


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

    // 1 user - nhiều attendance
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private Users user;


    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private LocalTime breakTime;// thời gian nghỉ giải lao
    @Column(nullable = false)
    private LocalDate workDate;   // ngày làm việc

    private Integer totalMinutes; // tổng phút làm việc trong ngày
    private Integer overtimeMinutes; // tổng OT phút
    private Integer breakMinutes; // tổng phút nghỉ

    private int isHoliday;
    private int status; // đã xác nhận - từ chối - chờ duyệt

    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // WorkRecord (1) - (n) WorkRecord
    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL)
    private List<WorkRecord> workRecords;
}

