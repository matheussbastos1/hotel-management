package com.example.hotelmanagement.repository;

import com.example.hotelmanagement.repository.repositoryExceptions.StayNotFoundException;
import com.example.hotelmanagement.models.Stay;

import java.util.ArrayList;
import java.util.List;

public interface StayRepository {

    List<Stay> stays = new ArrayList<>();

    void addStay(Stay stay);
    Stay findStayById(int stayId) throws StayNotFoundException;
    void updateStay(Stay stay) throws StayNotFoundException;
    List<Stay> getAllStays();
    void deleteStayById(int stayId) throws StayNotFoundException;
}
