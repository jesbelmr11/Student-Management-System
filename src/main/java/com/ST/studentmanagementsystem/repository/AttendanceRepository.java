package com.ST.studentmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ST.studentmanagementsystem.model.Attendance;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    // Spring Data JPA will auto-implement this
    List<Attendance> findByStudentId(String studentId);
}
