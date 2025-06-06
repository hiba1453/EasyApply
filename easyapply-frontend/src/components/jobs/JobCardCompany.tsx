import React from 'react';
import { Link } from 'react-router-dom';
import { MapPin, Calendar, DollarSign } from 'lucide-react';
import Card from '../ui/Card';
import Tag from '../ui/Tag';
import Button from '../ui/Button';
import { JobPosting } from '../../types';

interface JobCardCompanyProps {
  job: JobPosting;
  showActions?: boolean;
  onDelete?: (id: number) => void; // id is a number after mapping
}

const JobCardCompany: React.FC<JobCardCompanyProps> = ({ job, showActions = true, onDelete }) => {
  const { id, titre, entreprise, lieu, salaire, motsCles, dateExpiration } = job;

  // Calculate days ago
  const getDaysAgo = (dateString: string) => {
    const posted = new Date(dateString);
    const today = new Date();
    const diffTime = Math.abs(today.getTime() - posted.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    if (diffDays === 0) return "Aujourd'hui";
    if (diffDays === 1) return "Hier";
    return `Il y a ${diffDays} jours`;
  };

  return (
    <Card className="transition-all duration-200 hover:shadow-md">
      <div className="flex flex-col md:flex-row md:items-center">
        <div className="flex-1">
          <h3 className="text-lg font-semibold text-gray-900 mb-1">{titre}</h3>
          <p className="text-primary-600 font-medium mb-2">{entreprise.nom}</p>
          <div className="flex flex-wrap gap-4 text-sm text-gray-600 mb-3">
            <div className="flex items-center">
              <MapPin className="w-4 h-4 mr-1" />
              <span>{lieu}</span>
            </div>
            <div className="flex items-center">
              <DollarSign className="w-4 h-4 mr-1" />
              <span>{salaire}</span>
            </div>
            <div className="flex items-center">
              <Calendar className="w-4 h-4 mr-1" />
              <span>{getDaysAgo(dateExpiration)}</span>
            </div>
          </div>
          <div className="flex flex-wrap gap-2 mb-4">
            {Array.isArray(motsCles) && motsCles.map((tag: string, index: number) => (
              <Tag key={index} label={tag} color={index % 2 === 0 ? 'blue' : 'gray'} size="sm" />
            ))}
          </div>
        </div>
        {showActions && (
          <div className="flex flex-col md:flex-row gap-2 mt-4 md:mt-0">
            <Link to={`/dashboard/company/jobs/${id}`}>
              <Button variant="secondary" size="sm">Voir détails</Button>
            </Link>
            <Link to={`/dashboard/company/jobs/edit/${id}`}>
              <Button variant="primary" size="sm">Modifier</Button>
            </Link>
            {onDelete && (
              <Button variant="outline" size="sm" onClick={() => onDelete(id)}>
                Supprimer
              </Button>
            )}
          </div>
        )}
      </div>
    </Card>
  );
};

export default JobCardCompany;