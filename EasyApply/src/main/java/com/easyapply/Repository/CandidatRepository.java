package com.easyapply.Repository;
import com.easyapply.entity.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    
    // Tu peux ajouter des méthodes personnalisées ici si besoin
}
