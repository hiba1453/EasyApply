import { JobPosting, JobFilters } from '../types/job.ts';

const API_URL = 'http://localhost:8090/offres';

export const jobService = {
  async getJobs(filters?: JobFilters): Promise<JobPosting[]> {
    const token = localStorage.getItem('token');
    const queryParams = new URLSearchParams();
    
    if (filters) {
      if (filters.searchTerm) queryParams.append('search', filters.searchTerm);
      if (filters.location.length) queryParams.append('locations', filters.location.join(','));
      if (filters.contractType.length) queryParams.append('types', filters.contractType.join(','));
      if (filters.experience.length) queryParams.append('experience', filters.experience.join(','));
    }

    const response = await fetch(`${API_URL}/jobs?${queryParams}`, {
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      }
    });

    if (!response.ok) {
      throw new Error('Failed to fetch jobs');
    }

    return response.json();
  }
};