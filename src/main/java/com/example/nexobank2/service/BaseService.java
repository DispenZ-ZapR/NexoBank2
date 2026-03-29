package com.example.nexobank2.service;

import com.example.nexobank2.entity.BaseEntity;

import java.util.List;

public interface BaseService <T extends BaseEntity>{
    T save(T entity);
    void deleteById(Long id);
    T findById(Long id);
    List<T> findAll();
}
