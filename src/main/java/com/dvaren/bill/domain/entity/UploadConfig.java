package com.dvaren.bill.domain.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "upload")
public class UploadConfig {
    private String filePath = "\\";

    private String domain;

    private Integer maxSize = 500;
}
