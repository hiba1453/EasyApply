package com.easyapply.service;

import com.easyapply.entity.User;
import com.easyapply.entity.Entreprise;
import com.easyapply.repository.UserRepository;
import com.easyapply.repository.EntrepriseRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EntrepriseRepository entrepriseRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ✅ AUTHENTIFICATION UTILISATEUR (CANDIDAT)
    public Map<String, Object> authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
        
        User user = userOpt.get();
        
        if (!user.getIsActive()) {
            throw new RuntimeException("Compte désactivé");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
        
        // Générer le token JWT
        String token = generateJwtToken(user.getId(), user.getEmail(), "USER", user.getRole().toString());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
            "id", user.getId(),
            "email", user.getEmail(),
            "firstName", user.getFirstName(),
            "lastName", user.getLastName(),
            "role", user.getRole().toString()
        ));
        
        return response;
    }

    // ✅ AUTHENTIFICATION ENTREPRISE
    public Map<String, Object> authenticateEntreprise(String email, String password) {
        Optional<Entreprise> entrepriseOpt = entrepriseRepository.findByEmail(email);
        
        if (entrepriseOpt.isEmpty()) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
        
        Entreprise entreprise = entrepriseOpt.get();
        
        if (!entreprise.getIsActive()) {
            throw new RuntimeException("Compte entreprise désactivé");
        }
        
        if (!passwordEncoder.matches(password, entreprise.getPassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
        
        // Générer le token JWT
        String token = generateJwtToken(entreprise.getId(), entreprise.getEmail(), "ENTREPRISE", "RECRUITER");
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("entreprise", Map.of(
            "id", entreprise.getId(),
            "email", entreprise.getEmail(),
            "nom", entreprise.getNom(),
            "secteur", entreprise.getSecteur() != null ? entreprise.getSecteur() : "Non spécifié",
            "role", "ENTREPRISE"
        ));
        
        return response;
    }

    // ✅ GÉNÉRATION TOKEN JWT
    public String generateJwtToken(Long userId, String email, String userType, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("userType", userType) // USER ou ENTREPRISE
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // ✅ VALIDATION TOKEN JWT
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ EXTRACTION INFORMATIONS DU TOKEN
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    public String getUserTypeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userType", String.class);
    }

    public String getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    // ✅ VÉRIFICATION EXPIRATION TOKEN
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // ✅ AUTHENTIFICATION LINKEDIN OAUTH
    public Map<String, Object> authenticateWithLinkedIn(String email, String linkedinToken, 
                                                       String firstName, String lastName) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        User user;
        if (existingUser.isPresent()) {
            // Utilisateur existe, mettre à jour le token LinkedIn
            user = existingUser.get();
            user.setLinkedinToken(linkedinToken);
            userRepository.save(user);
        } else {
            // Créer un nouveau utilisateur
            user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("linkedin-oauth")); // Mot de passe temporaire
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setLinkedinToken(linkedinToken);
            user.setRole(User.Role.CANDIDATE);
            user.setIsActive(true);
            user = userRepository.save(user);
        }
        
        // Générer le token JWT
        String token = generateJwtToken(user.getId(), user.getEmail(), "USER", user.getRole().toString());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
            "id", user.getId(),
            "email", user.getEmail(),
            "firstName", user.getFirstName(),
            "lastName", user.getLastName(),
            "role", user.getRole().toString(),
            "isNewUser", existingUser.isEmpty()
        ));
        
        return response;
    }

    // ✅ RÉCUPÉRATION UTILISATEUR DEPUIS TOKEN
    public Optional<User> getUserFromToken(String token) {
        try {
            String userType = getUserTypeFromToken(token);
            if (!"USER".equals(userType)) {
                return Optional.empty();
            }
            
            String email = getEmailFromToken(token);
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Entreprise> getEntrepriseFromToken(String token) {
        try {
            String userType = getUserTypeFromToken(token);
            if (!"ENTREPRISE".equals(userType)) {
                return Optional.empty();
            }
            
            String email = getEmailFromToken(token);
            return entrepriseRepository.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // ✅ CHANGEMENT DE MOT DE PASSE
    public void changeUserPassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void changeEntreprisePassword(Long entrepriseId, String oldPassword, String newPassword) {
        Entreprise entreprise = entrepriseRepository.findById(entrepriseId)
            .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));
        
        if (!passwordEncoder.matches(oldPassword, entreprise.getPassword())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }
        
        entreprise.setPassword(passwordEncoder.encode(newPassword));
        entrepriseRepository.save(entreprise);
    }

    // ✅ RÉINITIALISATION MOT DE PASSE (simulation)
    public void resetPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Générer un nouveau mot de passe temporaire
            String tempPassword = "temp" + System.currentTimeMillis();
            user.setPassword(passwordEncoder.encode(tempPassword));
            userRepository.save(user);
            
            // Dans une vraie implémentation, envoyer un email avec le nouveau mot de passe
            System.out.println("Nouveau mot de passe temporaire pour " + email + ": " + tempPassword);
        }
        // Ne pas révéler si l'email existe ou pas
    }
}