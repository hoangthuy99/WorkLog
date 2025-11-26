package com.ra.DTO.request;

import com.ra.Model.Entity.Users;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class WorkRecordRequest {
    private int id;
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
