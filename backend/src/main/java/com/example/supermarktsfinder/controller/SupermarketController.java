package com.example.supermarktsfinder.controller;

import com.example.supermarktsfinder.model.Supermarket;
import com.example.supermarktsfinder.service.SupermarketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supermarkets")
@CrossOrigin(origins = "*")
public class SupermarketController {

    private final SupermarketService service;

    public SupermarketController(SupermarketService service) {
        this.service = service;
    }

    @GetMapping
    public List<Supermarket> search(@RequestParam String address,
                                    @RequestParam String city,
                                    @RequestParam(defaultValue = "1000") int radius) {
        return service.findSupermarkets(address, city, radius);
    }
}
