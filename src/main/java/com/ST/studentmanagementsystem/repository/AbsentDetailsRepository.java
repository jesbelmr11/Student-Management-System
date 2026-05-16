package com.ST.studentmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ST.studentmanagementsystem.model.AbsentDetails;

public interface AbsentDetailsRepository extends JpaRepository<AbsentDetails, Long> {
}
