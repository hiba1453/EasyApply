import React, { useState } from 'react';
import { Plus, Search } from 'lucide-react';
import Button from '../../../components/ui/Button';
import JobForm from '../../../components/jobs/JobForm';
import Card from '../../../components/ui/Card';
import JobCard from '../../../components/jobs/JobCard';
import { mockJobs } from '../../../utils/mockData';

const CompanyJobs: React.FC = () => {
  const [isCreating, setIsCreating] = useState(false);
  const [jobs, setJobs] = useState(mockJobs);

  const handleCreateJob = (jobData: any) => {
    // In a real app, you would send this to the server
    const newJob = {
      ...jobData,
      id: (jobs.length + 1).toString(),
      postedDate: new Date().toISOString().split('T')[0],
    };
    
    setJobs([newJob, ...jobs]);
    setIsCreating(false);
  };

  return (
    <div>
      <div className="flex flex-col md:flex-row md:items-center justify-between mb-8">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 mb-2">Mes offres d'emploi</h1>
          <p className="text-gray-600">
            Gérez vos offres d'emploi et suivez les candidatures reçues
          </p>
        </div>
        
        {!isCreating && (
          <Button 
            onClick={() => setIsCreating(true)}
            className="mt-4 md:mt-0"
          >
            <Plus className="w-5 h-5 mr-2" />
            Nouvelle offre
          </Button>
        )}
      </div>
      
      {isCreating ? (
        <div className="mb-8">
          <JobForm onSubmit={handleCreateJob} />
          <div className="mt-4 flex justify-end">
            <Button 
              variant="secondary" 
              onClick={() => setIsCreating(false)}
            >
              Annuler
            </Button>
          </div>
        </div>
      ) : (
        <div className="space-y-6">
          <div className="relative">
            <input
              type="text"
              placeholder="Rechercher dans vos offres..."
              className="pl-10 pr-4 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 w-full md:w-96"
            />
            <Search className="absolute left-3 top-2.5 w-5 h-5 text-gray-400" />
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <Card>
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold">Offres actives</h3>
                <span className="bg-primary-100 text-primary-700 px-2.5 py-1 rounded-full text-xs font-medium">
                  {jobs.length}
                </span>
              </div>
              
              <div className="space-y-4">
                {jobs.map(job => (
                  <JobCard key={job.id} job={job} showActions={false} />
                ))}
              </div>
            </Card>
            
            <Card>
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold">Statistiques</h3>
              </div>
              
              <div className="grid grid-cols-2 gap-4 mb-6">
                <div className="bg-gray-50 rounded-lg p-4">
                  <div className="text-3xl font-bold text-primary-600 mb-1">32</div>
                  <div className="text-gray-600 text-sm">Candidatures reçues</div>
                </div>
                
                <div className="bg-gray-50 rounded-lg p-4">
                  <div className="text-3xl font-bold text-green-600 mb-1">5</div>
                  <div className="text-gray-600 text-sm">Entretiens planifiés</div>
                </div>
                
                <div className="bg-gray-50 rounded-lg p-4">
                  <div className="text-3xl font-bold text-blue-600 mb-1">152</div>
                  <div className="text-gray-600 text-sm">Vues totales</div>
                </div>
                
                <div className="bg-gray-50 rounded-lg p-4">
                  <div className="text-3xl font-bold text-purple-600 mb-1">21%</div>
                  <div className="text-gray-600 text-sm">Taux de conversion</div>
                </div>
              </div>
              
              <h4 className="font-medium text-gray-800 mb-3">Activité récente</h4>
              <div className="space-y-3">
                <div className="flex items-start">
                  <div className="w-2 h-2 rounded-full bg-green-500 mt-1.5 mr-2"></div>
                  <div>
                    <p className="text-gray-800 text-sm">
                      Nouvelle candidature pour Développeur Frontend React
                    </p>
                    <p className="text-gray-500 text-xs">Il y a 1 heure</p>
                  </div>
                </div>
                
                <div className="flex items-start">
                  <div className="w-2 h-2 rounded-full bg-blue-500 mt-1.5 mr-2"></div>
                  <div>
                    <p className="text-gray-800 text-sm">
                      15 nouvelles vues sur Data Scientist
                    </p>
                    <p className="text-gray-500 text-xs">Il y a 3 heures</p>
                  </div>
                </div>
                
                <div className="flex items-start">
                  <div className="w-2 h-2 rounded-full bg-yellow-500 mt-1.5 mr-2"></div>
                  <div>
                    <p className="text-gray-800 text-sm">
                      L'offre UX/UI Designer expire dans 7 jours
                    </p>
                    <p className="text-gray-500 text-xs">Il y a 5 heures</p>
                  </div>
                </div>
              </div>
            </Card>
          </div>
        </div>
      )}
    </div>
  );
};

export default CompanyJobs;