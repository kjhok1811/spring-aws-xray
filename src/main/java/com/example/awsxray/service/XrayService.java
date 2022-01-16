package com.example.awsxray.service;

import com.example.awsxray.dto.XrayDTO;
import com.example.awsxray.entity.Xray;
import com.example.awsxray.repository.XrayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class XrayService {
    private final XrayRepository xrayRepository;
    private final XrayHttpClientService xrayHttpClientService;

    public List<XrayDTO> getXraies() {
        return xrayRepository.findAll().stream().map(XrayDTO::toDTO).collect(Collectors.toList());
    }

    public XrayDTO getXray(Long id) {
        Xray xray = xrayRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return XrayDTO.builder()
                .id(xray.getId())
                .name(xray.getName())
                .build();
    }

    @Transactional
    public void createXray(List<XrayDTO> xrayDTOS) {
        List<Xray> xraies = xrayDTOS.stream().map(Xray::toEntity).collect(Collectors.toList());
        xrayRepository.saveAll(xraies);
    }

    @Transactional
    public void removeXray(Long id) {
        xrayRepository.deleteById(id);
    }

    public List<XrayDTO> getXraiesWithRestTemplate() {
        return xrayHttpClientService.getXraiesWithRestTemplate();
    }
}
