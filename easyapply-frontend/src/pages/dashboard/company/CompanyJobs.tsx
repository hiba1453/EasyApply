import React, { useState, useEffect } from 'react';
import { Plus, Search } from 'lucide-react';
import Button from '../../../components/ui/Button';
import JobForm from '../../../components/jobs/JobForm';
import Card from '../../../components/ui/Card';
import JobCardCompany from '../../../components/jobs/JobCardCompany';
import { useNavigate } from 'react-router-dom';
import { JobPosting } from '../../../types';
import Tag from '../../../components/ui/Tag';

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
    id: number;
    nom: string;
    email: string;
    secteur: string;
    description: string;
  };
}

const CompanyJobs: React.FC = () => {
  const [isCreating, setIsCreating] = useState(false);
  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCompanyJobs = async () => {
      setLoading(true);
      try {
        const token = localStorage.getItem('token');
        const userId = localStorage.getItem('userId');

        if (!token || !userId) {
          navigate('/login');
          return;
        }

        const response = await fetch(`http://localhost:8090/api/jobs/company/${userId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });

        if (!response.ok) {
          if (response.status === 401 || response.status === 403) {
            navigate('/login');
            return;
          }
          throw new Error('Erreur lors de la récupération des offres');
        }

        const data = await response.json();
        setJobs(data.jobs || []);
      } catch (err: any) {
        console.error('Error fetching company jobs:', err);
        setError(err.message || 'Une erreur est survenue lors du chargement des offres');
      } finally {
        setLoading(false);
      }
    };

    fetchCompanyJobs();
  }, [navigate]);

  const handleCreateJob = (jobData: any) => {
    setJobs([jobData, ...jobs]);
    setIsCreating(false);
  };

  const mapJobToJobPosting = (job: Job): JobPosting => ({
    id: Number(job.id),
    titre: job.titre,
    entreprise: job.entreprise,
    lieu: job.lieu,
    salaire: job.salaire,
    description: job.description,
    dateExpiration: job.dateExpiration,
    motsCles: job.motsCles, // <-- reste string
    typeContrat: job.typeContrat,
    datePublication: job.datePublication,
    niveauExperience: job.niveauExperience,
    candidatures: [], // Remplacez ceci par les vraies données si disponibles
    recommandations: [], // Remplacez ceci par les vraies données si disponibles
  });

  // Fonction pour supprimer une offre
  const handleDeleteJob = async (id: number) => {
    if (!window.confirm("Voulez-vous vraiment supprimer cette offre ?")) return;
    try {
      const token = localStorage.getItem('token');
      const response = await fetch(`http://localhost:8090/api/jobs/${id}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Erreur lors de la suppression de l\'offre');
      }
      setJobs(jobs.filter(job => job.id !== id));
    } catch (err: any) {
      alert(err.message || 'Erreur lors de la suppression');
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center py-8">
        <p className="text-red-500">{error}</p>
        <Button onClick={() => window.location.reload()} className="mt-4">
          Réessayer
        </Button>
      </div>
    );
  }

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
                {jobs.map(job => {
                  const tags = job.motsCles ? job.motsCles.split(',').map(tag => tag.trim()) : [];
                  return (
                    <div key={job.id}>
                      <JobCardCompany job={mapJobToJobPosting(job)} showActions={true} onDelete={handleDeleteJob} />
                      {tags.length > 0 && (
                        <div className="flex flex-wrap gap-2 mb-4">
                          {tags.map((tag, index) => (
                            <Tag key={index} label={tag} color={index % 2 === 0 ? 'blue' : 'gray'} size="sm" />
                          ))}
                        </div>
                      )}
                    </div>
                  );
                })}
                {jobs.length === 0 && (
                  <p className="text-gray-500 text-center py-4">
                    Vous n'avez pas encore publié d'offres d'emploi.
                  </p>
                )}
              </div>
            </Card>
            
            <Card>
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold">Statistiques</h3>
              </div>
              
              <div className="grid grid-cols-2 gap-4 mb-6">
                <div className="bg-gray-50 rounded-lg p-4">
                  <div className="text-3xl font-bold text-primary-600 mb-1">
                    {jobs.length}
                  </div>
                  <div className="text-gray-600 text-sm">Offres actives</div>
                </div>
                
                <div className="bg-gray-50 rounded-lg p-4">
                  <div className="text-3xl font-bold text-green-600 mb-1">-</div>
                  <div className="text-gray-600 text-sm">Candidatures reçues</div>
                </div>
                
                <div className="bg-gray-50 rounded-lg p-4">
                  <div className="text-3xl font-bold text-blue-600 mb-1">-</div>
                  <div className="text-gray-600 text-sm">Vues totales</div>
                </div>
                
                <div className="bg-gray-50 rounded-lg p-4">
                  <div className="text-3xl font-bold text-purple-600 mb-1">-</div>
                  <div className="text-gray-600 text-sm">Taux de conversion</div>
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
