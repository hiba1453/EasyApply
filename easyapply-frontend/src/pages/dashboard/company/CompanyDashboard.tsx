import React from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Briefcase, 
  Users, 
  Eye, 
  TrendingUp, 
  Plus,
  Settings,
  BarChart3,
  Clock
} from 'lucide-react';
import Card from '../../../components/ui/Card';

const CompanyDashboard: React.FC = () => {
  const navigate = useNavigate();

  // Mock data - à remplacer par de vraies données
  const companyStats = {
    activeJobs: 12,
    totalApplications: 156,
    viewsThisMonth: 2840,
    hireRate: 18
  };

  const recentActivity = [
    { id: 1, activity: "Nouvelle candidature pour 'Développeur React'", time: "Il y a 2h" },
    { id: 2, activity: "Offre 'UX Designer' publiée avec succès", time: "Il y a 4h" },
    { id: 3, activity: "15 nouvelles vues sur vos offres", time: "Il y a 6h" },
    { id: 4, activity: "Candidature acceptée pour 'Chef de projet'", time: "Hier" }
  ];

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      {/* Header de bienvenue */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">
          Bienvenue sur votre espace entreprise
        </h1>
        <p className="text-gray-600 text-lg">
          Gérez vos offres d'emploi et trouvez les meilleurs talents
        </p>
      </div>

      {/* Statistiques principales */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <Card className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">Offres actives</p>
              <p className="text-2xl font-bold text-gray-900">{companyStats.activeJobs}</p>
            </div>
            <div className="p-3 bg-blue-100 rounded-lg">
              <Briefcase className="w-6 h-6 text-blue-600" />
            </div>
          </div>
        </Card>

        <Card className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">Candidatures</p>
              <p className="text-2xl font-bold text-gray-900">{companyStats.totalApplications}</p>
            </div>
            <div className="p-3 bg-green-100 rounded-lg">
              <Users className="w-6 h-6 text-green-600" />
            </div>
          </div>
        </Card>

        <Card className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">Vues ce mois</p>
              <p className="text-2xl font-bold text-gray-900">{companyStats.viewsThisMonth}</p>
            </div>
            <div className="p-3 bg-purple-100 rounded-lg">
              <Eye className="w-6 h-6 text-purple-600" />
            </div>
          </div>
        </Card>

        <Card className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-gray-600 mb-1">Taux d'embauche</p>
              <p className="text-2xl font-bold text-gray-900">{companyStats.hireRate}%</p>
            </div>
            <div className="p-3 bg-orange-100 rounded-lg">
              <TrendingUp className="w-6 h-6 text-orange-600" />
            </div>
          </div>
        </Card>
      </div>

      {/* Actions principales */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
        {/* Gestion des offres */}
        <Card className="p-6">
          <div className="text-center">
            <div className="p-4 bg-blue-100 rounded-full w-16 h-16 mx-auto mb-4 flex items-center justify-center">
              <Briefcase className="w-8 h-8 text-blue-600" />
            </div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">Mes offres postées</h3>
            <p className="text-gray-600 mb-4">
              Gérez vos offres d'emploi : créer, modifier, supprimer
            </p>
            <button
              onClick={() => navigate('/dashboard/company/jobs')}
              className="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors"
            >
              Voir mes offres
            </button>
          </div>
        </Card>

        {/* Créer une nouvelle offre */}
        <Card className="p-6">
          <div className="text-center">
            <div className="p-4 bg-green-100 rounded-full w-16 h-16 mx-auto mb-4 flex items-center justify-center">
              <Plus className="w-8 h-8 text-green-600" />
            </div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">Créer une offre</h3>
            <p className="text-gray-600 mb-4">
              Publiez une nouvelle offre d'emploi rapidement
            </p>
            <button
              onClick={() => navigate('/dashboard/company/jobs/create')}
              className="w-full bg-green-600 text-white py-2 px-4 rounded-lg hover:bg-green-700 transition-colors"
            >
              Créer une offre
            </button>
          </div>
        </Card>

        {/* Statistiques détaillées */}
        <Card className="p-6">
          <div className="text-center">
            <div className="p-4 bg-purple-100 rounded-full w-16 h-16 mx-auto mb-4 flex items-center justify-center">
              <BarChart3 className="w-8 h-8 text-purple-600" />
            </div>
            <h3 className="text-xl font-semibold text-gray-900 mb-2">Statistiques</h3>
            <p className="text-gray-600 mb-4">
              Analysez les performances de vos offres
            </p>
            <button
              onClick={() => navigate('/company/analytics')}
              className="w-full bg-purple-600 text-white py-2 px-4 rounded-lg hover:bg-purple-700 transition-colors"
            >
              Voir les stats
            </button>
          </div>
        </Card>
      </div>

      {/* Activité récente */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card className="p-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
            <Clock className="w-5 h-5 mr-2" />
            Activité récente
          </h3>
          <div className="space-y-3">
            {recentActivity.map((item) => (
              <div key={item.id} className="flex justify-between items-start p-3 bg-gray-50 rounded-lg">
                <p className="text-sm text-gray-800 flex-1">{item.activity}</p>
                <span className="text-xs text-gray-500 ml-2">{item.time}</span>
              </div>
            ))}
          </div>
        </Card>

        {/* Actions rapides */}
        <Card className="p-6">
          <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
            <Settings className="w-5 h-5 mr-2" />
            Actions rapides
          </h3>
          <div className="space-y-3">
            <button
              onClick={() => navigate('/company/applications')}
              className="w-full text-left p-3 rounded-lg border border-gray-200 hover:bg-gray-50 transition-colors"
            >
              <div className="flex items-center">
                <Users className="w-5 h-5 text-gray-600 mr-3" />
                <span className="text-gray-800">Gérer les candidatures</span>
              </div>
            </button>
            
            <button
              onClick={() => navigate('/company/profile')}
              className="w-full text-left p-3 rounded-lg border border-gray-200 hover:bg-gray-50 transition-colors"
            >
              <div className="flex items-center">
                <Settings className="w-5 h-5 text-gray-600 mr-3" />
                <span className="text-gray-800">Paramètres du profil</span>
              </div>
            </button>
            
            <button
              onClick={() => navigate('/company/candidates')}
              className="w-full text-left p-3 rounded-lg border border-gray-200 hover:bg-gray-50 transition-colors"
            >
              <div className="flex items-center">
                <Eye className="w-5 h-5 text-gray-600 mr-3" />
                <span className="text-gray-800">Parcourir les candidats</span>
              </div>
            </button>
          </div>
        </Card>
      </div>
    </div>
  );
};

export default CompanyDashboard;