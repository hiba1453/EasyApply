import { useState, useEffect } from 'react';
import { JobPosting, JobFilters } from '../types/job.ts';
import { jobService } from '../services/jobService.js';

interface UseJobsReturn {
  jobs: JobPosting[];
  loading: boolean;
  error: string | null;
}

export const useJobs = (filters?: JobFilters): UseJobsReturn => {
  const [jobs, setJobs] = useState<JobPosting[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchJobs = async () => {
      try {
        setLoading(true);
        const data = await jobService.getJobs(filters);
        setJobs(data);
        setError(null);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'An error occurred');
      } finally {
        setLoading(false);
      }
    };

    fetchJobs();
  }, [filters]);

  return { jobs, loading, error };
};