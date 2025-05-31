import React, { useState } from 'react';
import Input from '../ui/Input';
import Button from '../ui/Button';
import Card from '../ui/Card';

interface JobFormProps {
  onSubmit: (jobData: any) => void;
  initialValues?: any;
}

const JobForm: React.FC<JobFormProps> = ({ onSubmit, initialValues = {} }) => {
  const [formData, setFormData] = useState({
    title: initialValues.title || '',
    company: initialValues.company || '',
    location: initialValues.location || '',
    salary: initialValues.salary || '',
    description: initialValues.description || '',
    tags: initialValues.tags ? initialValues.tags.join(', ') : '',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    
    // Clear error when field is changed
    if (errors[name]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  const validateForm = () => {
    const newErrors: Record<string, string> = {};
    
    if (!formData.title.trim()) {
      newErrors.title = 'Le titre est requis';
    }
    
    if (!formData.company.trim()) {
      newErrors.company = "Le nom de l'entreprise est requis";
    }
    
    if (!formData.location.trim()) {
      newErrors.location = 'La localisation est requise';
    }
    
    if (!formData.description.trim()) {
      newErrors.description = 'La description est requise';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (validateForm()) {
      // Process tags string into array
      const processedData = {
        ...formData,
        tags: formData.tags.split(',').map(tag => tag.trim()).filter(tag => tag)
      };
      
      onSubmit(processedData);
    }
  };

  return (
    <Card>
      <h2 className="text-xl font-semibold mb-6">Publier une offre d'emploi</h2>
      
      <form onSubmit={handleSubmit} className="space-y-4">
        <Input
          label="Titre du poste"
          id="title"
          name="title"
          value={formData.title}
          onChange={handleChange}
          error={errors.title}
          placeholder="ex: Développeur Frontend React"
        />
        
        <Input
          label="Entreprise"
          id="company"
          name="company"
          value={formData.company}
          onChange={handleChange}
          error={errors.company}
          placeholder="ex: TechCorp"
        />
        
        <Input
          label="Localisation"
          id="location"
          name="location"
          value={formData.location}
          onChange={handleChange}
          error={errors.location}
          placeholder="ex: Paris, France"
        />
        
        <Input
          label="Salaire"
          id="salary"
          name="salary"
          value={formData.salary}
          onChange={handleChange}
          error={errors.salary}
          placeholder="ex: 45 000 € - 60 000 €"
        />
        
        <div className="input-group">
          <label htmlFor="description" className="label">
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
          />
          {errors.description && <p className="mt-1 text-sm text-error-600">{errors.description}</p>}
        </div>
        
        <Input
          label="Tags (séparés par des virgules)"
          id="tags"
          name="tags"
          value={formData.tags}
          onChange={handleChange}
          error={errors.tags}
          placeholder="ex: React, JavaScript, Remote"
          helper="Ajoutez des mots-clés pertinents pour votre offre d'emploi"
        />
        
        <div className="flex justify-end pt-4">
          <Button type="submit">Publier l'offre</Button>
        </div>
      </form>
    </Card>
  );
};

export default JobForm;