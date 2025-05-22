package com.easyapply.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendations")
public class Recommendation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "job_id", nullable = false)
    private Long jobId;
    
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
    
    public Recommendation(Long userId, Long jobId, Double matchScore) {
        this.userId = userId;
        this.jobId = jobId;
        this.matchScore = matchScore;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    
    public Double getMatchScore() { return matchScore; }
    public void setMatchScore(Double matchScore) { this.matchScore = matchScore; }
    
    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
    
    public Boolean getIsViewed() { return isViewed; }
    public void setIsViewed(Boolean isViewed) { this.isViewed = isViewed; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
}