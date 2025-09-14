package com.example.supermarktsfinder.service;

import com.example.supermarktsfinder.model.Supermarket;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class SupermarketService {
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Supermarket> findSupermarkets(String address, String city, int radius) {
        String query = address + ", " + city;
        String geocodeUrl = UriComponentsBuilder.fromUriString("https://nominatim.openstreetmap.org/search")
                .queryParam("format", "json")
                .queryParam("limit", "1")
                .queryParam("q", query)
                .toUriString();

        List<Map<String, Object>> geoResponse = restTemplate.getForObject(geocodeUrl, List.class);
        if (geoResponse == null || geoResponse.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Object> first = geoResponse.get(0);
        double lat = Double.parseDouble((String) first.get("lat"));
        double lon = Double.parseDouble((String) first.get("lon"));

        String overpassQuery = String.format(Locale.US,
                "[out:json];node[\"shop\"=\"supermarket\"](around:%d,%f,%f);out;",
                radius, lat, lon);
        String overpassUrl = "https://overpass-api.de/api/interpreter?data=" +
                URLEncoder.encode(overpassQuery, StandardCharsets.UTF_8);

        Map<String, Object> overpassResponse = restTemplate.getForObject(overpassUrl, Map.class);
        if (overpassResponse == null || !overpassResponse.containsKey("elements")) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> elements = (List<Map<String, Object>>) overpassResponse.get("elements");
        List<Supermarket> supermarkets = new ArrayList<>();
        for (Map<String, Object> el : elements) {
            Supermarket sm = new Supermarket();
            Map<String, Object> tags = (Map<String, Object>) el.get("tags");
            if (tags != null) {
                sm.setName((String) tags.getOrDefault("name", ""));
                String street = (String) tags.getOrDefault("addr:street", "");
                String house = (String) tags.getOrDefault("addr:housenumber", "");
                String cityTag = (String) tags.getOrDefault("addr:city", "");
                String addressStr = (street + " " + house).trim();
                if (!cityTag.isEmpty()) {
                    addressStr = addressStr + ", " + cityTag;
                }
                sm.setAddress(addressStr.trim());
            }
            sm.setLat(((Number) el.get("lat")).doubleValue());
            sm.setLon(((Number) el.get("lon")).doubleValue());
            supermarkets.add(sm);
        }
        return supermarkets;
    }
}
