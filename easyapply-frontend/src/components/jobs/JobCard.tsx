import React from 'react';
import { Link } from 'react-router-dom';
import { MapPin, Calendar, DollarSign } from 'lucide-react';
import Card from '../ui/Card';
import Tag from '../ui/Tag';
import Button from '../ui/Button';
import { JobPosting } from '../../types';

interface JobCardProps {
  job: JobPosting;
  showActions?: boolean;
}

const JobCard: React.FC<JobCardProps> = ({ job, showActions = true }) => {
  const { id, title, company, location, salary, tags, postedDate } = job;

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

  return (
    <Card className="transition-all duration-200 hover:shadow-md">
      <div className="flex flex-col md:flex-row md:items-center">
        <div className="flex-1">
          <h3 className="text-lg font-semibold text-gray-900 mb-1">{title}</h3>
          <p className="text-primary-600 font-medium mb-2">{company}</p>
          
          <div className="flex flex-wrap gap-4 text-sm text-gray-600 mb-3">
            <div className="flex items-center">
              <MapPin className="w-4 h-4 mr-1" />
              <span>{location}</span>
            </div>
            <div className="flex items-center">
              <DollarSign className="w-4 h-4 mr-1" />
              <span>{salary}</span>
            </div>
            <div className="flex items-center">
              <Calendar className="w-4 h-4 mr-1" />
              <span>{getDaysAgo(postedDate)}</span>
            </div>
          </div>
          
          <div className="flex flex-wrap gap-2 mb-4">
            {tags.map((tag, index) => (
              <Tag key={index} label={tag} color={index % 2 === 0 ? 'blue' : 'gray'} size="sm" />
            ))}
          </div>
        </div>
        
        {showActions && (
          <div className="flex flex-col md:flex-row gap-2 mt-4 md:mt-0">
            <Link to={`/jobs/${id}`}>
              <Button variant="secondary" size="sm">Voir détails</Button>
            </Link>
            <Link to={`/apply/${id}`}>
              <Button variant="primary" size="sm">Postuler</Button>
            </Link>
          </div>
        )}
      </div>
    </Card>
  );
};

export default JobCard;