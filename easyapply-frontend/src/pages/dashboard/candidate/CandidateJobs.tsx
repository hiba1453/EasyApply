import React, { useState, useEffect } from 'react';
import { Search, Filter } from 'lucide-react';
import JobList from '../../../components/jobs/JobList';
import Card from '../../../components/ui/Card';

interface Job {
  id: number;
  titre: string;
  description: string;
  motsCles: string;
  datePublication: string;
  dateExpiration: string;
  lieu: string;
  typeContrat: string;
  salaire: string;
  niveauExperience: string;
  entreprise: {
    nom: string;
  };
}

const CandidateJobs: React.FC = () => {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchJobs = async () => {
      setLoading(true);
      try {
        const response = await fetch('http://localhost:8090/offres', { // Fixed endpoint to /api/offres
          method: 'GET',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
          },
        });

        console.log('Jobs response status:', response.status);
        console.log('Jobs response headers:', Object.fromEntries(response.headers.entries()));

        if (!response.ok) {
          let errorData: any = {};
          try {
            errorData = await response.json();
            console.log('Jobs error response body:', errorData);
          } catch (e) {
            console.error('Failed to parse jobs error response:', e);
          }
          throw new Error(errorData.error || `Erreur HTTP ${response.status}`);
        }

        const data = await response.json();
        console.log('Jobs success response body:', data);
        setJobs(data || []); // Ensure jobs is an array
      } catch (err) {
        console.error(
          'Error fetching jobs:',
          err && typeof err === 'object' && 'message' in err && typeof (err as any).message === 'string'
            ? (err as any).message
            : err
        );
        setError(
          err && typeof err === 'object' && 'message' in err && typeof (err as any).message === 'string'
            ? (err as any).message
            : 'Une erreur est survenue lors du chargement des offres'
        );
      } finally {
        setLoading(false);
      }
    };

    fetchJobs();
  }, []);

  return (
    <div>
      <div className="flex flex-col md:flex-row md:items-center justify-between mb-8">
        <div>
          <h1 className="text-2xl font-bold text-gray-900 mb-2">Offres recommandées</h1>
          <p className="text-gray-600">
            Voici les offres qui correspondent le mieux à votre profil
          </p>
        </div>
        <div className="mt-4 md:mt-0 flex space-x-2">
          <div className="relative">
            <input
              type="text"
              placeholder="Rechercher..."
              className="pl-10 pr-4 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 w-full md:w-64"
            />
            <Search className="absolute left-3 top-2.5 w-5 h-5 text-gray-400" />
          </div>
          <button className="flex items-center space-x-2 px-4 py-2 rounded-md border border-gray-300 text-gray-700 hover:bg-gray-50">
            <Filter className="w-5 h-5" />
            <span>Filtres</span>
          </button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        <div className="lg:col-span-1">
          <Card>
            <h3 className="font-semibold text-lg mb-4">Affiner la recherche</h3>
            <div className="space-y-6">
              <div>
                <h4 className="font-medium text-gray-700 mb-2">Localisation</h4>
                <div className="space-y-2">
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">Paris</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">Lyon</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">Marseille</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">Remote</span>
                  </label>
                </div>
              </div>
              <div>
                <h4 className="font-medium text-gray-700 mb-2">Type de contrat</h4>
                <div className="space-y-2">
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">CDI</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">CDD</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">Stage</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">Freelance</span>
                  </label>
                </div>
              </div>
              <div>
                <h4 className="font-medium text-gray-700 mb-2">Expérience</h4>
                <div className="space-y-2">
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">Débutant</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">Intermédiaire</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">Expérimenté</span>
                  </label>
                  <label className="flex items-center">
                    <input type="checkbox" className="form-checkbox h-4 w-4 text-primary-600" />
                    <span className="ml-2 text-gray-700">Senior</span>
                  </label>
                </div>
              </div>
            </div>
          </Card>
        </div>

        <div className="lg:col-span-3">
          {loading ? (
            <div className="text-center py-8">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600 mx-auto"></div>
              <p className="mt-2 text-gray-600">Chargement des offres...</p>
            </div>
          ) : error ? (
            <div className="text-center py-8 text-red-500">
              <p>{error}</p>
            </div>
          ) : (
            <JobList
              jobs={jobs.map(job => ({
                ...job,
                candidatures: [],
                recommandations: [],
                entreprise: {
                  id: (job.entreprise as any)?.id ?? 0,
                  nom: job.entreprise.nom,
                  email: (job.entreprise as any)?.email ?? '',
                  secteur: (job.entreprise as any)?.secteur ?? '',
                  description: (job.entreprise as any)?.description ?? '',
                },
              }))}
            />
          )}
        </div>
      </div>
    </div>
  );
};

export default CandidateJobs;