package com.easyapply.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.easyapply.Repository.CandidatRepository;
import com.easyapply.Repository.CompanyRepository;
import com.easyapply.Repository.*;
import com.easyapply.DTO.LoginRequest;
import com.easyapply.entity.Candidat;
import com.easyapply.entity.Entreprise;
import com.easyapply.entity.*;
import com.easyapply.DTO.LoginResponse;
import com.easyapply.Service.JwtService;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpSession;


@Service
public class AuthService {
    @Autowired
    private CandidatRepository candidatRepository;
    
    @Autowired
    private CompanyRepository entrepriseRepository;
    
    @Autowired
    private AdminRepository adminRepository;
   
     @Autowired
    private HttpSession httpSession; // Add this line

    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse authenticate(LoginRequest request) {
        // Try Admin first
        Administrateur admin = adminRepository.findByEmail(request.getEmail()).orElse(null);
        if (admin != null && passwordEncoder.matches(request.getMotDePasse(), admin.getMotDePasse())) {
            return createLoginResponse(admin.getId(), admin.getEmail(), "ADMIN");
        }

        // Try Candidat
        Candidat candidat = candidatRepository.findByEmail(request.getEmail()).orElse(null);
        if (candidat != null && passwordEncoder.matches(request.getMotDePasse(), candidat.getMotDePasse())) {
            return createLoginResponse(candidat.getId(), candidat.getEmail(), "CANDIDAT");
        }

        // Try Entreprise
        Entreprise entreprise = entrepriseRepository.findByEmail(request.getEmail()).orElse(null);
        if (entreprise != null && passwordEncoder.matches(request.getMotDePasse(), entreprise.getMotDePasse())) {
            return createLoginResponse(entreprise.getId(), entreprise.getEmail(), "ENTREPRISE");
        }

        throw new RuntimeException("Email ou mot de passe incorrect");
    }

    private LoginResponse createLoginResponse(Long id, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        claims.put("email", email);
        claims.put("role", role);
        
        String token = jwtService.generateToken(claims, email);
        httpSession.setAttribute("USER_ID", id);
        httpSession.setAttribute("USER_ROLE", role);
        httpSession.setAttribute("USER_EMAIL", email);
        
        return new LoginResponse(token, role,id);
        
    }
   
    // Add method to check session
    public boolean isAuthenticated() {
        return httpSession.getAttribute("USER_ID") != null;
    }

    // Add method to get current user ID
    public Long getCurrentUserId() {
        return (Long) httpSession.getAttribute("USER_ID");
    }

    // Add method to get current user role
    public String getCurrentUserRole() {
        return (String) httpSession.getAttribute("USER_ROLE");
    }
    
}
