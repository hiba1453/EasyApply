import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Input from '../../components/ui/Input';
import PasswordInput from '../../components/ui/PasswordInput';
import Button from '../../components/ui/Button';

interface RegisterFormProps {
  onSubmit?: (data: any) => void;
}

const RegisterForm: React.FC<RegisterFormProps> = ({ onSubmit }) => {
  const [formData, setFormData] = useState({
    nom: '',
    email: '',
    motDePasse: '',
    confirmedMotDePasse: '',
    telephone: '',
    dateNaissance: '',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [serverError, setServerError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
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
    setServerError(null);
  };

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.nom.trim()) {
      newErrors.nom = "Le nom est requis";
    }

    if (!formData.email) {
      newErrors.email = "L'email est requis";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "L'email est invalide";
    }

    if (!formData.motDePasse) {
      newErrors.motDePasse = "Le mot de passe est requis";
    } else if (formData.motDePasse.length < 8) {
      newErrors.motDePasse = "Le mot de passe doit contenir au moins 8 caractères";
    }

    if (formData.motDePasse !== formData.confirmedMotDePasse) {
      newErrors.confirmedMotDePasse = "Les mots de passe ne correspondent pas";
    }

    if (!formData.telephone) {
      newErrors.telephone = "Le téléphone est requis";
    }

    if (!formData.dateNaissance) {
      newErrors.dateNaissance = "La date de naissance est requise";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (validateForm()) {
      // On retire confirmedMotDePasse avant l'envoi
      const { confirmedMotDePasse, ...submitData } = formData;

      try {
        const response = await fetch('http://localhost:8090/register', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(submitData),
        });

        const result = await response.json();

        if (!response.ok) {
          setServerError(result.error || "Erreur lors de l'inscription. Vérifiez les champs.");
        } else {
          if (onSubmit) onSubmit(result);
          navigate('/login');
        }
      } catch (error) {
        setServerError("Erreur réseau. Impossible de contacter le serveur.");
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

      {serverError && <div className="text-red-500 text-sm text-center">{serverError}</div>}

      <form onSubmit={handleSubmit} className="mt-8 space-y-5">
        <Input
          label="Nom"
          id="nom"
          name="nom"
          type="text"
          value={formData.nom}
          onChange={handleChange}
          placeholder="Votre nom"
          error={errors.nom}
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
          id="motDePasse"
          name="motDePasse"
          value={formData.motDePasse}
          onChange={handleChange}
          error={errors.motDePasse}
          helper="8 caractères minimum"
          autoComplete="new-password"
        />

        <PasswordInput
          label="Confirmer le mot de passe"
          id="confirmedMotDePasse"
          name="confirmedMotDePasse"
          value={formData.confirmedMotDePasse}
          onChange={handleChange}
          error={errors.confirmedMotDePasse}
          autoComplete="new-password"
        />

        <Input
          label="Téléphone"
          id="telephone"
          name="telephone"
          type="tel"
          value={formData.telephone}
          onChange={handleChange}
          placeholder="06 12 34 56 78"
          error={errors.telephone}
          autoComplete="tel"
        />

        <Input
          label="Date de naissance"
          id="dateNaissance"
          name="dateNaissance"
          type="date"
          value={formData.dateNaissance}
          onChange={handleChange}
          error={errors.dateNaissance}
          autoComplete="bday"
        />

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