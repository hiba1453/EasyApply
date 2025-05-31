import React from 'react';
import { Application, JobPosting } from '../../types';
import Card from '../ui/Card';
import Tag from '../ui/Tag';

interface ApplicationListProps {
  applications: Application[];
  jobs: JobPosting[];
}

const ApplicationList: React.FC<ApplicationListProps> = ({ applications, jobs }) => {
  // Helper function to get job details by ID
  const getJobById = (jobId: string) => {
    return jobs.find(job => job.id === jobId);
  };

  // Helper function to get status color
  const getStatusColor = (status: Application['status']) => {
    switch (status) {
      case 'pending':
        return 'yellow';
      case 'reviewed':
        return 'blue';
      case 'accepted':
        return 'green';
      case 'rejected':
        return 'red';
      default:
        return 'gray';
    }
  };

  // Helper function to get status text in French
  const getStatusText = (status: Application['status']) => {
    switch (status) {
      case 'pending':
        return 'En attente';
      case 'reviewed':
        return 'Examinée';
      case 'accepted':
        return 'Acceptée';
      case 'rejected':
        return 'Refusée';
      default:
        return 'Inconnu';
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-semibold mb-6">Mes candidatures</h2>

      {applications.length > 0 ? (
        <div className="space-y-4">
          {applications.map(application => {
            const job = getJobById(application.jobId);
            if (!job) return null;

            return (
              <Card key={application.id} className="transition-all duration-200 hover:shadow-md">
                <div className="flex flex-col md:flex-row md:items-center">
                  <div className="flex-1">
                    <div className="flex flex-col md:flex-row md:items-center justify-between mb-2">
                      <h3 className="text-lg font-semibold text-gray-900">{job.title}</h3>
                      <Tag 
                        label={getStatusText(application.status)} 
                        color={getStatusColor(application.status)} 
                        className="md:ml-2 mt-1 md:mt-0"
                      />
                    </div>
                    <p className="text-primary-600 font-medium mb-1">{job.company}</p>
                    <p className="text-gray-600 text-sm mb-3">
                      Candidature envoyée le {new Date(application.appliedDate).toLocaleDateString('fr-FR', {
                        day: 'numeric',
                        month: 'long',
                        year: 'numeric'
                      })}
                    </p>
                    <div className="flex flex-wrap gap-2">
                      {job.tags.map((tag, index) => (
                        <Tag key={index} label={tag} color="gray" size="sm" />
                      ))}
                    </div>
                  </div>
                </div>
              </Card>
            );
          })}
        </div>
      ) : (
        <div className="text-center py-12">
          <div className="mx-auto w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mb-4">
            <svg
              width="40"
              height="40"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
              className="text-gray-400"
            >
              <rect width="18" height="18" x="3" y="4" rx="2" ry="2"></rect>
              <line x1="16" x2="16" y1="2" y2="6"></line>
              <line x1="8" x2="8" y1="2" y2="6"></line>
              <line x1="3" x2="21" y1="10" y2="10"></line>
              <path d="m9 16 2 2 4-4"></path>
            </svg>
          </div>
          <h3 className="text-lg font-medium text-gray-900 mb-1">Aucune candidature pour le moment</h3>
          <p className="text-gray-600 mb-6">
            Parcourez les offres d'emploi recommandées et commencez à postuler !
          </p>
          <a 
            href="/dashboard/candidate/jobs" 
            className="inline-flex items-center justify-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
          >
            Voir les offres recommandées
          </a>
        </div>
      )}
    </div>
  );
};

export default ApplicationList;