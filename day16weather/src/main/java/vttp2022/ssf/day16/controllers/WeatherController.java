package vttp2022.ssf.day16.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssf.day16.models.Weather;
import vttp2022.ssf.day16.services.WeatherService;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherSvc;
    
    @GetMapping
    public String getWeather(Model model, @RequestParam String city) {

        // run the service  
        List<Weather> list = weatherSvc.getWeather(city);  
        model.addAttribute("city", city.toUpperCase());
        model.addAttribute("list", list);
        return "weather";
    }



}  
