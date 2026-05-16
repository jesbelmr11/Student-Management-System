package com.ST.studentmanagementsystem.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class TeacherIdGenerator implements IdentifierGenerator {

    private static final String PREFIX = "TEA";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {
        try {
            String query = "SELECT COUNT(t) FROM Teacher t";
            Long count = (Long) session.createQuery(query).uniqueResult();
            long nextId = (count == null ? 1 : count + 1);
            return String.format("%s%04d", PREFIX, nextId);
        } catch (Exception e) {
            throw new HibernateException("Unable to generate Teacher ID", e);
        }
    }
}
