package com.job_portal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.job_portal.entity.Job;
import com.job_portal.entity.JobApplication;
import com.job_portal.entity.User;


public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByJob(Job job);
    List<JobApplication> findByJobSeeker(User jobSeeker);
}
