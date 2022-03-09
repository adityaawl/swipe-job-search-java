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
public class Worker {

    private Long userId;
    private String guid;
    private Integer age;
    private Name name;
    private String email;
    private String phone;
    private List<DayOfWeek> availability;
    private Boolean hasDriverLicense;
    private String transportation;
    private GeocodePreference jobSearchAddress;
    private List<String> skills;
    private List<String> certificates;
    private Boolean isActive;
    private Integer rating;

    public Boolean getHasDriverLicense() {
        return Optional.ofNullable(hasDriverLicense).orElse(Boolean.FALSE);
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
    public static class Name {

        private String first;
        private String last;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
    public static class DayOfWeek {

        private String title;
        private Integer dayIndex;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString(callSuper = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
    public static class GeocodePreference extends Coordinates {

        private String unit;
        private Integer maxJobDistance;
    }
}
