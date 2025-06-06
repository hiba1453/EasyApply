import React, { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { MapPin, DollarSign, Calendar, Briefcase, Clock } from 'lucide-react';
import Card from '../ui/Card';
import Tag from '../ui/Tag';
import Button from '../ui/Button';
import { JobPosting } from '../../types';

interface OffreDetailsResponse {
  offre: JobPosting;
  hasApplied: boolean;
}

const JobDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [offreDetails, setOffreDetails] = useState<OffreDetailsResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchOffreDetails = async () => {
      try {
        const response = await fetch(`http://localhost:8090/offre/${id}`, {
          method: 'GET',
          credentials: 'include',
          headers: { 'Content-Type': 'application/json' },
        });

        console.log('Response status:', response.status);
        console.log('Response headers:', Object.fromEntries(response.headers.entries()));

        if (!response.ok) {
          let errorData: any = {};
          try {
            errorData = await response.json();
            console.log('Error response body:', errorData);
          } catch (e) {
            console.error('Failed to parse error response:', e);
          }
          if (response.status === 401) {
            localStorage.removeItem('role');
            localStorage.removeItem('userId');
            localStorage.removeItem('email');
            navigate('/login', { state: { error: 'Veuillez vous connecter pour voir les détails.' } });
            return;
          }
          throw new Error(errorData.error || `Erreur HTTP ${response.status}`);
        }

        const data = await response.json();
        console.log('Success response body:', data);
        setOffreDetails(data);
      } catch (err) {
        if (err && typeof err === 'object' && 'message' in err) {
          console.error('Fetch error:', (err as { message: string }).message);
          setError((err as { message: string }).message);
        } else {
          console.error('Fetch error:', err);
          setError('Une erreur est survenue.');
        }
      } finally {
        setLoading(false);
      }
    };

    fetchOffreDetails();
  }, [id, navigate]);

  if (loading) {
    return <div className="text-center p-8">Chargement...</div>;
  }

  if (error) {
    return (
      <div className="text-center p-8 text-red-600">
        {error}
        <div className="mt-4">
          <Link to="/dashboard/candidat/jobs">
            <Button variant="primary">Retour aux offres</Button>
          </Link>
        </div>
      </div>
    );
  }

  if (!offreDetails) {
    return <div className="text-center p-8">Offre non trouvée.</div>;
  }

  const { offre, hasApplied } = offreDetails;
  const tags = offre.motsCles ? offre.motsCles.split(',').map(tag => tag.trim()) : [];

  return (
    <div className="container mx-auto p-8">
      <Card className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900 mb-2">{offre.titre}</h1>
        <p className="text-primary-600 font-medium mb-4">{offre.entreprise.nom}</p>
        <div className="flex flex-wrap gap-4 text-sm text-gray-600 mb-4">
          {offre.lieu && (
            <div className="flex items-center">
              <MapPin className="w-4 h-4 mr-1" />
              <span>{offre.lieu}</span>
            </div>
          )}
          {offre.salaire && (
            <div className="flex items-center">
              <DollarSign className="w-4 h-4 mr-1" />
              <span>{offre.salaire}</span>
            </div>
          )}
          <div className="flex items-center">
            <Calendar className="w-4 h-4 mr-1" />
            <span>
              Publié {new Date(offre.datePublication).toLocaleDateString('fr-FR')}
            </span>
          </div>
          {offre.typeContrat && (
            <div className="flex items-center">
              <Briefcase className="w-4 h-4 mr-1" />
              <span>{offre.typeContrat}</span>
            </div>
          )}
          {offre.niveauExperience && (
            <div className="flex items-center">
              <Clock className="w-4 h-4 mr-1" />
              <span>{offre.niveauExperience}</span>
            </div>
          )}
        </div>
        {tags.length > 0 && (
          <div className="flex flex-wrap gap-2 mb-4">
            {tags.map((tag, index) => (
              <Tag key={index} label={tag} color={index % 2 === 0 ? 'blue' : 'gray'} size="sm" />
            ))}
          </div>
        )}
        <div className="mb-4">
          <h2 className="text-lg font-semibold mb-2">Description</h2>
          <p className="text-gray-700">{offre.description || 'Aucune description disponible.'}</p>
        </div>
        <div className="flex gap-4">
          <Link to="/dashboard/candidat/jobs">
            <Button variant="secondary">Retour</Button>
          </Link>
          {hasApplied ? (
            <Button variant="primary" disabled>Déjà postulé</Button>
          ) : (
            <Link to={`/apply/${id}`}>
              <Button variant="primary">Postuler</Button>
            </Link>
          )}
        </div>
      </Card>
    </div>
  );
};

export default JobDetails;