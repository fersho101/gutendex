package com.example.gutendex.service;


import com.example.gutendex.model.GutendexResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ApiService {
    private final RestTemplate restTemplate;

    @Value("${gutendex.api.url}")
    private String apiUrl;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GutendexResponse searchBookByTitle(String title) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("search", title)
                .toUriString();

        return restTemplate.getForObject(url, GutendexResponse.class);
    }
}
