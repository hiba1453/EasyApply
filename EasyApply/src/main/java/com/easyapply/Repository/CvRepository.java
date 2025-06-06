package com.easyapply.Repository;

import com.easyapply.entity.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CvRepository extends JpaRepository<CV, Long> {
    CV findById(long id);
   
    boolean existsByCandidatId(long candidatId);
    boolean existsByNomFichier(String nomFichier);
    boolean existsById(long id);
    Optional <CV> findByCandidatId(long candidatId);
    
  }