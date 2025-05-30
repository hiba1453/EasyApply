package com.easyapply.service;

import com.easyapply.entity.User;
import com.easyapply.entity.Profile;
import com.easyapply.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.time.LocalDateTime;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ GESTION DES UTILISATEURS
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ✅ INSCRIPTION UTILISATEUR
    public  User createUser(String email, String password, String firstName, String lastName) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // Créer le nouvel utilisateur
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(User.Role.CANDIDATE);
        user.setIsActive(true);

        

        // Créer un profil vide pour l'utilisateur
        Profile profile = new Profile(user);
        user.setProfile(profile);
        
        return userRepository.save(user);
    }

    // ✅ MISE À JOUR PROFIL
    public User updateUserProfile(Long userId, Map<String, Object> updates) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Mettre à jour les champs de base
        if (updates.containsKey("firstName")) {
            user.setFirstName((String) updates.get("firstName"));
        }
        if (updates.containsKey("lastName")) {
            user.setLastName((String) updates.get("lastName"));
        }
        if (updates.containsKey("phoneNumber")) {
            user.setPhoneNumber((String) updates.get("phoneNumber"));
        }

        // Mettre à jour le profil professionnel
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile(user);
            user.setProfile(profile);
        }

        if (updates.containsKey("summary")) {
            profile.setSummary((String) updates.get("summary"));
        }
        if (updates.containsKey("skills")) {
            profile.setSkills((String) updates.get("skills"));
        }
        if (updates.containsKey("yearsOfExperience")) {
            profile.setYearsOfExperience((Integer) updates.get("yearsOfExperience"));
        }
        if (updates.containsKey("preferredLocation")) {
            profile.setPreferredLocation((String) updates.get("preferredLocation"));
        }
        if (updates.containsKey("preferredSalary")) {
            profile.setPreferredSalary((Double) updates.get("preferredSalary"));
        }

        // Analyser le profil pour calculer la complétude
        profile.analyserProfil();

        return userRepository.save(user);
    }

    // ✅ AUTHENTIFICATION
    public boolean validateUserCredentials(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getIsActive() && passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    // ✅ GESTION LINKEDIN OAUTH - AMÉLIORÉE
    public User createUserFromLinkedIn(String email, String linkedinToken, 
                                      String firstName, String lastName) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        if (existingUser.isPresent()) {
            // Utilisateur existe, mettre à jour le token LinkedIn
            User user = existingUser.get();
            user.setLinkedinToken(linkedinToken);
            return userRepository.save(user);
        } else {
            // Créer un nouveau utilisateur
            User user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("linkedin-oauth-" + System.currentTimeMillis())); 
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setLinkedinToken(linkedinToken);
            user.setRole(User.Role.CANDIDATE);
            user.setIsActive(true);

            User savedUser = userRepository.save(user);

            // Créer un profil vide
            Profile profile = new Profile(savedUser);
            savedUser.setProfile(profile);
            
            return userRepository.save(savedUser);
        }
    }

    // ✅ STATISTIQUES UTILISATEUR
    public Map<String, Object> getUserStats(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Map<String, Object> stats = new HashMap<>();
        
        // Statistiques de base
        stats.put("totalApplications", user.getApplications().size());
        stats.put("totalCVs", user.getCvs().size());
        stats.put("totalRecommendations", user.getRecommendations().size());
        
        // Profil completion
        Profile profile = user.getProfile();
        if (profile != null) {
            stats.put("profileCompleteness", profile.getProfileCompleteness());
            stats.put("isProfileComplete", profile.isProfileComplete());
        } else {
            stats.put("profileCompleteness", 0.0);
            stats.put("isProfileComplete", false);
        }

        // Statistiques des candidatures par statut
        Map<String, Long> applicationStats = user.getApplications().stream()
            .collect(java.util.stream.Collectors.groupingBy(
                app -> app.getStatus().toString(),
                java.util.stream.Collectors.counting()
            ));
        stats.put("applicationsByStatus", applicationStats);

        return stats;
    }

    // ✅ DÉSACTIVATION/ACTIVATION COMPTE
    public User deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.setIsActive(false);
        return userRepository.save(user);
    }

    public User activateUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.setIsActive(true);
        return userRepository.save(user);
    }

    // ✅ RECHERCHE UTILISATEURS
    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    public List<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> getRecentUsers() {
        // Implémentation alternative en Java pour éviter les problèmes HQL
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return userRepository.findAll().stream()
            .filter(u -> u.getCreatedAt().isAfter(thirtyDaysAgo))
            .sorted((u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()))
            .toList();
    }

    // ✅ SUPPRESSION UTILISATEUR
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        userRepository.deleteById(userId);
    }
}