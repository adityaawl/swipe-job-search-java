package com.example.swipe.domain;

import com.example.swipe.domain.common.Coordinates;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class Job {

    private Long jobId;
    private String guid;
    private String company;
    private String jobTitle;
    private String about;
    private Instant startTime;
    private Integer workersRequired;
    private String billRate;
    private Coordinates location;
    private List<String> requiredCertificates;
    private Boolean driverLicenseRequired;

    public Integer getWorkersRequired() {
        return Optional.ofNullable(workersRequired).orElse(0);
    }

    public Boolean getDriverLicenseRequired() {
        return Optional.ofNullable(driverLicenseRequired).orElse(Boolean.FALSE);
    }
}
