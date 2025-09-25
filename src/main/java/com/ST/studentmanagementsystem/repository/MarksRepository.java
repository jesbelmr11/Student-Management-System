package com.ST.studentmanagementsystem.repository;

import com.ST.studentmanagementsystem.model.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {

    // Custom finder method to get marks by Student.id
    List<Marks> findByStudentId(Long studentId);
}
