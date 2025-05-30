package com.easyapply.controller;

import com.easyapply.dto.CVRequest;
import com.easyapply.entity.CV;
import com.easyapply.entity.User;
import com.easyapply.repository.CVRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/api/cv")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CVController {

    @Autowired
    private CVRepository cvRepository;

    @PostMapping("/save")
    public ResponseEntity<?> saveCV(@RequestBody CVRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Non connecté"));
        }

        CV cv = new CV();
        cv.setUser(user);
        cv.setFileName("generated-from-form.pdf");
        cv.setFilePath("N/A");
        cv.setIsPrimary(true);
        cv.setIsAnalyzed(false);
        cv.setExtractedSkills(String.join(", ", request.getCompetences()));
        cv.setAnalysisStatus(CV.AnalysisStatus.COMPLETED);
        cv.setExtractionConfidence(1.0);

        cvRepository.save(cv);
        return ResponseEntity.ok(Map.of("message", "CV sauvegardé avec succès"));
    }
}