package com.ra.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
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
    @Column(nullable = false)
    private LocalDate workDate;   // ngày làm việc

    private Integer totalMinutes; // tổng phút làm việc trong ngày
    private Integer overtimeMinutes; // tổng OT phút
    private Integer breakMinutes; // tổng phút nghỉ
    private int extraDay; // làm thêm giờ chuyển qua ngày hôm sau


    private boolean isHoliday;
    private int status; // đã xác nhận - từ chối - chờ duyệt

    private boolean deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // WorkRecord (1) - (n) WorkRecord
    @OneToMany(mappedBy = "attendance", fetch = FetchType.EAGER)
    private List<WorkRecord> workRecords;



    public void calculateTimes() {
        // 1. Tính total breakWork từ WorkRecord
        int totalBreak = 0;


        if (workRecords != null) {
            totalBreak = workRecords.stream()
                    .mapToInt(w -> w.getBreakWork() == null ? 0 : w.getBreakWork())
                    .sum();
        }

        this.breakMinutes = totalBreak; // GÁN TỰ ĐỘNG

        // 2. Nếu có check-in và check-out mới tính totalMinutes
        if (checkInTime == null || checkOutTime == null) {
            totalMinutes = 0;
            overtimeMinutes = 0;
            return;
        }

        LocalDateTime inDT = LocalDateTime.of(workDate, checkInTime);
        LocalDateTime outDT = LocalDateTime.of(workDate, checkOutTime)
                .plusDays(extraDay);

        int total = (int) Duration.between(inDT, outDT).toMinutes();

        totalMinutes = total - (breakMinutes == null ? 0 : breakMinutes);
        if (totalMinutes < 0) totalMinutes = 0;

        overtimeMinutes = Math.max(0, totalMinutes - 480);
    }

}
