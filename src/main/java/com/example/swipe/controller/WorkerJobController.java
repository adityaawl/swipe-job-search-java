package com.example.swipe.controller;

import com.example.swipe.domain.Job;
import com.example.swipe.domain.Worker;
import com.example.swipe.service.JobSearchEngine;
import com.example.swipe.service.WorkerService;
import com.example.swipe.utility.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class WorkerJobController {

    private final WorkerService workerService;
    private final JobSearchEngine jobSearchEngine;

    public WorkerJobController(WorkerService workerService, JobSearchEngine jobSearchEngine) {
        this.workerService = workerService;
        this.jobSearchEngine = jobSearchEngine;
    }

    /**
     * Returns the matching job for given workerId if it's valid and exist in the system.
     *
     * @param workerId Integer value referring the {@link Worker#userId}
     * @param limit    Optional. Limit the result count. Default 3.
     * @return
     */
    @GetMapping("/recommend/{workerId}")
    public ResponseEntity recommendJobs(@PathVariable String workerId,
                                        @RequestParam(value = "limit", required = false, defaultValue = "3") Integer limit) {

        Logger.info(getClass(), "Recommend job request received for worker {} with limit {}.", workerId, limit);

        if (workerId == null || asLong(workerId, "workerId") == null) {
            Logger.error(getClass(), "Invalid workerId.");
            return ResponseEntity.badRequest().body("WorkerId is numeric value and it's mandatory.");
        }

        Worker worker = workerService.getWorker(asLong(workerId, null));

        if (worker == null) {
            Logger.error(getClass(), "Worker not found for id {}.", workerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid workerId. Record not found.");
        }

        Logger.debug(getClass(), "Fetching recommended jobs for worker {}.", worker);

        List<Job> matchingJobs = jobSearchEngine.findMatchingJobsForWorker(worker, limit);

        Logger.info(getClass(), "Found {} matching jobs for worker {}.", matchingJobs.size(), workerId);

        return ResponseEntity.ok(matchingJobs);
    }

    /**
     * Convert String to Long. Return 0 in case of exception.
     *
     * @param longStr
     * @param fieldName
     * @return
     */
    private Long asLong(String longStr, String fieldName) {
        try {
            return Long.parseLong(longStr);
        } catch (NumberFormatException e) {
            Logger.error(getClass(), "Invalid {} value {}.", fieldName, longStr);
        }
        return null;
    }
}
