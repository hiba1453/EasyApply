package com.easyapply.Service;

import com.easyapply.Repository.OffreEmploiRepository;
import com.easyapply.entity.OffreEmploi;
import com.easyapply.DTO.OffreEmploiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OffreEmploiService {
    
    @Autowired
    private OffreEmploiRepository offreEmploiRepository;

    public List<OffreEmploi> getAllOffres() {
        return offreEmploiRepository.findAll();
    }

    public OffreEmploiResponse getOffreById(Long id) {
        OffreEmploi offre = offreEmploiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée"));
        return convertToDTO(offre);
    }

   
    private OffreEmploiResponse convertToDTO(OffreEmploi offre) {
        OffreEmploiResponse response = new OffreEmploiResponse();
        response.setId(offre.getId());
        response.setTitre(offre.getTitre());
        response.setDescription(offre.getDescription());
        response.setMotsCles(offre.getMotsCles());
        response.setDatePublication(offre.getDatePublication());
        response.setDateExpiration(offre.getDateExpiration());
        response.setLieu(offre.getLieu());
        response.setTypeContrat(offre.getTypeContrat());
        response.setSalaire(offre.getSalaire());
        
        // If entreprise is not null, get its name
        if (offre.getEntreprise() != null) {
            response.setEntrepriseNom(offre.getEntreprise().getNom());
        }
        
        response.setNiveauExperience(offre.getNiveauExperience());
        return response;
    }
}