package com.prooptykwebapi.prooptyk.controller;


import com.prooptykwebapi.prooptyk.model.Eyeglass;
import com.prooptykwebapi.prooptyk.service.IEyeglassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EyeglassController {

    @Autowired
    private IEyeglassService eyeglassService;

    @RequestMapping(value = "/eyeglasses/getall", method = RequestMethod.GET)
    public Iterable<Eyeglass> getAllEyeglasses() {
        return this.eyeglassService.getAllEyeglasses();
    }

    @RequestMapping(value = "/eyeglasses/add", method = RequestMethod.POST)
    public String addEyeglass(@RequestBody Eyeglass eyeglass) {
        eyeglassService.addEyeglass(eyeglass);
        return "Succesfully added";
    }
}
