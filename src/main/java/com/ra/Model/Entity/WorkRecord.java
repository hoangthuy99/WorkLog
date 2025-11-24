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
@Table(name = "work_record")
public class WorkRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Users user;
    private Date workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Double totalHours;
    private int status; // trạng thái bản ghi công
    private String remarks; // ghi chú về bản ghi công
    private LocalDateTime deletedAt;// thời gian xóa bản ghi tam thơi
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
