package com.example.awsxray.controller;

import com.example.awsxray.dto.XrayDTO;
import com.example.awsxray.service.XrayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class XrayController {
    private final XrayService xrayService;

    @GetMapping("/xray")
    public List<XrayDTO> getXraies() {
        return xrayService.getXraies();
    }

    @GetMapping("/xray/{id}")
    public XrayDTO getXray(@PathVariable Long id) {
        return xrayService.getXray(id);
    }

    @PostMapping("/xray")
    public void createXray(@RequestBody List<XrayDTO> xrayDTOS) {
        xrayService.createXray(xrayDTOS);
    }

    @DeleteMapping("/xray/{id}")
    public void removeXray(@PathVariable Long id) {
        xrayService.removeXray(id);
    }

    @GetMapping("/api/xray")
    public List<XrayDTO> getXraiesWithRestTemplate() {
        return xrayService.getXraiesWithRestTemplate();
    }
}

