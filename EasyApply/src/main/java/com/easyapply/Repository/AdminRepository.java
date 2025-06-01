package com.easyapply.Repository;
import org.springframework.stereotype.Repository;
import com.easyapply.entity.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; 
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
@Repository


public interface AdminRepository extends JpaRepository<Administrateur, Long> {
    Optional<Administrateur> findByEmail(String email);
    
    @Transactional
    @Modifying
    @Query("UPDATE Administrateur a SET a.motDePasse = :newPassword WHERE a.id = :id")
    void updatePassword(@Param("id") Long id, @Param("newPassword") String newPassword);
    
    @Query("SELECT a FROM Administrateur a WHERE a.id = :id")
    Optional<Administrateur> findById(@Param("id") Long id);

}
