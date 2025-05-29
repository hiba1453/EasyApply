package com.easyapply.controller;

import com.easyapply.service.CVAnalysisService;
import com.easyapply.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/api/cv")
@CrossOrigin(origins = "*")
@Tag(name = "CV Analysis", description = "Analyse réelle des CV avec extraction automatique")
public class CVController {

    @Autowired
    private CVAnalysisService cvAnalysisService;
    
    @Autowired
    private AuthService authService;

    @PostMapping("/upload-analyze")
    @Operation(summary = "Upload et analyse CV", 
               description = "Uploader un CV PDF et extraire automatiquement les informations")
    public ResponseEntity<?> uploadAndAnalyzeCV(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "false") boolean updateProfile,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        try {
            // Validation du fichier
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Fichier requis"));
            }
            
            if (!file.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Seuls les fichiers PDF sont autorisés"));
            }
            
            if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Fichier trop volumineux (max 10MB)"));
            }

            // 1. Extraction du texte du PDF
            String cvText = cvAnalysisService.extractTextFromPDF(file);
            
            if (cvText.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Impossible d'extraire le texte du PDF"));
            }

            // 2. Analyse complète du contenu
            Map<String, Object> analysis = cvAnalysisService.analyzeCVContent(cvText);
            
            // 3. Génération des recommandations d'offres
            var jobRecommendations = cvAnalysisService.generateJobRecommendations(analysis);
            
            // 4. Mise à jour du profil utilisateur si demandé et token valide
            if (updateProfile && authHeader != null) {
                try {
                    String token = authHeader.replace("Bearer ", "");
                    if (authService.validateJwtToken(token)) {
                        Long userId = authService.getUserIdFromToken(token);
                        cvAnalysisService.updateUserProfileFromCV(userId, analysis);
                    }
                } catch (Exception e) {
                    // Continuer même si la mise à jour échoue
                    System.err.println("Erreur mise à jour profil: " + e.getMessage());
                }
            }

            // 5. Réponse complète avec toutes les informations extraites
            return ResponseEntity.ok(Map.of(
                "message", "CV analysé avec succès !",
                "fileName", file.getOriginalFilename(),
                "fileSize", file.getSize(),
                "extractedText", cvText.length() > 500 ? cvText.substring(0, 500) + "..." : cvText,
                "analysis", Map.of(
                    "technicalSkills", analysis.get("technicalSkills"),
                    "experienceLevel", analysis.get("experienceLevel"),
                    "experiences", analysis.get("experiences"),
                    "educations", analysis.get("educations"),
                    "languages", analysis.get("languages"),
                    "domainScores", analysis.get("domainScores"),
                    "recommendedPositions", analysis.get("recommendedPositions")
                ),
                "jobRecommendations", jobRecommendations,
                "profileUpdated", updateProfile && authHeader != null,
                "nextSteps", java.util.Arrays.asList(
                    "Vérifiez les compétences extraites",
                    "Consultez les recommandations de postes",
                    "Explorez les offres correspondantes",
                    "Complétez votre profil si nécessaire"
                )
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de l'analyse du CV: " + e.getMessage()));
        }
    }

    @PostMapping("/analyze-text")
    @Operation(summary = "Analyser texte CV", 
               description = "Analyser directement le texte d'un CV (pour tests)")
    public ResponseEntity<?> analyzeText(@RequestBody Map<String, String> request) {
        try {
            String cvText = request.get("text");
            if (cvText == null || cvText.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Texte requis"));
            }

            // Analyse du texte
            Map<String, Object> analysis = cvAnalysisService.analyzeCVContent(cvText);
            
            // Génération des recommandations
            var jobRecommendations = cvAnalysisService.generateJobRecommendations(analysis);

            return ResponseEntity.ok(Map.of(
                "message", "Texte analysé avec succès !",
                "analysis", analysis,
                "jobRecommendations", jobRecommendations
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de l'analyse: " + e.getMessage()));
        }
    }

    @PostMapping("/extract-skills")
    @Operation(summary = "Extraire compétences uniquement", 
               description = "Extraire seulement les compétences techniques d'un CV")
    public ResponseEntity<?> extractSkills(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty() || !file.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Fichier PDF requis"));
            }

            String cvText = cvAnalysisService.extractTextFromPDF(file);
            Map<String, Object> analysis = cvAnalysisService.analyzeCVContent(cvText);

            return ResponseEntity.ok(Map.of(
                "fileName", file.getOriginalFilename(),
                "technicalSkills", analysis.get("technicalSkills"),
                "domainScores", analysis.get("domainScores"),
                "recommendedPositions", analysis.get("recommendedPositions")
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de l'extraction: " + e.getMessage()));
        }
    }
}