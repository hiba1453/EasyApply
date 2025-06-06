export interface JobPosting {
  id: string;
  titre: string;
  description: string;
  entreprise: {
    nom: string;
  };
  lieu?: string;
  salaire?: string;
  typeContrat?: string;
  niveauExperience?: string;
  motsCles?: string;
  datePublication: string;
  dateExpiration?: string;
  tags?: string[];
}

export interface JobFilters {
  location: string[];
  contractType: string[];
  experience: string[];
  searchTerm: string;
}