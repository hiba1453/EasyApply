import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Input from '../ui/Input';
import Button from '../ui/Button';
import Card from '../ui/Card';

interface JobFormProps {
  onSubmit?: (jobData: any) => void;
  initialValues?: any;
}

const JobForm: React.FC<JobFormProps> = ({ onSubmit, initialValues = {} }) => {
  const navigate = useNavigate();
  const currentDateTime = new Date('2025-06-01T22:06:00+01:00'); // Current date/time: June 01, 2025, 10:06 PM +01

  const [formData, setFormData] = useState({
    titre: initialValues.titre || '',
    entrepriseId: initialValues.entrepriseId || '',
    lieu: initialValues.lieu || '',
    salaire: initialValues.salaire || '',
    description: initialValues.description || '',
    motsCles: initialValues.motsCles || '',
    datePublication: initialValues.datePublication || currentDateTime.toISOString(), // Default to now
    dateExpiration: initialValues.dateExpiration || '',
  });
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [submissionError, setSubmissionError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  if (errors[name]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
    setSubmissionError(null);
  };

  const validateForm = () => {
    const newErrors: Record<string, string> = {};
    
    if (!formData.titre.trim()) {
      newErrors.titre = 'Le titre est requis';
    }
    
    if (!formData.entrepriseId) {
      newErrors.entrepriseId = "L'ID de l'entreprise est requis";
    } else if (isNaN(Number(formData.entrepriseId))) {
      newErrors.entrepriseId = "L'ID de l'entreprise doit être un nombre";
    }
    
    if (!formData.lieu.trim()) {
      newErrors.lieu = 'La localisation est requise';
    }
    
    if (!formData.description.trim()) {
      newErrors.description = 'La description est requise';
    }
    
    if (formData.salaire && !/^\d{1,3}(,\d{3})*\s*€\s*-\s*\d{1,3}(,\d{3})*\s*€$/.test(formData.salaire)) {
      newErrors.salaire = 'Le salaire doit être au format "XX,XXX € - YY,YYY €"';
    }

    // Validate dateExpiration is in the future
    if (!formData.dateExpiration) {
      newErrors.dateExpiration = 'La date d’expiration est requise';
    } else {
      const expirationDate = new Date(formData.dateExpiration);
      if (isNaN(expirationDate.getTime())) {
        newErrors.dateExpiration = 'La date d’expiration est invalide';
      } else if (expirationDate <= currentDateTime) {
        newErrors.dateExpiration = 'La date d’expiration doit être dans le futur';
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmissionError(null);
    setSuccess(null);

    if (!validateForm()) {
      return;
    }

    try {
      setLoading(true);
      const response = await fetch('http://localhost:8090/api/jobs', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          titre: formData.titre,
          description: formData.description,
          entrepriseId: Number(formData.entrepriseId),
          lieu: formData.lieu,
          salaire: formData.salaire,
          motsCles: formData.motsCles,
          datePublication: formData.datePublication, // ISO string
          dateExpiration: formData.dateExpiration, // ISO string
        }),
      });

      const data = await response.json();

      if (!response.ok) {
        const errorMsg = data.error || 'Échec de la création de l’offre';
        if (response.status === 400 && data.error === 'Le titre est requis') {
          setErrors((prev) => ({ ...prev, titre: data.error }));
        } else if (response.status === 400 && data.error === "L'ID de l'entreprise est requis") {
          setErrors((prev) => ({ ...prev, entrepriseId: data.error }));
        } else {
          setSubmissionError(errorMsg);
        }
        throw new Error(errorMsg);
      }

      setSuccess(data.message || 'Offre créée avec succès');
      setFormData({
        titre: '',
        entrepriseId: '',
        lieu: '',
        salaire: '',
        description: '',
        motsCles: '',
        datePublication: currentDateTime.toISOString(),
        dateExpiration: '',
      });

      setTimeout(() => {
        navigate('/dashboard/company');
      }, 2000);

      if (onSubmit) {
        onSubmit(data.job);
      }
    } catch (err: any) {
      setSubmissionError(err.message || 'Erreur lors de la connexion au serveur');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card>
      <h2 className="text-xl font-semibold mb-6" role="heading" aria-level={2}>
        Publier une offre d'emploi
      </h2>
      
      <form onSubmit={handleSubmit} className="space-y-4" noValidate>
        <Input
          label="Titre du poste"
          id="titre"
          name="titre"
          value={formData.titre}
          onChange={handleChange}
          error={errors.titre}
          placeholder="ex: Développeur Frontend React"
          aria-required="true"
          aria-describedby="titre-error"
        />
        {errors.titre && <p id="titre-error" className="mt-1 text-sm text-error-600" role="alert">{errors.titre}</p>}
        
        <Input
          label="ID de l'entreprise"
          id="entrepriseId"
          name="entrepriseId"
          value={formData.entrepriseId}
          onChange={handleChange}
          error={errors.entrepriseId}
          placeholder="ex: 123"
          helper="Entrez l'ID unique de votre entreprise"
          aria-required="true"
          aria-describedby="entrepriseId-error"
        />
        {errors.entrepriseId && <p id="entrepriseId-error" className="mt-1 text-sm text-error-600" role="alert">{errors.entrepriseId}</p>}
        
        <Input
          label="Localisation"
          id="lieu"
          name="lieu"
          value={formData.lieu}
          onChange={handleChange}
          error={errors.lieu}
          placeholder="ex: Paris, France"
          aria-required="true"
          aria-describedby="lieu-error"
        />
        {errors.lieu && <p id="lieu-error" className="mt-1 text-sm text-error-600" role="alert">{errors.lieu}</p>}
        
        <Input
          label="Salaire"
          id="salaire"
          name="salaire"
          value={formData.salaire}
          onChange={handleChange}
          error={errors.salaire}
          placeholder="ex: 45,000 € - 60,000 €"
          aria-describedby="salaire-error"
        />
        {errors.salaire && <p id="salaire-error" className="mt-1 text-sm text-error-600" role="alert">{errors.salaire}</p>}
        
        <Input
          label="Date de publication"
          id="datePublication"
          name="datePublication"
          type="datetime-local"
          value={formData.datePublication.slice(0, 16)} // Format for datetime-local
          onChange={handleChange}
          error={errors.datePublication}
          disabled // Auto-set to current date/time
          aria-describedby="datePublication-error"
        />
        {errors.datePublication && <p id="datePublication-error" className="mt-1 text-sm text-error-600" role="alert">{errors.datePublication}</p>}
        
        <Input
          label="Date d'expiration"
          id="dateExpiration"
          name="dateExpiration"
          type="datetime-local"
          value={formData.dateExpiration.slice(0, 16)} // Format for datetime-local
          onChange={handleChange}
          error={errors.dateExpiration}
          aria-required="true"
          aria-describedby="dateExpiration-error"
        />
        {errors.dateExpiration && <p id="dateExpiration-error" className="mt-1 text-sm text-error-600" role="alert">{errors.dateExpiration}</p>}
        
        <div className="input-group">
          <label htmlFor="description" className="label" aria-required="true">
            Description
          </label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows={5}
            className={`input ${errors.description ? 'border-error-500 focus:ring-error-500 focus:border-error-500' : ''}`}
            placeholder="Décrivez le poste, les responsabilités, et les qualifications requises..."
            aria-describedby="description-error"
          />
          {errors.description && <p id="description-error" className="mt-1 text-sm text-error-600" role="alert">{errors.description}</p>}
        </div>
        
        <Input
          label="Mots-clés (séparés par des virgules)"
          id="motsCles"
          name="motsCles"
          value={formData.motsCles}
          onChange={handleChange}
          error={errors.motsCles}
          placeholder="ex: React, JavaScript, Remote"
          helper="Ajoutez des mots-clés pertinents pour votre offre d'emploi"
          aria-describedby="motsCles-error"
        />
        {errors.motsCles && <p id="motsCles-error" className="mt-1 text-sm text-error-600" role="alert">{errors.motsCles}</p>}
        
        {submissionError && <p className="text-red-500 text-sm" role="alert">{submissionError}</p>}
        {success && <p className="text-green-500 text-sm" role="alert">{success}</p>}
        
        <div className="flex justify-end pt-4">
          <Button type="submit" disabled={loading} aria-label={loading ? 'Envoi en cours' : 'Publier l\'offre'}>
            {loading ? 'Envoi en cours...' : "Publier l'offre"}
          </Button>
        </div>
      </form>
    </Card>
  );
};

export default JobForm;