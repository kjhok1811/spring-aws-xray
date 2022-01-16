package com.example.awsxray.entity;

import javax.persistence.*;

import com.example.awsxray.dto.XrayDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Xray {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Builder
    public Xray(String name) {
        this.name = name;
    }

    public static Xray toEntity(XrayDTO xrayDTO) {
        return Xray.builder()
                .name(xrayDTO.getName())
                .build();
    }
}
