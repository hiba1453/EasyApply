import React, { useState } from 'react';
import Button from '../../../components/ui/Button';
import Card from '../../../components/ui/Card';

interface Application {
  id: number;
  nom: string;
  poste: string;
  offre: string;
  statut: 'En attente' | 'Acceptée' | 'Refusée';
}

const exemples: Application[] = [
  { id: 1, nom: 'Ahmed Ben Ali', poste: 'Développeur Fullstack', offre: 'Développeur Fullstack - Casablanca', statut: 'En attente' },
  { id: 2, nom: 'Fatima Zahra', poste: 'Designer UI/UX', offre: 'Designer UI/UX - Rabat', statut: 'En attente' },
  { id: 3, nom: 'Youssef Alaoui', poste: 'Chef de projet', offre: 'Chef de projet - Marrakech', statut: 'En attente' },
  { id: 4, nom: 'Karim Hassan', poste: 'Data Scientist', offre: 'Data Scientist - Tanger', statut: 'En attente' },
  { id: 5, nom: 'Leila Ben Salah', poste: 'Marketing Digital', offre: 'Marketing Digital - Fès', statut: 'En attente' },
  { id: 6, nom: 'Omar Ben Younes', poste: 'DevOps Engineer', offre: 'DevOps Engineer - Agadir', statut: 'En attente' },
  { id: 7, nom: 'Amina Ben Ammar', poste: 'UX Researcher', offre: 'UX Researcher - Meknès', statut: 'En attente' },
  { id: 8, nom: 'Mehdi Ben Hassen', poste: 'Product Manager', offre: 'Product Manager - Oujda', statut: 'En attente' },
  { id: 9, nom: 'Sana Ben Moussa', poste: 'Frontend Developer', offre: 'Frontend Developer - Tétouan', statut: 'En attente' },
  { id: 10, nom: 'Rachid Ben Amor', poste: 'Backend Developer', offre: 'Backend Developer - Salé', statut: 'En attente' },
];

const statusColors = {
  'En attente': 'bg-yellow-100 text-yellow-800',
  'Acceptée': 'bg-green-100 text-green-800',
  'Refusée': 'bg-red-100 text-red-800',
};

const CompanyApplications: React.FC = () => {
  const [applications, setApplications] = useState<Application[]>(exemples);
  const [message, setMessage] = useState<string | null>(null);

  const handleStatusChange = (id: number, statut: Application['statut']) => {
    setApplications(applications.map(app => app.id === id ? { ...app, statut } : app));
    setMessage(`Statut mis à jour: ${statut}`);
    setTimeout(() => setMessage(null), 2000);
  };

  return (
    <div className="max-w-3xl mx-auto py-10">
      <h1 className="text-2xl font-bold text-gray-900 mb-2">Candidatures reçues</h1>
      <p className="text-gray-600 mb-8">Voici la liste des candidatures reçues pour vos offres. Gérez leur statut facilement.</p>
      {message && (
        <div className="mb-4 p-3 rounded bg-green-100 text-green-800 text-center font-medium shadow">
          {message}
        </div>
      )}
      <div className="space-y-6">
        {applications.map(app => (
          <Card key={app.id} className="flex flex-col md:flex-row md:items-center justify-between p-6">
            <div>
              <div className="text-lg font-semibold text-gray-800">{app.nom}</div>
              <div className="text-gray-500">{app.poste}</div>
              <div className="text-sm text-gray-600 mt-1">Offre: {app.offre}</div>
            </div>
            <div className="flex items-center gap-3 mt-4 md:mt-0">
              <span className={`px-3 py-1 rounded-full text-xs font-medium ${statusColors[app.statut]}`}>{app.statut}</span>
              <Button variant="primary" size="sm" disabled={app.statut === 'Acceptée'} onClick={() => handleStatusChange(app.id, 'Acceptée')}>Accepter</Button>
              <Button variant="outline" size="sm" disabled={app.statut === 'Refusée'} onClick={() => handleStatusChange(app.id, 'Refusée')}>Refuser</Button>
              <Button variant="secondary" size="sm" disabled={app.statut === 'En attente'} onClick={() => handleStatusChange(app.id, 'En attente')}>En attente</Button>
            </div>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default CompanyApplications;
