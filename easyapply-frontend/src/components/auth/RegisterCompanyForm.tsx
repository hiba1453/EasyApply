import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Input from '../ui/Input';
import Button from '../ui/Button';
import PasswordInput from '../ui/PasswordInput';
import { FaLinkedin } from 'react-icons/fa';

interface RegisterCompanyFormProps {
  onSubmit?: (data: any) => void;
}

const RegisterCompanyForm: React.FC<RegisterCompanyFormProps> = ({ onSubmit }) => {
  const [formData, setFormData] = useState({
    nom: '',
    email: '',
    motDePasse: '',
    confirmPassword: '',
    secteur: '',
    description: '',
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

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
  };

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.nom.trim()) {
      newErrors.nom = "Le nom de l'entreprise est requis";
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

    if (formData.motDePasse !== formData.confirmPassword) {
      newErrors.confirmPassword = "Les mots de passe ne correspondent pas";
    }

    if (!formData.secteur.trim()) {
      newErrors.secteur = "Le secteur d'activité est requis";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (validateForm()) {
      const { confirmPassword, ...submitData } = formData;

      try {
        const response = await fetch('http://localhost:8090/register/company', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(submitData),
        });

        if (!response.ok) {
          const errorData = await response.text();
          console.error("Erreur serveur :", errorData);
          alert("Erreur lors de l'inscription : " + (errorData || "Vérifiez les champs."));
          return;
        } else {
          const result = await response.json();
          console.log("Inscription entreprise réussie :", result);
          alert(result.message );
          if (onSubmit) onSubmit(result);
        }
      } catch (error) {
        console.error("Erreur réseau :", error);
        alert("Erreur réseau. Impossible de contacter le serveur.");
      }
    }
  };

  const handleLinkedinButton = () => {
    const clientId = "773tqf9qc1colw";
    const redirectUri = encodeURIComponent("http://localhost:3000/linkedInLogin"); // Mets ici ton redirectUri
    const state = "foobar"; // Génère un vrai state en prod
    const scope = "r_liteprofile%20r_emailaddress";

    const linkedinUrl = `https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}&state=${state}&scope=${scope}`;

    window.location.href = linkedinUrl;
  };

  return (
    <div className="max-w-md w-full space-y-6">
      <div className="text-center">
        <h2 className="text-3xl font-bold text-gray-900">Créer un compte entreprise</h2>
        <p className="mt-2 text-gray-600">
          Rejoignez EasyApply pour publier vos offres d'emploi et recruter les meilleurs talents
        </p>
      </div>

      <form onSubmit={handleSubmit} className="mt-8 space-y-5">
        <Input
          label="Nom de l'entreprise"
          id="nom"
          name="nom"
          type="text"
          value={formData.nom}
          onChange={handleChange}
          placeholder="TechCorp SARL"
          error={errors.nom}
          autoComplete="organization"
        />

        <Input
          label="Email professionnel"
          id="email"
          name="email"
          type="email"
          value={formData.email}
          onChange={handleChange}
          placeholder="contact@entreprise.com"
          error={errors.email}
          autoComplete="email"
        />

        <Input
          label="Secteur d'activité"
          id="secteur"
          name="secteur"
          type="text"
          value={formData.secteur}
          onChange={handleChange}
          placeholder="ex: Technologie, Finance, Santé..."
          error={errors.secteur}
        />

        <div className="input-group">
          <label htmlFor="description" className="label">
            Description de l'entreprise (optionnel)
          </label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows={3}
            className="input"
            placeholder="Décrivez brièvement votre entreprise, ses valeurs et sa mission..."
          />
        </div>

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
          id="confirmPassword"
          name="confirmPassword"
          value={formData.confirmPassword}
          onChange={handleChange}
          error={errors.confirmPassword}
          autoComplete="new-password"
        />

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
            </Link>{' '}et la{' '}
            <Link to="/privacy" className="font-medium text-primary-600 hover:text-primary-500">
              politique de confidentialité
            </Link>
          </label>
        </div>

        <Button type="submit" fullWidth>
          Créer le compte entreprise
        </Button>
      </form>

      <div className="text-center mt-4">
        <p className="text-sm text-gray-600">
          Vous avez déjà un compte ?{' '}
          <Link to="/login" className="font-medium text-primary-600 hover:text-primary-500">
            Connectez-vous
          </Link>
        </p>
        <p className="text-sm text-gray-600 mt-2">
          Vous êtes un candidat ?{' '}
          <Link to="/register" className="font-medium text-primary-600 hover:text-primary-500">
            S'inscrire comme candidat
          </Link>
        </p>
      </div>

      <button
        type="button"
        onClick={handleLinkedinButton}
        className="w-full flex items-center justify-center gap-2 bg-blue-700 hover:bg-blue-800 text-white py-2 px-4 rounded"
      >
        <FaLinkedin />
        Se connecter avec LinkedIn
      </button>
    </div>
  );
};

export default RegisterCompanyForm;