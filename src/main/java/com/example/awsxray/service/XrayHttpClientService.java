package com.example.awsxray.service;

import com.example.awsxray.dto.XrayDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class XrayHttpClientService {
    private static final String LOCALHOST_XRAY = "http://localhost:8080/xray";
    private final RestTemplate restTemplate;

    public List<XrayDTO> getXraiesWithRestTemplate() {
        return restTemplate.exchange(
                LOCALHOST_XRAY,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<XrayDTO>>(){}
            ).getBody();
    }
}
