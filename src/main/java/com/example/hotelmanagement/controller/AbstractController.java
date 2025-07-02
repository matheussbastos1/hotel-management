package com.example.hotelmanagement.controller;

import java.util.List;
import java.util.Optional;

public abstract class AbstractController<T> {
    protected abstract List<T>  findAll() throws Exception;
    protected abstract Optional<T> findById(int id) throws Exception;
    protected abstract boolean add(T entity) throws Exception;
    protected abstract boolean update(T entity) throws Exception;
    protected abstract boolean remove(int id) throws Exception;
}
