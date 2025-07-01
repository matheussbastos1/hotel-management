package com.example.hotelmenagement.repository.impl;

import com.example.hotelmenagement.repository.StayRepository;
import com.example.hotelmenagement.service.repositoryExceptions.StayNotFoundException;
import com.example.hotelmenagement.models.Stay;

import java.util.ArrayList;
import java.util.List;

public class StayRepositoryImpl implements StayRepository {
    @Override
    public Stay findStayById(int stayId) throws StayNotFoundException {
        for (Stay stay : stays) {
            if (stay.getStayId() == stayId) {
                return stay;
            }
        }
        throw new StayNotFoundException("Estadia com o ID " + stayId + " não encontrada.");
    }

    @Override
    public void updateStay(Stay stay) throws StayNotFoundException {
        for (int i = 0; i < stays.size(); i++) {
            if (stays.get(i).getStayId() == stay.getStayId()) {
                stays.set(i, stay);
                return;
            }
        }
    }

    @Override
    public List<Stay> getAllStays() {
        return new ArrayList<>(this.stays);
    }

    @Override
    public void deleteStayById(int stayId) throws StayNotFoundException {
        for (int i = 0; i < stays.size(); i++) {
            if (stays.get(i).getStayId() == stayId) {
                stays.remove(i);
                return;
            }
        }
        throw new StayNotFoundException("Estadia com o ID " + stayId + " não encontrada para exclusão.");
    }

    ArrayList<Stay> stays = new ArrayList<>();

    @Override
    public void addStay(Stay stay) {
        this.stays.add(stay);
    }


}
