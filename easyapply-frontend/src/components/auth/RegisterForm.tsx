import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Input from '../ui/Input';
import PasswordInput from '../ui/PasswordInput';
import Button from '../ui/Button';

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
      const { confirmedMotDePasse, ...submitData } = formData;
      if (onSubmit) {
        onSubmit(submitData);
      }
    }
  };

  return (
    <>
      <div className="text-center">
        <h2 className="text-3xl font-bold text-gray-900">Créer un compte</h2>
        <p className="mt-2 text-gray-600">
          Rejoignez EasyApply pour accéder à des offres d'emploi adaptées à votre profil
        </p>
      </div>

      {serverError && <div className="text-red-500 text-sm text-center mt-4">{serverError}</div>}

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

        <div className="space-y-2">
          <div className="text-center">
            <p className="text-sm text-gray-600">
              Vous avez déjà un compte ?{' '}
              <Link to="/login" className="font-medium text-primary-600 hover:text-primary-500">
                Connectez-vous
              </Link>
            </p>
          </div>
          <div className="text-center">
            <p className="text-sm text-gray-600">
              Vous êtes une entreprise ?{' '}
              <Link to="/register/company" className="font-medium text-primary-600 hover:text-primary-500">
                S'inscrire comme entreprise
              </Link>
            </p>
          </div>
        </div>
      </form>
    </>
  );
};

export default RegisterForm;