package com.easyapply.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "job_id", nullable = false)
    private Long jobId;
    
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PENDING;
    
    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;
    
    @CreationTimestamp
    @Column(name = "applied_at")
    private LocalDateTime appliedAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum ApplicationStatus {
        PENDING, VIEWED, ACCEPTED, REJECTED
    }
    
    // Constructeurs
    public Application() {}
    
    public Application(Long userId, Long jobId) {
        this.userId = userId;
        this.jobId = jobId;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    
    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
    
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}