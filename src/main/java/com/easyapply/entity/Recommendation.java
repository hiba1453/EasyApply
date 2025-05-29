package com.easyapply.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recommendations")
public class Recommendation {
    
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
    
    @Column(name = "match_score", nullable = false)
    private Double matchScore;
    
    @Column(columnDefinition = "TEXT")
    private String reasoning;
    
    @Column(name = "is_viewed")
    private Boolean isViewed = false;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructeurs
    public Recommendation() {}
    
    public Recommendation(User user, Job job, Double matchScore) {
        this.user = user;
        this.job = job;
        this.matchScore = matchScore;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    // ✅ NOUVEAUX GETTERS/SETTERS POUR LES RELATIONS
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }
    
    public Double getMatchScore() { return matchScore; }
    public void setMatchScore(Double matchScore) { this.matchScore = matchScore; }
    
    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
    
    public Boolean getIsViewed() { return isViewed; }
    public void setIsViewed(Boolean isViewed) { this.isViewed = isViewed; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    // ✅ MÉTHODES DE COMPATIBILITÉ (pour ne pas casser le code existant)
    public Long getUserId() { 
        return user != null ? user.getId() : null; 
    }
    
    public Long getJobId() { 
        return job != null ? job.getId() : null; 
    }
}