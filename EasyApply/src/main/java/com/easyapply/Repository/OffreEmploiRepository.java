package com.easyapply.Repository;

import com.easyapply.entity.OffreEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OffreEmploiRepository extends JpaRepository<OffreEmploi, Long> {
    List<OffreEmploi> findByLieu(String lieu);
    List<OffreEmploi> findByTypeContrat(String typeContrat);
    List<OffreEmploi> findByNiveauExperience(String niveauExperience);
    

}
