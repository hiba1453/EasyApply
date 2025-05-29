import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Input from '../ui/Input';
import Button from '../ui/Button';
import PasswordInput from '../ui/PasswordInput';

interface RegisterFormProps {
  onSubmit: (data: {
    name: string;
    email: string;
    password: string;
    type: 'candidate' | 'company';
  }) => void;
}

const RegisterForm: React.FC<RegisterFormProps> = ({ onSubmit }) => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    type: 'candidate' as 'candidate' | 'company',
  });
  
  const [errors, setErrors] = useState<Record<string, string>>({});

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
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
    
    if (!formData.name.trim()) {
      newErrors.name = "Le nom est requis";
    }
    
    if (!formData.email) {
      newErrors.email = "L'email est requis";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "L'email est invalide";
    }
    
    if (!formData.password) {
      newErrors.password = "Le mot de passe est requis";
    } else if (formData.password.length < 8) {
      newErrors.password = "Le mot de passe doit contenir au moins 8 caractères";
    }
    
    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Les mots de passe ne correspondent pas";
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (validateForm()) {
      const { confirmPassword, ...submitData } = formData;
      try {
        const response = await fetch('http://localhost:8081/api/auth/register', {
          method: 'POST',
          headers: {
            'Content-type': 'application/json',
          },
          body: JSON.stringify(submitData),
        });

        if (!response.ok) {
          const errorData = await response.json();
          console.error("Erreur serveur :", errorData);
          alert("Erreur lors de l'inscription : " + (errorData.message || "Vérifiez les champs."));
        } else {
          const result = await response.json();
          console.log("Inscription réussie :", result);
          alert("Inscription réussie !");
        }
      } catch (error) {
        console.error("Erreur réseau :", error);
        alert("Erreur réseau. Impossible de contacter le serveur.");
      }
    }
  };

  return (
    <div className="max-w-md w-full space-y-6">
      <div className="text-center">
        <h2 className="text-3xl font-bold text-gray-900">Créer un compte</h2>
        <p className="mt-2 text-gray-600">
          Rejoignez EasyApply pour accéder à des offres d'emploi adaptées à votre profil
        </p>
      </div>
      
      <form onSubmit={handleSubmit} className="mt-8 space-y-5">
        <Input
          label="Nom complet"
          id="name"
          name="name"
          type="text"
          value={formData.name}
          onChange={handleChange}
          placeholder="safae"
          error={errors.name}
          autoComplete="name"
        />
        
        <Input
          label="Email"
          id="email"
          name="email"
          type="email"
          value={formData.email}
          onChange={handleChange}
          placeholder="votre.email@exemple.com"
          error={errors.email}
          autoComplete="email"
        />
        
        <PasswordInput
          label="Mot de passe"
          id="password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          error={errors.password}
          helper="8 caractères minimum"
          autoComplete="new-password"
        />
        
        <PasswordInput
          label="Confirmer le mot de passe"
          id="confirmPassword"
          name="confirmPassword"
          value={formData.confirmPassword}
          onChange={handleChange}
          error={errors.confirmPassword}
          autoComplete="new-password"
        />
        
        <div className="input-group">
          <label htmlFor="type" className="label">
            type de compte
          </label>
          <div className="flex space-x-4 mt-1">
            <label className="flex items-center">
              <input
                type="radio"
                name="type"
                value="candidate"
                checked={formData.type === 'candidate'}
                onChange={handleChange}
                className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300"
              />
              <span className="ml-2 text-gray-700">Candidat</span>
            </label>
            <label className="flex items-center">
              <input
                type="radio"
                name="type"
                value="company"
                checked={formData.type === 'company'}
                onChange={handleChange}
                className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300"
              />
              <span className="ml-2 text-gray-700">Entreprise</span>
            </label>
          </div>
        </div>
        
        <div className="flex items-center">
          <input
            id="terms"
            name="terms"
            type="checkbox"
            className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
            required
          />
          <label htmlFor="terms" className="ml-2 block text-sm text-gray-700">
            J'accepte les{' '}
            <Link to="/terms" className="font-medium text-primary-600 hover:text-primary-500">
              conditions d'utilisation
            </Link>{' '}
            et la{' '}
            <Link to="/privacy" className="font-medium text-primary-600 hover:text-primary-500">
              politique de confidentialité
            </Link>
          </label>
        </div>
        
        <Button type="submit" fullWidth>
          S'inscrire
        </Button>
      </form>
      
      <div className="text-center mt-4">
        <p className="text-sm text-gray-600">
          Vous avez déjà un compte ?{' '}
          <Link to="/login" className="font-medium text-primary-600 hover:text-primary-500">
            Connectez-vous
          </Link>
        </p>
      </div>
    </div>
  );
};

export default RegisterForm;