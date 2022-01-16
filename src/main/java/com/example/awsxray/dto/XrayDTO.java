package com.example.awsxray.dto;

import com.example.awsxray.entity.Xray;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class XrayDTO {
    private Long id;
    private String name;

    @Builder
    public XrayDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static XrayDTO toDTO(Xray xray) {
        return XrayDTO.builder()
                .id(xray.getId())
                .name(xray.getName())
                .build();
    }
}
