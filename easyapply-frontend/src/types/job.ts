export interface JobPosting {
  id: string;
  title: string;
  company: string;
  location: string;
  salary: string;
  description: string;
  type: string;
  requirements: string[];
  postedDate: string;
  deadline: string;
}

export interface JobFilters {
  location: string[];
  contractType: string[];
  experience: string[];
  searchTerm: string;
}