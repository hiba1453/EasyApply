package com.easyapply.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
public class Profile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    private String summary;
    private String skills;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructeur
    public Profile() {}
    
    // Getters et Setters
    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public Long getUserId() { 
        return userId; 
    }
    
    public void setUserId(Long userId) { 
        this.userId = userId; 
    }
    
    public String getSummary() { 
        return summary; 
    }
    
    public void setSummary(String summary) { 
        this.summary = summary; 
    }
    
    public String getSkills() { 
        return skills; 
    }
    
    public void setSkills(String skills) { 
        this.skills = skills; 
    }
    
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
}