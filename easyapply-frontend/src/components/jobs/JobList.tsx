import React from 'react';
import JobCard from './JobCard';
import { JobPosting } from '../../types';

interface JobListProps {
  jobs: JobPosting[];
  title?: string;
  description?: string;
  emptyMessage?: string;
}

const JobList: React.FC<JobListProps> = ({ 
  jobs, 
  title = "Offres d'emploi",
  description,
  emptyMessage = "Aucune offre d'emploi disponible pour le moment." 
}) => {
  return (
    <div>
      {title && <h2 className="text-2xl font-semibold mb-2">{title}</h2>}
      {description && <p className="text-gray-600 mb-6">{description}</p>}

      {jobs.length > 0 ? (
        <div className="space-y-4">
          {jobs.map((job) => (
            <JobCard key={job.id} job={job} />
          ))}
        </div>
      ) : (
        <div className="text-center py-10">
          <p className="text-gray-500">{emptyMessage}</p>
        </div>
      )}
    </div>
  );
};

export default JobList;