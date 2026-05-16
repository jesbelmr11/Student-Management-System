package com.ST.studentmanagementsystem.repository;

import com.ST.studentmanagementsystem.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Add this method for case-insensitive name search
    Teacher findByNameIgnoreCase(String name);

    // Optional: also keep email search for login
    Teacher findByEmail(String email);
}
