package com.example.swipe.service;

import com.example.swipe.domain.Job;
import com.example.swipe.domain.Worker;
import com.example.swipe.domain.Worker.GeocodePreference;
import com.example.swipe.domain.common.Coordinates;
import com.example.swipe.utility.ApplicationUtilities;
import com.example.swipe.utility.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Search engine service responsible to find the matching {@link Job}s for a {@link Worker}.
 */
@Service
public class JobSearchEngine {

    @Autowired
    private JobService jobService;

    /**
     * Returns the best possible matching {@link Job}s for given {@link Worker}.
     * <br>
     * Matching Criteria:
     * <br> 1. Remove the jobs where workersRequired is 0.
     * <br> 2. Compare the driver license requirement.
     * <br> 3. Perform the Geocode matching. Calculate the distance between job & worker coordinates and validate it against maxJobDistance.
     * <br> 4. Look for certificate matching (score based).
     * <br>
     * Finally, returns the number of best matching jobs based on the limit and search score.
     *
     * @param worker {@link Worker}
     * @param limit  To restrict the result count.
     * @return
     */
    public List<Job> findMatchingJobsForWorker(Worker worker, int limit) {
        if (worker == null) {
            Logger.warn(getClass(), "Worker is null. Returning empty list.");
            return Collections.emptyList();
        }

        List<Job> jobs = jobService.getJobs();
        if (CollectionUtils.isEmpty(jobs)) {
            Logger.warn(getClass(), "No active jobs found to perform the match for worker {}.", worker.getUserId());
            return Collections.emptyList();
        }

        // 1. Remove the jobs where workersRequired is 0.
        // 2. Compare the driver license requirement.
        // 3. Perform the Geocode matching. Calculate the distance between job & worker coordinates and validate it against maxJobDistance.
        List<Job> matchingJobs = jobs.stream()
                .filter(j -> j.getWorkersRequired() > 0)
                .filter(j -> !j.getDriverLicenseRequired() || Objects.equals(worker.getHasDriverLicense(), j.getDriverLicenseRequired()))
                .filter(j -> matchJobSearchCriteria(j.getLocation(), worker.getJobSearchAddress()))
                .collect(Collectors.toList());

        Logger.debug(getClass(), "Found {} matching jobs after initial filtering for worker {}.", matchingJobs.size(), worker.getUserId());

        // Match the required certificates.
        if (!CollectionUtils.isEmpty(worker.getCertificates())) {
            Logger.debug(getClass(), "Matching required certificates from worker certificates {}.", worker.getCertificates());
            Map<Job, Double> jobCertificatesScore = matchingJobs.stream()
                    .collect(Collectors.toMap(Function.identity(),
                                              job -> getCertificateScore(job.getRequiredCertificates(), worker.getCertificates())));
            matchingJobs = jobCertificatesScore.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
        Logger.info(getClass(), "Total {} matching jobs after all the filtering for worker {}.", matchingJobs.size(), worker.getUserId());

        return matchingJobs.stream().limit(limit).collect(Collectors.toList());
    }

    /**
     * Check the distance between Job & Worker preferred coordinates. Return TRUE only if the distance is within the range specified in Workers
     * search preference.
     * <br>
     * Assumption: if job search preference is not provided by a worker, this will return TRUE assuming worker has no address preference.
     *
     * @param jobCoordinates   {@link Coordinates}. Referring to {@link Job#location}
     * @param workerPreference {@link Coordinates}. Referring to {@link Worker#jobSearchAddress}
     * @return
     */
    private boolean matchJobSearchCriteria(Coordinates jobCoordinates, GeocodePreference workerPreference) {
        try {
            double distance = ApplicationUtilities.distance(jobCoordinates, workerPreference, workerPreference.getUnit());
            Logger.debug(getClass(), "Job coordinates {} & Worker preference {}. Distance {}.", jobCoordinates, workerPreference, distance);
            return distance < workerPreference.getMaxJobDistance();

        } catch (Exception e) {
            Logger.error(getClass(),
                         "Exception occurred while calculating distance between Job coordinates {} & Worker preference {}.",
                         jobCoordinates,
                         workerPreference,
                         e);
        }
        // TODO revisit...
        return true;
    }

    /**
     * Compares the certificates details between the {@link Job} & {@link Worker}.
     * <br>
     * Current implementation is based on scoring (0...1). Score is 1, only if Worker has all the required certificates for a particular Job.
     *
     * @param jobRequiredCerts List of String. Referring to {@link Job#requiredCertificates}
     * @param workerCerts      List of String. Referring to {@link Worker#certificates}
     * @return
     */
    private double getCertificateScore(List<String> jobRequiredCerts, List<String> workerCerts) {
        if (CollectionUtils.isEmpty(jobRequiredCerts) || CollectionUtils.isEmpty(workerCerts)) {
            return 0;
        }
        return ((double) jobRequiredCerts.stream().filter(workerCerts::contains).count()) / (double) jobRequiredCerts.size();
    }
}
