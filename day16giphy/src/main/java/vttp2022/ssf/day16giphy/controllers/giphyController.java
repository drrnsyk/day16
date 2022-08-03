package vttp2022.ssf.day16giphy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/giphy")
public class giphyController {
    
    @GetMapping
    public String getGiphy (Model model, 
        @RequestParam String searchTerm, 
        @RequestParam Integer limit,
        @RequestParam String ratings) {

        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("limit", limit);
        model.addAttribute("ratings", ratings);
        return "giphy";
        
    }
    
}
