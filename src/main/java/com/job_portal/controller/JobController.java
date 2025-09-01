package com.job_portal.controller;

import com.job_portal.entity.Job;
import com.job_portal.entity.User;
import com.job_portal.service.JobService;
import com.job_portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

    // List all jobs
    @GetMapping("/list")
    public String listJobs(Model model) {
        model.addAttribute("jobs", jobService.findAll());
        return "jobs"; // jobs.html
    }

    // Show form to create a new job
    @GetMapping("/create")
    public String createJob(Model model) {
        model.addAttribute("job", new Job());
        return "create-job";
    }

    // Save a new job posted by a recruiter
    @PostMapping("/create")
    public String saveJob(@ModelAttribute Job job, Principal principal) {
        User recruiter = userService.findByEmail(principal.getName());
        job.setRecruiter(recruiter);
        jobService.save(job);
        return "redirect:/jobs/list";
    }

    // Delete a job (Recruiter only)
    @PostMapping("/delete/{id}")
    public String deleteJob(@PathVariable Long id) {
        System.out.println("Deleting job with ID: " + id);
        jobService.deleteJob(id);
        return "redirect:/jobs/list";
    }
    @GetMapping("/delete/test")
    @ResponseBody
    public String testDelete() {
        return "Controller is reachable";
    }


}
