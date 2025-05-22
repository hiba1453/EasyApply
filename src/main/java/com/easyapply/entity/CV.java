package com.easyapply.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cvs")
public class CV {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "file_name", nullable = false)
    private String fileName;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "is_primary")
    private Boolean isPrimary = false;
    
    @Column(name = "is_analyzed")
    private Boolean isAnalyzed = false;
    
    @CreationTimestamp
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructeurs
    public CV() {}
    
    public CV(Long userId, String fileName, String filePath) {
        this.userId = userId;
        this.fileName = fileName;
        this.filePath = filePath;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    
    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
    
    public Boolean getIsAnalyzed() { return isAnalyzed; }
    public void setIsAnalyzed(Boolean isAnalyzed) { this.isAnalyzed = isAnalyzed; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}