package com.prooptykwebapi.prooptyk.service;


import com.prooptykwebapi.prooptyk.model.Eyeglass;

import java.util.List;

public interface IEyeglassService {

    List<Eyeglass> getAllEyeglasses();
    Eyeglass getEyeglassById(long eyeglassId);
    void addEyeglass(Eyeglass eyeglass);
    void updateEyeglass(Eyeglass eyeglass);
    void deleteEyeglass(long eyeglassId);
}
