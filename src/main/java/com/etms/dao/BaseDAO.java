package com.etms.dao;

import java.util.List;

public interface BaseDAO<T> {
    T findById(int id) throws Exception;

    List<T> findAll() throws Exception;

    void save(T entity) throws Exception;

    void update(T entity) throws Exception;

    void delete(int id) throws Exception;
}