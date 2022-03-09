package com.example.swipe.controller;

import com.example.swipe.Application;
import com.example.swipe.domain.Job;
import com.example.swipe.domain.Worker;
import com.example.swipe.service.WorkerService;
import com.example.swipe.utility.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Profile("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class WorkerJobControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private WorkerService workerService;
    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void recommendJobsWithoutWorkerIdReturnException() throws JsonProcessingException {
        String invalidWorkerId = "ab123";

        ResponseEntity<String> response = testRestTemplate.getForEntity("/jobs/recommend/" + invalidWorkerId, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("WorkerId is numeric value and it's mandatory.");
    }

    @Test
    public void recommendJobsForWorkerIdNotExistReturnException() throws JsonProcessingException {
        String invalidWorkerId = "123";

        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(Worker[].class))).thenReturn(ResponseEntity.ok(new Worker[]{}));

        ResponseEntity<String> response = testRestTemplate.getForEntity("/jobs/recommend/" + invalidWorkerId, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Invalid workerId. Record not found.");
    }

    @Test
    public void recommendJobsForWorkerIdNoJobReturnEmptyResponse() throws IOException {

        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(Worker[].class))).thenReturn(ResponseEntity.ok(loadWorkers()));
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(Job[].class))).thenReturn(ResponseEntity.ok(new Job[]{}));

        List<Worker> workers = workerService.getWorkers();

        if (CollectionUtils.isEmpty(workers)) {
            Logger.error(getClass(), "No worker record found.");
            return;
        }

        Worker worker = workers.get(0);

        ResponseEntity<List> response = testRestTemplate.getForEntity("/jobs/recommend/" + worker.getUserId(), List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(0);
    }

    @Test
    public void recommendJobsForWorkerIdMatchingJobResponse() throws IOException {

        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(Worker[].class))).thenReturn(ResponseEntity.ok(loadWorkers()));
        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(Job[].class))).thenReturn(ResponseEntity.ok(loadJobs()));

        List<Worker> workers = workerService.getWorkers();

        if (CollectionUtils.isEmpty(workers)) {
            Logger.error(getClass(), "No worker record found.");
            return;
        }

        Map<Long, Integer> testWorkerWithExpectedResultCount = new HashMap<>();
        testWorkerWithExpectedResultCount.put(0L, 3);
        testWorkerWithExpectedResultCount.put(7L, 11);
        testWorkerWithExpectedResultCount.put(10L, 0);
        testWorkerWithExpectedResultCount.put(12L, 14);
        testWorkerWithExpectedResultCount.put(24L, 0);
        testWorkerWithExpectedResultCount.put(37L, 8);
        testWorkerWithExpectedResultCount.put(46L, 4);

        //Worker worker = workers.get(0);
        workers.stream().filter(w -> testWorkerWithExpectedResultCount.containsKey(w.getUserId())).forEach(worker -> {
            ResponseEntity<List> response = testRestTemplate.getForEntity("/jobs/recommend/" + worker.getUserId() + "?limit=20", List.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().size()).isEqualTo(testWorkerWithExpectedResultCount.get(worker.getUserId()));
        });
        //assertThat(response.getBody().size()).isNotZero();
    }

    private Worker[] loadWorkers() throws IOException {

        Resource workerJsonResource = resourceLoader.getResource("classpath:data/workers.json");
        return objectMapper.readValue(workerJsonResource.getInputStream(), Worker[].class);
    }

    private Job[] loadJobs() throws IOException {

        Resource jobJsonResource = resourceLoader.getResource("classpath:data/jobs.json");
        return objectMapper.readValue(jobJsonResource.getInputStream(), Job[].class);
    }
}
