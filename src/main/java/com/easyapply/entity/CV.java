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
@Table(name = "cvs")
public class CV {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // ✅ REMPLACE userId par relation ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
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
    
    // ✅ NOUVEAUX CHAMPS selon diagramme
    @Column(name = "extracted_skills", columnDefinition = "TEXT")
    private String extractedSkills;
    
    @Column(name = "extraction_confidence")
    private Double extractionConfidence;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "analysis_status")
    private AnalysisStatus analysisStatus = AnalysisStatus.PENDING;
    
    @CreationTimestamp
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum AnalysisStatus {
        PENDING, IN_PROGRESS, COMPLETED, FAILED
    }
    
    // Constructeurs
    public CV() {}
    
    public CV(User user, String fileName, String filePath) {
        this.user = user;
        this.fileName = fileName;
        this.filePath = filePath;
    }
    
    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    // ✅ NOUVEAUX GETTERS/SETTERS POUR LA RELATION
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
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
    
    // ✅ NOUVEAUX GETTERS/SETTERS
    public String getExtractedSkills() { return extractedSkills; }
    public void setExtractedSkills(String extractedSkills) { this.extractedSkills = extractedSkills; }
    
    public Double getExtractionConfidence() { return extractionConfidence; }
    public void setExtractionConfidence(Double extractionConfidence) { this.extractionConfidence = extractionConfidence; }
    
    public AnalysisStatus getAnalysisStatus() { return analysisStatus; }
    public void setAnalysisStatus(AnalysisStatus analysisStatus) { this.analysisStatus = analysisStatus; }
    
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    // ✅ MÉTHODE DE COMPATIBILITÉ (pour ne pas casser le code existant)
    public Long getUserId() { 
        return user != null ? user.getId() : null; 
    }
    
    // ✅ MÉTHODES MÉTIER (selon diagramme de classes)
    public void extraireInformations() {
        // Cette méthode sera appelée par le microservice NLP
        this.setAnalysisStatus(AnalysisStatus.IN_PROGRESS);
        this.setIsAnalyzed(false);
        // Le microservice Python appellera setExtractedSkills() et setExtractionConfidence()
    }
    
    public void genererCV() {
        // Génération de CV personnalisé - à implémenter
        // Cette méthode créera un nouveau PDF basé sur le template
    }
    
    public void marquerCommePrincipal() {
        // Logique pour définir ce CV comme principal
        this.setIsPrimary(true);
    }
}