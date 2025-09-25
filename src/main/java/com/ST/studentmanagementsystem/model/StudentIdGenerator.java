package com.ST.studentmanagementsystem.model;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

public class StudentIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        // Generates ID like STUD-1a2b3c4d
        return "STUD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
