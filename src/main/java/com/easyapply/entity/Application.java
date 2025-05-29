package com.easyapply.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "applications")
public class Application {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // ✅ REMPLACE userId par relation ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // ✅ REMPLACE jobId par relation ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
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
    
    public Application(User user, Job job) {
        this.user = user;
        this.job = job;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    // ✅ NOUVEAUX GETTERS/SETTERS POUR LES RELATIONS
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }
    
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    
    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
    
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // ✅ MÉTHODES DE COMPATIBILITÉ (pour ne pas casser le code existant)
    public Long getUserId() { 
        return user != null ? user.getId() : null; 
    }
    
    public Long getJobId() { 
        return job != null ? job.getId() : null; 
    }
}