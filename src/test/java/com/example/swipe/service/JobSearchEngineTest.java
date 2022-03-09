package com.example.swipe.service;

import com.example.swipe.domain.Job;
import com.example.swipe.domain.Worker;
import com.example.swipe.domain.Worker.GeocodePreference;
import com.example.swipe.domain.common.Coordinates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Profile("test")
@ExtendWith(SpringExtension.class)
public class JobSearchEngineTest {

    @TestConfiguration
    static class JobSearchEngineTestContextConfiguration {

        @Bean
        public JobSearchEngine jobSearchEngine() {
            return new JobSearchEngine();
        }
    }

    @Autowired
    private JobSearchEngine jobSearchEngine;

    @MockBean
    private JobService jobService;

    @Test
    public void findMatchingJobsForNoWorkerReturnEmptyCollection() {
        List<Job> result = jobSearchEngine.findMatchingJobsForWorker(null, 0);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void findMatchingJobsForWorkerWithNoJobsAvailableReturnEmptyCollection() {
        Mockito.when(jobService.getJobs()).thenReturn(Collections.emptyList());

        List<Job> result = jobSearchEngine.findMatchingJobsForWorker(getWorker(), 0);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void findMatchingJobsForWorkerReturnMatchingResult() {
        Mockito.when(jobService.getJobs()).thenReturn(getJobs());

        List<Job> result = jobSearchEngine.findMatchingJobsForWorker(getWorker(), 3);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals((Long) 201L, result.get(0).getJobId());
        Assertions.assertEquals((Long) 101L, result.get(1).getJobId());

    }

    private Worker getWorker() {
        GeocodePreference jobSearchAddress = new GeocodePreference("km", 10);
        jobSearchAddress.setLongitude(15.067608);
        jobSearchAddress.setLatitude(50.081925);
        return Worker.builder()
                .userId(10001L)
                .hasDriverLicense(true)
                .jobSearchAddress(jobSearchAddress)
                .certificates(Arrays.asList("AA", "CC", "PP", "XX"))
                .build();
    }

    private List<Job> getJobs() {
        Job job1 = Job.builder()
                .jobId(101L)
                .workersRequired(1)
                .driverLicenseRequired(false)
                .location(new Coordinates(15.067609, 50.081925))
                .requiredCertificates(Arrays.asList("AA", "YY", "ZZ"))
                .build();
        Job job2 = Job.builder()
                .jobId(201L)
                .workersRequired(1)
                .driverLicenseRequired(true)
                .location(new Coordinates(15.067608, 50.082925))
                .requiredCertificates(Arrays.asList("AA", "CC", "PP", "XX", "YY", "ZZ"))
                .build();
        Job job3 = Job.builder()
                .jobId(301L)
                .workersRequired(1)
                .driverLicenseRequired(true)
                .location(new Coordinates(16.067608, 50.082925))
                .requiredCertificates(Arrays.asList("XX", "YY", "ZZ"))
                .build();
        return Arrays.asList(job1, job2, job3);
    }
}
