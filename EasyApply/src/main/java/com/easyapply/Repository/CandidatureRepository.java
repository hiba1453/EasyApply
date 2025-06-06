package com.easyapply.Repository;

import com.easyapply.entity.Candidature;
import com.easyapply.entity.CandidatureId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CandidatureRepository extends JpaRepository<Candidature, CandidatureId> {
    Optional<Candidature> findByCandidatIdAndOffreId(Long candidatId, Long offreId);

    Optional<Candidature> findById(CandidatureId id);
    void deleteById(CandidatureId id);
    boolean existsById(CandidatureId id);
   
   }