package com.easyapply.service;

import com.easyapply.entity.CV;
import com.easyapply.entity.User;
import com.easyapply.repository.CVRepository;
import com.easyapply.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class CVService {

    @Autowired
    private CVRepository cvRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CVAnalysisService cvAnalysisService;

    @Value("${app.upload.dir:uploads/cvs}")
    private String uploadDir;

    // ✅ SAUVEGARDER UN CV RÉELLEMENT
    public CV saveCV(Long userId, MultipartFile file, boolean isPrimary) throws IOException {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Créer le répertoire s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom de fichier unique
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        Path filePath = uploadPath.resolve(uniqueFileName);

        // Sauvegarder le fichier physiquement
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Si c'est le CV principal, désactiver les autres
        if (isPrimary) {
            cvRepository.findByUser_Id(userId).forEach(cv -> {
                cv.setIsPrimary(false);
                cvRepository.save(cv);
            });
        }

        // Créer l'entité CV
        CV cv = new CV(user, originalFileName, filePath.toString());
        cv.setFileSize(file.getSize());
        cv.setIsPrimary(isPrimary);
        cv.setAnalysisStatus(CV.AnalysisStatus.PENDING);

        CV savedCV = cvRepository.save(cv);

        // Lancer l'analyse en arrière-plan (async)
        analyzeCV(savedCV);

        return savedCV;
    }

    // ✅ RÉCUPÉRER LES CV D'UN UTILISATEUR
    public List<Map<String, Object>> getUserCVs(Long userId) {
        List<CV> cvs = cvRepository.findByUser_IdOrderByUploadedAtDesc(userId);
        
        return cvs.stream().map(cv -> {
            Map<String, Object> cvMap = new HashMap<>();
            cvMap.put("id", cv.getId());
            cvMap.put("fileName", cv.getFileName());
            cvMap.put("fileSize", cv.getFileSize());
            cvMap.put("isPrimary", cv.getIsPrimary());
            cvMap.put("uploadedAt", cv.getUploadedAt());
            cvMap.put("analysisStatus", cv.getAnalysisStatus());
            cvMap.put("extractedSkills", cv.getExtractedSkills());
            cvMap.put("extractionConfidence", cv.getExtractionConfidence());
            cvMap.put("isAnalyzed", cv.getIsAnalyzed());
            return cvMap;
        }).collect(Collectors.toList());
    }

    // ✅ RÉCUPÉRER LE CV PRINCIPAL
    public Optional<Map<String, Object>> getPrimaryCV(Long userId) {
        return cvRepository.findByUser_IdAndIsPrimaryTrue(userId)
            .map(cv -> {
                Map<String, Object> cvMap = new HashMap<>();
                cvMap.put("id", cv.getId());
                cvMap.put("fileName", cv.getFileName());
                cvMap.put("fileSize", cv.getFileSize());
                cvMap.put("isPrimary", cv.getIsPrimary());
                cvMap.put("uploadedAt", cv.getUploadedAt());
                cvMap.put("analysisStatus", cv.getAnalysisStatus());
                cvMap.put("extractedSkills", cv.getExtractedSkills());
                cvMap.put("extractionConfidence", cv.getExtractionConfidence());
                return cvMap;
            });
    }

    // ✅ ANALYSER UN CV RÉELLEMENT
    private void analyzeCV(CV cv) {
        try {
            cv.setAnalysisStatus(CV.AnalysisStatus.IN_PROGRESS);
            cvRepository.save(cv);

            // Lire le fichier PDF
            Path filePath = Paths.get(cv.getFilePath());
            if (!Files.exists(filePath)) {
                throw new RuntimeException("Fichier CV non trouvé: " + cv.getFilePath());
            }

            // Analyser avec le service d'analyse
            String extractedText = cvAnalysisService.extractTextFromFile(filePath);
            Map<String, Object> analysis = cvAnalysisService.analyzeCVContent(extractedText);

            // Sauvegarder les résultats
            cv.setExtractedSkills(analysis.get("technicalSkills").toString());
            cv.setExtractionConfidence(0.85); // Ou calculer basé sur l'analyse
            cv.setAnalysisStatus(CV.AnalysisStatus.COMPLETED);
            cv.setIsAnalyzed(true);

            cvRepository.save(cv);

            // Mettre à jour le profil utilisateur si c'est le CV principal
            if (cv.getIsPrimary()) {
                cvAnalysisService.updateUserProfileFromCV(cv.getUser().getId(), analysis);
            }

        } catch (Exception e) {
            cv.setAnalysisStatus(CV.AnalysisStatus.FAILED);
            cvRepository.save(cv);
            System.err.println("Erreur lors de l'analyse du CV: " + e.getMessage());
        }
    }

    // ✅ DÉFINIR UN CV COMME PRINCIPAL
    public CV setPrimaryCV(Long userId, Long cvId) {
        // Désactiver tous les CV principaux de l'utilisateur
        cvRepository.findByUser_Id(userId).forEach(cv -> {
            cv.setIsPrimary(false);
            cvRepository.save(cv);
        });

        // Activer le CV demandé
        CV cv = cvRepository.findById(cvId)
            .orElseThrow(() -> new RuntimeException("CV non trouvé"));
            
        if (!cv.getUser().getId().equals(userId)) {
            throw new RuntimeException("CV ne appartient pas à cet utilisateur");
        }

        cv.setIsPrimary(true);
        return cvRepository.save(cv);
    }

    // ✅ SUPPRIMER UN CV
    public void deleteCV(Long userId, Long cvId) throws IOException {
        CV cv = cvRepository.findById(cvId)
            .orElseThrow(() -> new RuntimeException("CV non trouvé"));
            
        if (!cv.getUser().getId().equals(userId)) {
            throw new RuntimeException("CV ne appartient pas à cet utilisateur");
        }

        // Supprimer le fichier physique
        Path filePath = Paths.get(cv.getFilePath());
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // Supprimer de la base de données
        cvRepository.delete(cv);
    }

    // ✅ OBTENIR LES STATISTIQUES CV POUR UN UTILISATEUR
    public Map<String, Object> getCVStats(Long userId) {
        List<CV> cvs = cvRepository.findByUser_Id(userId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCVs", cvs.size());
        stats.put("analyzedCVs", cvs.stream().filter(CV::getIsAnalyzed).count());
        stats.put("primaryCV", cvs.stream().filter(CV::getIsPrimary).findFirst().orElse(null));
        
        // Statistiques par statut d'analyse
        Map<String, Long> analysisByStatus = cvs.stream()
            .collect(Collectors.groupingBy(
                cv -> cv.getAnalysisStatus().toString(),
                Collectors.counting()
            ));
        stats.put("analysisByStatus", analysisByStatus);
        
        return stats;
    }
}