package com.easyapply.service;

import com.easyapply.entity.CV;
import com.easyapply.entity.User;
import com.easyapply.entity.Profile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CVAnalysisService {

    @Autowired
    private UserService userService;

    // ✅ EXTRACTION TEXTE DU PDF
    public String extractTextFromPDF(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    // ✅ ANALYSE COMPLÈTE DU CV
    public Map<String, Object> analyzeCVContent(String cvText) {
        Map<String, Object> analysis = new HashMap<>();
        
        // 1. Extraction des compétences techniques
        Set<String> technicalSkills = extractTechnicalSkills(cvText);
        analysis.put("technicalSkills", technicalSkills);
        
        // 2. Extraction des expériences
        List<String> experiences = extractExperiences(cvText);
        analysis.put("experiences", experiences);
        
        // 3. Extraction des formations
        List<String> educations = extractEducations(cvText);
        analysis.put("educations", educations);
        
        // 4. Extraction des langues
        Set<String> languages = extractLanguages(cvText);
        analysis.put("languages", languages);
        
        // 5. Calcul du niveau d'expérience
        int experienceLevel = calculateExperienceLevel(experiences);
        analysis.put("experienceLevel", experienceLevel);
        
        // 6. Score de correspondance par domaine
        Map<String, Double> domainScores = calculateDomainScores(technicalSkills);
        analysis.put("domainScores", domainScores);
        
        // 7. Recommandations de postes
        List<String> recommendedPositions = generatePositionRecommendations(technicalSkills, experienceLevel);
        analysis.put("recommendedPositions", recommendedPositions);
        
        return analysis;
    }

    // ✅ EXTRACTION COMPÉTENCES TECHNIQUES
    private Set<String> extractTechnicalSkills(String text) {
        Set<String> skills = new HashSet<>();
        String lowerText = text.toLowerCase();
        
        // Compétences de programmation
        String[] programmingSkills = {
            "java", "python", "javascript", "typescript", "c#", "c++", "php", "ruby", "go", "rust",
            "spring", "spring boot", "react", "angular", "vue", "node.js", "express",
            "hibernate", "jpa", "mybatis", "django", "flask", "laravel", "symfony",
            "mysql", "postgresql", "mongodb", "redis", "elasticsearch",
            "docker", "kubernetes", "aws", "azure", "gcp", "jenkins", "gitlab ci",
            "git", "maven", "gradle", "npm", "webpack", "babel",
            "rest api", "graphql", "microservices", "api rest", "web services"
        };
        
        for (String skill : programmingSkills) {
            if (lowerText.contains(skill.toLowerCase())) {
                skills.add(skill);
            }
        }
        
        // Compétences métier
        String[] businessSkills = {
            "gestion de projet", "scrum", "agile", "kanban", "devops",
            "analyse fonctionnelle", "uml", "merise", "conception",
            "tests unitaires", "tests d'intégration", "junit", "mockito",
            "sécurité", "oauth", "jwt", "ssl", "https"
        };
        
        for (String skill : businessSkills) {
            if (lowerText.contains(skill.toLowerCase())) {
                skills.add(skill);
            }
        }
        
        return skills;
    }

    // ✅ EXTRACTION EXPÉRIENCES PROFESSIONNELLES
    private List<String> extractExperiences(String text) {
        List<String> experiences = new ArrayList<>();
        
        // Patterns pour détecter les expériences
        Pattern[] experiencePatterns = {
            Pattern.compile("(?i)(\\d{4})\\s*[-–]\\s*(\\d{4}|présent|actuel)\\s*[:]?\\s*([^\\n]+)", Pattern.MULTILINE),
            Pattern.compile("(?i)(\\w+\\s+\\d{4})\\s*[-–]\\s*(\\w+\\s+\\d{4}|présent|actuel)\\s*[:]?\\s*([^\\n]+)", Pattern.MULTILINE),
            Pattern.compile("(?i)expérience\\s*professionnelle[^\\n]*\\n([^\\n]+)", Pattern.MULTILINE),
            Pattern.compile("(?i)(développeur|ingénieur|consultant|chef de projet|analyst|manager)[^\\n]*", Pattern.MULTILINE)
        };
        
        for (Pattern pattern : experiencePatterns) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String experience = matcher.group().trim();
                if (experience.length() > 10) { // Filtrer les matches trop courts
                    experiences.add(experience);
                }
            }
        }
        
        // Limiter à 10 expériences max
        return experiences.stream().distinct().limit(10).toList();
    }

    // ✅ EXTRACTION FORMATIONS
    private List<String> extractEducations(String text) {
        List<String> educations = new ArrayList<>();
        
        Pattern[] educationPatterns = {
            Pattern.compile("(?i)(\\d{4})\\s*[-–]\\s*(\\d{4})\\s*[:]?\\s*([^\\n]*(?:université|école|institut|master|licence|bts|dut|ingénieur)[^\\n]*)", Pattern.MULTILINE),
            Pattern.compile("(?i)(master|licence|bachelor|bts|dut|doctorat|phd|ingénieur)[^\\n]*", Pattern.MULTILINE),
            Pattern.compile("(?i)formation[^\\n]*\\n([^\\n]+)", Pattern.MULTILINE)
        };
        
        for (Pattern pattern : educationPatterns) {
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String education = matcher.group().trim();
                if (education.length() > 5) {
                    educations.add(education);
                }
            }
        }
        
        return educations.stream().distinct().limit(5).toList();
    }

    // ✅ EXTRACTION LANGUES
    private Set<String> extractLanguages(String text) {
        Set<String> languages = new HashSet<>();
        String lowerText = text.toLowerCase();
        
        String[] commonLanguages = {
            "français", "anglais", "espagnol", "allemand", "italien", "portugais",
            "arabe", "chinois", "japonais", "russe", "néerlandais"
        };
        
        for (String lang : commonLanguages) {
            if (lowerText.contains(lang)) {
                languages.add(lang);
            }
        }
        
        return languages;
    }

    // ✅ CALCUL NIVEAU D'EXPÉRIENCE
    private int calculateExperienceLevel(List<String> experiences) {
        // Analyser les dates dans les expériences pour estimer les années d'expérience
        int totalYears = 0;
        
        for (String exp : experiences) {
            // Extraire les années d'expérience depuis les patterns de dates
            Pattern yearPattern = Pattern.compile("(\\d{4})\\s*[-–]\\s*(\\d{4}|présent|actuel)");
            Matcher matcher = yearPattern.matcher(exp);
            
            if (matcher.find()) {
                int startYear = Integer.parseInt(matcher.group(1));
                int endYear = matcher.group(2).matches("\\d{4}") ? 
                    Integer.parseInt(matcher.group(2)) : 
                    java.time.Year.now().getValue();
                
                totalYears += Math.max(0, endYear - startYear);
            }
        }
        
        // Limiter à un maximum réaliste
        return Math.min(totalYears, 30);
    }

    // ✅ CALCUL SCORES PAR DOMAINE
    private Map<String, Double> calculateDomainScores(Set<String> skills) {
        Map<String, Double> domainScores = new HashMap<>();
        
        // Domaines techniques avec leurs compétences associées
        Map<String, Set<String>> domains = Map.of(
            "Backend Java", Set.of("java", "spring", "spring boot", "hibernate", "jpa", "maven", "gradle"),
            "Frontend Web", Set.of("javascript", "typescript", "react", "angular", "vue", "html", "css"),
            "Data Science", Set.of("python", "pandas", "numpy", "scikit-learn", "tensorflow", "pytorch"),
            "DevOps", Set.of("docker", "kubernetes", "aws", "azure", "jenkins", "gitlab ci", "terraform"),
            "Base de données", Set.of("mysql", "postgresql", "mongodb", "redis", "elasticsearch"),
            "Mobile", Set.of("android", "ios", "react native", "flutter", "swift", "kotlin")
        );
        
        for (Map.Entry<String, Set<String>> domain : domains.entrySet()) {
            long matchingSkills = domain.getValue().stream()
                .mapToInt(skill -> skills.contains(skill) ? 1 : 0)
                .sum();
            
            double score = (double) matchingSkills / domain.getValue().size();
            domainScores.put(domain.getKey(), score);
        }
        
        return domainScores;
    }

    // ✅ GÉNÉRATION RECOMMANDATIONS DE POSTES
    private List<String> generatePositionRecommendations(Set<String> skills, int experienceLevel) {
        List<String> positions = new ArrayList<>();
        
        // Recommandations basées sur les compétences
        if (skills.contains("java") && skills.contains("spring")) {
            if (experienceLevel >= 5) {
                positions.add("Architecte Java Senior");
                positions.add("Lead Developer Java");
            } else if (experienceLevel >= 2) {
                positions.add("Développeur Java Senior");
                positions.add("Ingénieur Logiciel Java");
            } else {
                positions.add("Développeur Java Junior");
            }
        }
        
        if (skills.contains("react") || skills.contains("angular")) {
            if (experienceLevel >= 3) {
                positions.add("Développeur Frontend Senior");
            } else {
                positions.add("Développeur Frontend");
            }
        }
        
        if (skills.contains("python") && skills.contains("django")) {
            positions.add("Développeur Python");
            positions.add("Ingénieur Backend Python");
        }
        
        if (skills.contains("docker") && skills.contains("kubernetes")) {
            positions.add("Ingénieur DevOps");
            positions.add("Site Reliability Engineer");
        }
        
        if (skills.contains("scrum") || skills.contains("agile")) {
            if (experienceLevel >= 5) {
                positions.add("Chef de Projet Technique");
                positions.add("Scrum Master Senior");
            }
        }
        
        // Postes génériques si pas de match spécifique
        if (positions.isEmpty()) {
            if (experienceLevel >= 3) {
                positions.add("Ingénieur Logiciel");
                positions.add("Consultant Technique");
            } else {
                positions.add("Développeur");
                positions.add("Analyste Programmeur");
            }
        }
        
        return positions.stream().distinct().limit(5).toList();
    }

    // ✅ MISE À JOUR PROFIL UTILISATEUR AVEC ANALYSE CV
    public void updateUserProfileFromCV(Long userId, Map<String, Object> cvAnalysis) {
        try {
            // Construire les données du profil
            Map<String, Object> profileUpdates = new HashMap<>();
            
            // Compétences
            Set<String> skills = (Set<String>) cvAnalysis.get("technicalSkills");
            if (skills != null && !skills.isEmpty()) {
                profileUpdates.put("skills", String.join(", ", skills));
            }
            
            // Expériences
            List<String> experiences = (List<String>) cvAnalysis.get("experiences");
            if (experiences != null && !experiences.isEmpty()) {
                profileUpdates.put("experiences", String.join("\n", experiences));
            }
            
            // Formations
            List<String> educations = (List<String>) cvAnalysis.get("educations");
            if (educations != null && !educations.isEmpty()) {
                profileUpdates.put("formations", String.join("\n", educations));
            }
            
            // Années d'expérience
            Integer experienceLevel = (Integer) cvAnalysis.get("experienceLevel");
            if (experienceLevel != null) {
                profileUpdates.put("yearsOfExperience", experienceLevel);
            }
            
            // Générer un résumé automatique
            List<String> recommendedPositions = (List<String>) cvAnalysis.get("recommendedPositions");
            if (recommendedPositions != null && !recommendedPositions.isEmpty()) {
                String summary = "Profil " + recommendedPositions.get(0) + 
                    " avec " + (experienceLevel != null ? experienceLevel : 0) + 
                    " ans d'expérience. Compétences principales : " + 
                    (skills != null ? String.join(", ", skills).substring(0, Math.min(100, String.join(", ", skills).length())) : "");
                profileUpdates.put("summary", summary);
            }
            
            // Mettre à jour le profil
            userService.updateUserProfile(userId, profileUpdates);
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du profil: " + e.getMessage());
        }
    }

    // ✅ GÉNÉRER RECOMMANDATIONS D'OFFRES BASÉES SUR L'ANALYSE CV
    public List<Map<String, Object>> generateJobRecommendations(Map<String, Object> cvAnalysis) {
        List<Map<String, Object>> recommendations = new ArrayList<>();
        
        Set<String> skills = (Set<String>) cvAnalysis.get("technicalSkills");
        Integer experienceLevel = (Integer) cvAnalysis.get("experienceLevel");
        Map<String, Double> domainScores = (Map<String, Double>) cvAnalysis.get("domainScores");
        
        // Générer des recommandations basées sur les scores de domaine
        if (domainScores != null) {
            for (Map.Entry<String, Double> domain : domainScores.entrySet()) {
                if (domain.getValue() > 0.3) { // Seuil de correspondance minimum
                    Map<String, Object> recommendation = new HashMap<>();
                    recommendation.put("domain", domain.getKey());
                    recommendation.put("matchScore", domain.getValue());
                    recommendation.put("reasoning", generateRecommendationReasoning(domain.getKey(), skills, experienceLevel));
                    recommendation.put("salaryRange", estimateSalaryRange(domain.getKey(), experienceLevel));
                    
                    recommendations.add(recommendation);
                }
            }
        }
        
        // Trier par score de correspondance décroissant
        recommendations.sort((r1, r2) -> 
            Double.compare((Double) r2.get("matchScore"), (Double) r1.get("matchScore")));
        
        return recommendations.stream().limit(5).toList();
    }
    
    private String generateRecommendationReasoning(String domain, Set<String> skills, Integer experienceLevel) {
        StringBuilder reasoning = new StringBuilder();
        reasoning.append("Correspondance avec le domaine ").append(domain);
        
        if (experienceLevel != null && experienceLevel > 0) {
            reasoning.append(" grâce à vos ").append(experienceLevel).append(" ans d'expérience");
        }
        
        if (skills != null && !skills.isEmpty()) {
            reasoning.append(" et vos compétences en : ").append(String.join(", ", skills));
        }
        
        return reasoning.toString();
    }
    
    private Map<String, Integer> estimateSalaryRange(String domain, Integer experienceLevel) {
        Map<String, Integer> salaryRange = new HashMap<>();
        
        // Salaires de base par domaine (en euros)
        Map<String, Integer> baseSalaries = Map.of(
            "Backend Java", 35000,
            "Frontend Web", 33000,
            "Data Science", 40000,
            "DevOps", 42000,
            "Base de données", 38000,
            "Mobile", 36000
        );
        
        int baseSalary = baseSalaries.getOrDefault(domain, 35000);
        
        // Ajustement selon l'expérience
        int experienceMultiplier = experienceLevel != null ? experienceLevel : 0;
        int minSalary = baseSalary + (experienceMultiplier * 3000);
        int maxSalary = minSalary + 15000;
        
        salaryRange.put("min", minSalary);
        salaryRange.put("max", maxSalary);
        
        return salaryRange;
    }
}