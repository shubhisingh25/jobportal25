package com.job_portal.config;

import com.job_portal.entity.User;
import com.job_portal.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    // Password encoder bean
   

    // UserDetailsService to load user from DB
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            User user = userService.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            // normalize role to uppercase and trim spaces
            String role = user.getRole().toUpperCase().trim();

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role))
            );
        };
    }

    // Authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,CustomSuccessHandeler customSuccessHandler) throws Exception {
        http 
       .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/jobs/list").permitAll()
                .requestMatchers("/jobs/create", "/applications/job/**","/jobs/delete/**").hasRole("RECRUITER")
                .requestMatchers("/applications/apply/**", "/applications/my-applications").hasRole("JOBSEEKER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                //.defaultSuccessUrl("/jobs/list")
                .successHandler(customSuccessHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/auth/login")
                .permitAll()
            );

        return http.build();
    }
}
//   /auth/login , /jobs/list koi bhi use kr skta h 
//   / applications/job /{id}  - by recruiter... isse pta chlega ki kisne kisne apply ki h ..
//  /jobs/create ... by recruiter .. isse jobs create krega
// /applications/my-applications ... by jobseeker .. isse status dekhega 
// recruiter k liye sbse phle job list khulegi .. 
// job seeker k liye sbse phle uski applications lk status khulenge 