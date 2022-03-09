package com.example.swipe.service;

import com.example.swipe.domain.Worker;
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
 * Service implementation to perform {@link Worker} related operations.
 */
@Service
public class WorkerService {

    private static final String WORKER_ENDPOINT = "/workers";

    private final RestTemplate restTemplate;

    @Value("${swipeJobServer}")
    private String swipeJobServer;

    public WorkerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Worker> getWorkers() {
        ResponseEntity<Worker[]> response = restTemplate.getForEntity(swipeJobServer + WORKER_ENDPOINT, Worker[].class);

        Logger.debug(getClass(), "GET /workers API response code {} w/ body {}.", response.getStatusCode(), response.getBody());

        return Optional.of(response)
                .filter(res -> res.getStatusCode() == HttpStatus.OK && res.getBody() != null)
                .map(res -> Arrays.asList(res.getBody()))
                .orElse(Collections.emptyList());
    }

    public Worker getWorker(Long workerId) {
        if (workerId == null) {
            return null;
        }

        return getWorkers().stream().filter(w -> workerId.equals(w.getUserId())).findFirst().orElse(null);
    }
}
