package com.example.Hotel_management.repository;

import com.example.Hotel_management.repository.repositoryExceptions.StayNotFoundException;
import com.example.Hotel_management.models.Stay;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface StayRepository {

    List<Stay> stays = new ArrayList<>();

    void addStay(Stay stay);
    Stay findStayById(int stayId) throws StayNotFoundException;
    void updateStay(Stay stay) throws StayNotFoundException;
    List<Stay> getAllStays();
    void deleteStayById(int stayId) throws StayNotFoundException;
}
