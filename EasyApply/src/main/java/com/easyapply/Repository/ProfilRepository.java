package com.easyapply.Repository;
import org.springframework.stereotype.Repository;
import com.easyapply.entity.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


@Repository


public interface ProfilRepository extends JpaRepository<Profil, Long> {
   
    Optional<Profil> findById(Long id);
   

    
    // Additional methods can be defined here if needed

}
