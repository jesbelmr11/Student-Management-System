package com.ST.studentmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ST.studentmanagementsystem.model.StudentNotice;

public interface StudentNoticeRepository extends JpaRepository<StudentNotice, Long> {
}
