export interface User {
  id: string;
  name: string;
  email: string;
  type: 'candidate' | 'company';
}

export interface Entreprise {
  id: number;
  nom: string;
  email: string;
  secteur: string;
  description: string;
}

export interface JobPosting {
  id: number;
  titre: string;
  description: string;
  motsCles: string;
  datePublication: string;
  dateExpiration: string;
  lieu: string;
  salaire: string;
  typeContrat: string;
  niveauExperience: string;
  entreprise: Entreprise;
  candidatures: any[];
  recommandations: any[];
}

export interface Application {
  id: string;
  jobId: string;
  status: 'pending' | 'reviewed' | 'accepted' | 'rejected';
  appliedDate: string;
}

export interface Company {
  id: string;
  name: string;
  logo: string;
  industry: string;
  size: string;
  description: string;
}

export interface Resume {
  id: string;
  userId: string;
  fullName: string;
  jobTitle: string;
  email: string;
  location: string;
  experience: {
    title: string;
    company: string;
    location: string;
    startDate: string;
    endDate: string;
    description: string;
  }[];
  education: {
    institution: string;
    degree: string;
    field: string;
    startDate: string;
    endDate: string;
  }[];
  skills: string[];
  languages: {
    name: string;
    level: string;
  }[];
}

export interface ResumeEducation {
  institution: string;
  degree: string;
  field: string;
  startDate: string;
  endDate: string;
}

export interface ResumeExperience {
  company: string;
  title: string;
  location: string;
  startDate: string;
  endDate: string;
  description: string;
}

export interface ResumeLanguage {
  name: string;
  level: string;
}