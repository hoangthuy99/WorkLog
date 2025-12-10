package com.ra.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "work_record")
public class WorkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Detail belongs to Attendance
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendanceId", nullable = false)
    private Attendance attendance;

    // Project (optional nếu ngày đó không thuộc dự án)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

    // Task
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskId")
    private Tasks task;

    @Column(nullable = false)
    private LocalTime startTime;   // giờ bắt đầu block

    @Column(nullable = false)
    private LocalTime endTime;     // giờ kết thúc block

    private Integer workMinutes; // tổng phút trong block
    private Integer breakWork; // phút nghỉ trong block (nếu có)

    private String remarks;
    private Integer status;

    private boolean deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public String getWorkTimeFormatted() {
        int totalSeconds = this.workMinutes * 60;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


}