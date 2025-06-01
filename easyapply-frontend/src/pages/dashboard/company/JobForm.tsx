import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { 
  Save, 
  X, 
  Upload,
  MapPin,
  DollarSign,
  Calendar,
  Users,
  Briefcase
} from 'lucide-react';
import Card from '../../../components/ui/Card';

interface JobFormData {
  title: string;
  department: string;
  location: string;
  type: string;
  experience: string;
  salary: string;
  description: string;
  requirements: string;
  benefits: string;
  deadline: string;
  remote: boolean;
  urgent: boolean;
}

const JobForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const isEdit = Boolean(id);

  const [formData, setFormData] = useState<JobFormData>({
    title: '',
    department: '',
    location: '',
    type: 'CDI',
    experience: 'Intermédiaire',
    salary: '',
    description: '',
    requirements: '',
    benefits: '',
    deadline: '',
    remote: false,
    urgent: false
  });

  const [errors, setErrors] = useState<Partial<JobFormData>>({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const jobTypes = [
    'CDI', 'CDD', 'Stage', 'Freelance', 'Alternance', 'Intérim'
  ];

  const experienceLevels = [
    'Débutant', 'Intermédiaire', 'Expérimenté', 'Senior', 'Expert'
  ];

  const departments = [
    'Développement', 'Design', 'Marketing', 'Ventes', 'RH', 
    'Finance', 'Support', 'Direction', 'Autre'
  ];

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target;
    
    if (type === 'checkbox') {
      const checked = (e.target as HTMLInputElement).checked;
      setFormData(prev => ({
        ...prev,
        [name]: checked
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }

    // Clear error when user starts typing
    if (errors[name as keyof JobFormData]) {
      setErrors(prev => ({
        ...prev,
        [name]: undefined
      }));
    }
  };

  const validateForm = (): boolean => {
    const newErrors: Partial<JobFormData> = {};

    if (!formData.title.trim()) newErrors.title = 'Le titre est requis';
    if (!formData.department.trim()) newErrors.department = 'Le département est requis';
    if (!formData.location.trim()) newErrors.location = 'La localisation est requise';
    if (!formData.description.trim()) newErrors.description = 'La description est requise';
    if (!formData.requirements.trim()) newErrors.requirements = 'Les exigences sont requises';
    if (!formData.deadline) newErrors.deadline = 'La date limite est requise';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validateForm()) return;

    setIsSubmitting(true);
    
    try {
      // Ici, vous feriez l'appel API pour créer/modifier l'offre
      console.log('Données à envoyer:', formData);
      
      // Simulation d'un délai d'API
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // Redirection vers la liste des offres
      navigate('/company/jobs');
    } catch (error) {
      console.error('Erreur lors de l\'enregistrement:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    navigate('/company/jobs');
  };

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      {/* Header */}
      <div className="mb-8">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900 mb-2">
              {isEdit ? 'Modifier l\'offre' : 'Créer une nouvelle offre'}
            </h1>
            <p className="text-gray-600">
              {isEdit ? 'Modifiez les détails de votre offre d\'emploi' : 'Publiez une nouvelle offre d\'emploi'}
            </p>
          </div>
          <button
            onClick={handleCancel}
            className="flex items-center space-x-2 px-4 py-2 text-gray-600 hover:text-gray-800 transition-colors"
          >
            <X className="w-5 h-5" />
            <span>Annuler</span>
          </button>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="max-w-4xl mx-auto">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          {/* Colonne principale */}
          <div className="lg:col-span-2 space-y-6">
            {/* Informations générales */}
            <Card className="p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4 flex items-center">
                <Briefcase className="w-5 h-5 mr-2" />
                Informations générales
              </h3>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Titre du poste *
                  </label>
                  <input
                    type="text"
                    name="title"
                    value={formData.title}
                    onChange={handleInputChange}
                    className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                      errors.title ? 'border-red-500' : 'border-gray-300'
                    }`}
                    placeholder="Ex: Développeur React Senior"
                  />
                  {errors.title && <p className="text-red-500 text-xs mt-1">{errors.title}</p>}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Département *
                  </label>
                  <select
                    name="department"
                    value={formData.department}
                    onChange={handleInputChange}
                    className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                      errors.department ? 'border-red-500' : 'border-gray-300'
                    }`}
                  >
                    <option value="">Sélectionner</option>
                    {departments.map(dept => (
                      <option key={dept} value={dept}>{dept}</option>
                    ))}
                  </select>
                  {errors.department && <p className="text-red-500 text-xs mt-1">{errors.department}</p>}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Type de contrat
                  </label>
                  <select
                    name="type"
                    value={formData.type}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    {jobTypes.map(type => (
                      <option key={type} value={type}>{type}</option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Localisation *
                  </label>
                  <input
                    type="text"
                    name="location"
                    value={formData.location}
                    onChange={handleInputChange}
                    className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                      errors.location ? 'border-red-500' : 'border-gray-300'
                    }`}
                    placeholder="Ex: Paris, France"
                  />
                  {errors.location && <p className="text-red-500 text-xs mt-1">{errors.location}</p>}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Niveau d'expérience
                  </label>
                  <select
                    name="experience"
                    value={formData.experience}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    {experienceLevels.map(level => (
                      <option key={level} value={level}>{level}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="mt-4 grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Salaire
                  </label>
                  <input
                    type="text"
                    name="salary"
                    value={formData.salary}
                    onChange={handleInputChange}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="Ex: 45 000 - 55 000 €"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Date limite de candidature *
                  </label>
                  <input
                    type="date"
                    name="deadline"
                    value={formData.deadline}
                    onChange={handleInputChange}
                    className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                      errors.deadline ? 'border-red-500' : 'border-gray-300'
                    }`}
                  />
                  {errors.deadline && <p className="text-red-500 text-xs mt-1">{errors.deadline}</p>}
                </div>
              </div>

              <div className="mt-4 flex space-x-4">
                <label className="flex items-center">
                  <input
                    type="checkbox"
                    name="remote"
                    checked={formData.remote}
                    onChange={handleInputChange}
                    className="form-checkbox h-4 w-4 text-blue-600"
                  />
                  <span className="ml-2 text-sm text-gray-700">Télétravail possible</span>
                </label>
                
                <label className="flex items-center">
                  <input
                    type="checkbox"
                    name="urgent"
                    checked={formData.urgent}
                    onChange={handleInputChange}
                    className="form-checkbox h-4 w-4 text-red-600"
                  />
                  <span className="ml-2 text-sm text-gray-700">Recrutement urgent</span>
                </label>
              </div>
            </Card>

            {/* Description du poste */}
            <Card className="p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">
                Description du poste
              </h3>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Description détaillée *
                </label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  rows={6}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                    errors.description ? 'border-red-500' : 'border-gray-300'
                  }`}
                  placeholder="Décrivez le poste, les missions principales, l'environnement de travail..."
                />
                {errors.description && <p className="text-red-500 text-xs mt-1">{errors.description}</p>}
              </div>
            </Card>

            {/* Exigences */}
            <Card className="p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">
                Profil recherché
              </h3>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Compétences et exigences *
                </label>
                <textarea
                  name="requirements"
                  value={formData.requirements}
                  onChange={handleInputChange}
                  rows={5}
                  className={`w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 ${
                    errors.requirements ? 'border-red-500' : 'border-gray-300'
                  }`}
                  placeholder="Listez les compétences techniques, l'expérience requise, les diplômes..."
                />
                {errors.requirements && <p className="text-red-500 text-xs mt-1">{errors.requirements}</p>}
              </div>
            </Card>

            {/* Avantages */}
            <Card className="p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">
                Avantages et bénéfices
              </h3>
              
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Ce que nous offrons
                </label>
                <textarea
                  name="benefits"
                  value={formData.benefits}
                  onChange={handleInputChange}
                  rows={4}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="Avantages sociaux, formation, évolution de carrière, télétravail..."
                />
              </div>
            </Card>
          </div>

          {/* Sidebar */}
          <div className="space-y-6">
            {/* Aperçu */}
            <Card className="p-6">
              <h3 className="text-lg font-semibold text-gray-900 mb-4">
                Aperçu de l'offre
              </h3>
              
              <div className="space-y-3 text-sm">
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">Titre:</span>
                  <span className="font-medium">{formData.title || 'Non défini'}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">Type:</span>
                  <span className="font-medium">{formData.type}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">Localisation:</span>
                  <span className="font-medium">{formData.location || 'Non définie'}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">Expérience:</span>
                  <span className="font-medium">{formData.experience}</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="text-gray-600">Échéance:</span>
                  <span className="font-medium">{formData.deadline || 'Non définie'}</span>
                </div>
              </div>

              {(formData.remote || formData.urgent) && (
                <div className="mt-4 pt-4 border-t border-gray-200">
                  <div className="space-y-2">
                    {formData.remote && (
                      <span className="inline-block px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full">
                        Télétravail
                      </span>
                    )}
                    {formData.urgent && (
                      <span className="inline-block px-2 py-1 bg-red-100 text-red-800 text-xs rounded-full">
                        Urgent
                      </span>
                    )}
                  </div>
                </div>
              )}
            </Card>

            {/* Actions */}
            <Card className="p-6">
              <div className="space-y-3">
                <button
                  type="submit"
                  disabled={isSubmitting}
                  className="w-full bg-blue-600 text-white py-3 px-4 rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-colors flex items-center justify-center space-x-2"
                >
                  {isSubmitting ? (
                    <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                  ) : (
                    <Save className="w-5 h-5" />
                  )}
                  <span>
                    {isSubmitting ? 'Enregistrement...' : (isEdit ? 'Modifier' : 'Publier l\'offre')}
                  </span>
                </button>
                
                <button
                  type="button"
                  onClick={handleCancel}
                  className="w-full bg-gray-100 text-gray-700 py-3 px-4 rounded-lg hover:bg-gray-200 transition-colors"
                >
                  Annuler
                </button>
              </div>
            </Card>

            {/* Conseils */}
            <Card className="p-6 bg-blue-50 border-blue-200">
              <h4 className="text-sm font-semibold text-blue-900 mb-2">💡 Conseils</h4>
              <ul className="text-xs text-blue-800 space-y-1">
                <li>• Soyez précis dans le titre</li>
                <li>• Détaillez les missions</li>
                <li>• Listez les compétences requises</li>
                <li>• Mentionnez les avantages</li>
                <li>• Fixez une date limite réaliste</li>
              </ul>
            </Card>
          </div>
        </div>
      </form>
    </div>
  );
};

export default JobForm;