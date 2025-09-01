package com.job_portal.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "jobseeker_id")
    private User jobSeeker;

    private String status = "APPLIED";

    // Instead of storing file in DB
    @Column(name = "resume_link")
    private String resumeLink;

    // Default constructor
    public JobApplication() {}

    // Constructor with resumeLink
    public JobApplication(Long id, Job job, User jobSeeker, String status, String resumeLink) {
        this.id = id;
        this.job = job;
        this.jobSeeker = jobSeeker;
        this.status = status;
        this.resumeLink = resumeLink;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public User getJobSeeker() { return jobSeeker; }
    public void setJobSeeker(User jobSeeker) { this.jobSeeker = jobSeeker; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getResumeLink() { return resumeLink; }
    public void setResumeLink(String resumeLink) { this.resumeLink = resumeLink; }
}
