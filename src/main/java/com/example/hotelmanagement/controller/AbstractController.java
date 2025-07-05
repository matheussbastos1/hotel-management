package com.example.hotelmanagement.controller;

import java.util.List;
import java.util.Optional;

public abstract class AbstractController<T> {
    // Todos os métodos devem ser public abstract para serem implementados publicamente
    public abstract List<T>  findAll() throws Exception;
    public abstract Optional<T> findById(int id) throws Exception;
    public abstract boolean add(T entity) throws Exception;
    public abstract boolean update(T entity) throws Exception;
    public abstract boolean remove(int id) throws Exception;
}