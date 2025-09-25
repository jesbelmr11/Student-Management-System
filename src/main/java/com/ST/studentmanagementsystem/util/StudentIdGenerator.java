package com.ST.studentmanagementsystem.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String prefix = "STUD";
        int nextId = 1;

        try {
            Statement stmt = session.connection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT student_id FROM student ORDER BY student_id DESC LIMIT 1");

            if (rs.next()) {
                String lastId = rs.getString(1); // e.g., STUD0005
                int lastNum = Integer.parseInt(lastId.replace(prefix, ""));
                nextId = lastNum + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error generating Student ID", e);
        }

        return String.format("%s%04d", prefix, nextId); // STUD0001, STUD0002 ...
    }
}
