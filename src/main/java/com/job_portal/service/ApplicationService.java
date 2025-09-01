package com.job_portal.service;

import com.job_portal.entity.Job;
import com.job_portal.entity.JobApplication;
import com.job_portal.entity.User;
import com.job_portal.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private JobApplicationRepository applicationRepository;

    // Apply for a job
    public void apply(JobApplication app) {
        applicationRepository.save(app);
    }

    // Get applications for a specific job
    public List<JobApplication> findByJob(Job job) {
        return applicationRepository.findByJob(job);
    }

    // Get applications made by a specific job seeker
    public List<JobApplication> findByJobSeeker(User user) {
        return applicationRepository.findByJobSeeker(user);
    }

    // Delete application by ID
    public void delete(Long appId) {
        applicationRepository.deleteById(appId);
    }

    // Update application status
    public void updateStatus(Long appId, String status) {
        JobApplication app = applicationRepository.findById(appId).orElse(null);
        if (app != null) {
            app.setStatus(status);
            applicationRepository.save(app);
        }
    }
    public JobApplication findById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

}
