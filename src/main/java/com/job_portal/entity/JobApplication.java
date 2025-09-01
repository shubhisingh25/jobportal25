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

  
    @Column(name = "resume")
    private byte[] resume;

    @Column(name = "resume_file_name") // <- explicitly map to DB column
    private String resumeFileName;

    // Default constructor
    public JobApplication() {}

    // Parameterized constructor
    public JobApplication(Long id, Job job, User jobSeeker, String status, byte[] resume, String resumeFileName) {
        this.id = id;
        this.job = job;
        this.jobSeeker = jobSeeker;
        this.status = status;
        this.resume = resume;
        this.resumeFileName = resumeFileName;
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

    public byte[] getResume() { return resume; }
    public void setResume(byte[] resume) { this.resume = resume; }

    public String getResumeFileName() { return resumeFileName; }
    public void setResumeFileName(String resumeFileName) { this.resumeFileName = resumeFileName; }
}
