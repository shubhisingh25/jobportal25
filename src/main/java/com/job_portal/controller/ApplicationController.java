package com.job_portal.controller;

import com.job_portal.entity.Job;
import com.job_portal.entity.JobApplication;
import com.job_portal.entity.User;
import com.job_portal.service.ApplicationService;
import com.job_portal.service.JobService;
import com.job_portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService appService;

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

    // Apply for a job
//    @PostMapping("/apply/{jobId}")
//    public String applyJob(@PathVariable Long jobId, Principal principal) {
//        User seeker = userService.findByEmail(principal.getName());
//        Job job = jobService.findById(jobId);
//
//        if (job != null && seeker != null) {
//            JobApplication app = new JobApplication();
//            app.setJob(job);
//            app.setJobSeeker(seeker);
//            appService.apply(app);
//        }
//        return "redirect:/applications/my-applications";
//    }

    // View applications of the logged-in job seeker
    @GetMapping("/my-applications")
    public String myApplications(Model model, Principal principal) {
        User seeker = userService.findByEmail(principal.getName());
        List<JobApplication> applications = appService.findByJobSeeker(seeker);
        model.addAttribute("applications", applications);
        return "my-applications";
    }

    // View applicants for a specific job (for recruiters)
    @GetMapping("/job/{jobId}")
    public String jobApplicants(@PathVariable Long jobId, Model model) {
        Job job = jobService.findById(jobId);
        if (job != null) {
            List<JobApplication> applications = appService.findByJob(job);
            model.addAttribute("applications", applications);
        }
        return "job-applicants";
    }

    // Update status of an application
    @PostMapping("/update-status/{appId}")
    public String updateStatus(@PathVariable Long appId, @RequestParam String status) {
        appService.updateStatus(appId, status);
        JobApplication updatedApp = appService.findByJobSeeker(null).stream()
                .filter(a -> a.getId().equals(appId))
                .findFirst()
                .orElse(null);

        if (updatedApp != null) {
            return "redirect:/applications/job/" + updatedApp.getJob().getId();
        }
        return "redirect:/jobs/list";
    }
    
    @PostMapping("/apply/{jobId}")
    public String applyJobWithResume(@PathVariable Long jobId, @RequestParam("resume") MultipartFile resumeFile, Principal principal) throws IOException {
        User seeker = userService.findByEmail(principal.getName());
        Job job = jobService.findById(jobId);

        if (job != null && seeker != null) {
            JobApplication app = new JobApplication();
            app.setJob(job);
            app.setJobSeeker(seeker);
            if (!resumeFile.isEmpty()) {
                app.setResume(resumeFile.getBytes());
                app.setResumeFileName(resumeFile.getOriginalFilename());
            }
            appService.apply(app);
        }
        return "redirect:/applications/my-applications";
    }

    // Download resume (for recruiters)
@GetMapping("/download-resume/{appId}")
public ResponseEntity<byte[]> downloadResume(@PathVariable Long appId) {
    JobApplication app = appService.findById(appId);
    
    if (app == null || app.getResume() == null) {
        return ResponseEntity.notFound().build();
    }

    // Try to guess file type (default PDF if null)
    String fileName = app.getResumeFileName() != null ? app.getResumeFileName() : "resume.pdf";
    MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
    if (fileName.endsWith(".pdf")) {
        mediaType = MediaType.APPLICATION_PDF;
    } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
        mediaType = MediaType.valueOf("application/msword");
    }

    return ResponseEntity.ok()
            .contentType(mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
            .body(app.getResume());
}

}
