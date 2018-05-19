package com.prooptykwebapi.prooptyk.service;

import com.prooptykwebapi.prooptyk.model.Eyeglass;
import com.prooptykwebapi.prooptyk.repository.EyeglassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EyeglassService implements IEyeglassService {

    @Autowired
    private EyeglassRepository eyeglassRepository;


    @Override
    public List<Eyeglass> getAllEyeglasses() {
        return this.eyeglassRepository.findAll();
    }

    @Override
    public Eyeglass getEyeglassById(long eyeglassId) {
        return null;
    }

    @Override
    public void addEyeglass(Eyeglass eyeglass) {
        eyeglassRepository.save(eyeglass);
    }

    @Override
    public void updateEyeglass(Eyeglass eyeglass) {

    }

    @Override
    public void deleteEyeglass(long eyeglassId) {

    }
}
