package com.example.swipe.service;

import com.example.swipe.domain.Job;
import com.example.swipe.utility.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation to perform {@link Job} related operations.
 */
@Service
public class JobService {

    private static final String JOB_ENDPOINT = "/jobs";

    private final RestTemplate restTemplate;

    @Value("${swipeJobServer}")
    private String swipeJobServer;

    public JobService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Job> getJobs() {
        ResponseEntity<Job[]> response = restTemplate.getForEntity(swipeJobServer + JOB_ENDPOINT, Job[].class);

        Logger.debug(getClass(), "GET /workers API response code {} w/ body {}.", response.getStatusCode(), response.getBody());

        return Optional.of(response)
                .filter(res -> res.getStatusCode() == HttpStatus.OK && res.getBody() != null)
                .map(res -> Arrays.asList(res.getBody()))
                .orElse(Collections.emptyList());
    }
}
