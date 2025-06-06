import React from 'react';
import { Link } from 'react-router-dom';
import { MapPin, Calendar, DollarSign } from 'lucide-react';
import Card from '../ui/Card';
import Tag from '../ui/Tag';
import Button from '../ui/Button';
import { JobPosting } from '../../types';

interface JobCardProp {
    job: JobPosting;
    showActions?: boolean;
}


const JobCardCandidat: React.FC<JobCardProp> = ({ job, showActions = true }) => {
  const { id, titre, entreprise, lieu, salaire, motsCles, datePublication } = job;
  const [showDetails, setShowDetails] = React.useState(false);

  // Split motsCles into tags
  const tags = motsCles ? motsCles.split(',').map(tag => tag.trim()) : [];

  // Calculate days ago
  const getDaysAgo = (dateString: string) => {
    const postedDate = new Date(dateString);
    const today = new Date();
    const diffTime = Math.abs(today.getTime() - postedDate.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays === 0) return "Aujourd'hui";
    if (diffDays === 1) return "Hier";
    return `Il y a ${diffDays} jours`;
  };

  const handleApply = async () => {
    if (!window.confirm("Voulez-vous vraiment postuler à cette offre ?")) return;
    try {
      const response = await fetch(`http://localhost:8090/offre/${id}/apply`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' }
      });
      const data = await response.json();
      if (response.ok) {
        alert(data.message || "Candidature soumise avec succès !");
      } else {
        alert(data.error || "Erreur lors de la candidature.");
      }
    } catch (err) {
      alert("Erreur réseau lors de la candidature.");
    }
  };

  return (
    <Card className="transition-all duration-200 hover:shadow-md">
      <div className="flex flex-col md:flex-row md:items-center">
        <div className="flex-1">
          <h3 className="text-lg font-semibold text-gray-900 mb-1">{titre}</h3>
          <p className="text-primary-600 font-medium mb-2">{entreprise.nom}</p>
          
          <div className="flex flex-wrap gap-4 text-sm text-gray-600 mb-3">
            {lieu && (
              <div className="flex items-center">
                <MapPin className="w-4 h-4 mr-1" />
                <span>{lieu}</span>
              </div>
            )}
            {salaire && (
              <div className="flex items-center">
                <DollarSign className="w-4 h-4 mr-1" />
                <span>{salaire}</span>
              </div>
            )}
            <div className="flex items-center">
              <Calendar className="w-4 h-4 mr-1" />
              <span>{getDaysAgo(datePublication)}</span>
            </div>
          </div>
          
          {tags.length > 0 && (
            <div className="flex flex-wrap gap-2 mb-4">
              {tags.map((tag, index) => (
                <Tag key={index} label={tag} color={index % 2 === 0 ? 'blue' : 'gray'} size="sm" />
              ))}
            </div>
          )}
        </div>
        
        {showActions && (
          <div className="flex flex-col md:flex-row gap-2 mt-4 md:mt-0">
            <Button variant="secondary" size="sm" onClick={() => setShowDetails(v => !v)}>
              {showDetails ? "Masquer les détails" : "Voir détails"}
            </Button>
            <Button variant="primary" size="sm" onClick={handleApply}>
              Postuler
            </Button>
          </div>
        )}
      </div>

      {showDetails && (
        <div className="mt-4 p-4 bg-gray-50 rounded">
          <h3 className="text-md font-semibold mb-2">Détails de l'offre</h3>
          <p><b>Entreprise :</b> {job.entreprise.nom}</p>
          <p><b>Description de l'offre :</b> {job.description}</p>
          <p><b>Lieu :</b> {job.lieu}</p>
          <p><b>Salaire :</b> {job.salaire}</p>
          <p><b>Date de publication :</b> {new Date(job.datePublication).toLocaleDateString('fr-FR')}</p>
          <p><b>Type de contrat :</b> {job.typeContrat}</p>
          <p><b>Niveau d'expérience :</b> {job.niveauExperience}</p>
          <p><b>Date d'expiration :</b> {job.dateExpiration}</p>
          <p><b>Nombre de candidatures :</b> {job.candidatures?.length ?? 0}</p>

          {/* Ajoute d'autres infos si besoin */}
        </div>
      )}
    </Card>
  );
};

export default JobCardCandidat;