package com.example.hotelmenagement.controller;

import java.util.List;
import java.util.Optional;
import com.example.hotelmenagement.models.Stay;
import com.example.hotelmenagement.service.repositoryExceptions.StayNotFoundException;
import com.example.hotelmenagement.repository.impl.StayRepositoryImpl;

public class StayController extends AbstractController<Stay> {
    private final StayRepositoryImpl stayRepository;

    public StayController(StayRepositoryImpl stayRepository) {
        this.stayRepository = stayRepository;
    }

    @Override
    public boolean add(Stay stay) {
        stayRepository.addStay(stay);
        return true;
    }

    @Override
    public boolean update(Stay stay) throws StayNotFoundException {
        stayRepository.updateStay(stay);
        return true;
    }

    @Override
    public boolean remove(int id) throws StayNotFoundException {
        Stay stay = stayRepository.findStayById(id);
        Long stayId = stay.getStayId();
        int stayIdInt = stayId.intValue();
        stayRepository.deleteStayById(stayIdInt);
        return true;
    }

    @Override
    public List<Stay> findAll() throws StayNotFoundException {
        List<Stay> stays = stayRepository.getAllStays();
        if (stays.isEmpty()) {
            throw new StayNotFoundException("Nenhuma estadia encontrada.");
        }
        return stays;
    }

    @Override
    public Optional<Stay> findById(int id) throws StayNotFoundException {
        Stay stay = stayRepository.findStayById(id);
        return Optional.of(stay);

    }
}
