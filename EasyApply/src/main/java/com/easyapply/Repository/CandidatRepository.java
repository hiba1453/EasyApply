package com.easyapply.Repository;
<<<<<<< HEAD
import com.easyapply.entity.Candidat;
=======

>>>>>>> 8463d1990a4eeebad083d2a05be791f54e41874b
import org.springframework.data.jpa.repository.JpaRepository;

import com.easyapply.entity.Candidat;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    
    // Tu peux ajouter des méthodes personnalisées ici si besoin
}
