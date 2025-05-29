package com.easyapply.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easyapply.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Méthodes de base héritées de JpaRepository :
    // - save(User user)
    // - findById(Long id)
    // - findAll()
    // - deleteById(Long id)
    // - count()
    
    // ✅ MÉTHODES PERSONNALISÉES POUR L'AUTHENTIFICATION
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    // ✅ MÉTHODES POUR LA GESTION DES UTILISATEURS
    List<User> findByRole(User.Role role);
    
    List<User> findByIsActiveTrue();
    
    List<User> findByIsActiveFalse();
    
    // ✅ MÉTHODES POUR LINKEDIN OAUTH
    Optional<User> findByLinkedinToken(String linkedinToken);
    
    // ✅ REQUÊTES PERSONNALISÉES AVEC @Query
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    List<User> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT u FROM User u JOIN u.applications a WHERE a.status = :status")
    List<User> findUsersWithApplicationStatus(@Param("status") com.easyapply.entity.Application.ApplicationStatus status);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isActive = true")
    Long countActiveUsersByRole(@Param("role") User.Role role);
    
    // ✅ STATISTIQUES UTILISATEURS - CORRIGÉ
    @Query("SELECT u FROM User u WHERE u.createdAt >= :thirtyDaysAgo")
    List<User> findRecentUsers(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);
}