package com.job_portal.service;

import com.job_portal.entity.Job;
import com.job_portal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationService applicationService;

    // Save a job
    public void save(Job job) {
        jobRepository.save(job);
    }

    // Get all jobs
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    // Find job by ID
    public Job findById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    // Delete job and its applications
    public void deleteJob(Long jobId) {
        Job job = findById(jobId);
        if (job != null) {
            // Delete all applications related to this job
            applicationService.findByJob(job).forEach(app -> applicationService.delete(app.getId()));
            // Delete the job itself
            jobRepository.delete(job);
        }
    }
}
