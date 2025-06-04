import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { FaLinkedin } from 'react-icons/fa'; 
import Input from '../ui/Input';
import Button from '../ui/Button';
import PasswordInput from '../ui/PasswordInput';

// Update the interface
interface LoginFormProps {
  onSubmit: (data: { email: string; motDePasse: string; token: string; user: any }) => void;
}

const LoginForm: React.FC<LoginFormProps> = ({ onSubmit }) => {
  // Update state to use motDePasse
  const [formData, setFormData] = useState({
    email: '',
    motDePasse: '', // Changed from password
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

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
  };

  const validateForm = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.email) {
      newErrors.email = "L'email est requis";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "L'email est invalide";
    }

    if (!formData.motDePasse) { // Changed from password
      newErrors.motDePasse = "Le mot de passe est requis";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (validateForm()) {
      try {
        const response = await fetch('http://localhost:8090/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(formData),
        });

        if (!response.ok) {
          const errorData = await response.json();
          console.error("Erreur serveur :", errorData);
          alert("Erreur lors de la connexion : " + (errorData.error || "Vérifiez vos identifiants."));
        } else {
          const result = await response.json();
          console.log("Connexion réussie :", result);
          alert("Connexion réussie !");
          // Pass result to parent component
          onSubmit({ ...formData, token: result.token, user: result.user });
        }
      } catch (error) {
        console.error("Erreur réseau :", error);
        alert("Erreur réseau. Impossible de contacter le serveur.");
      }
    }
  };

  // Fonction pour le bouton LinkedIn
  const handleLinkedinButton = () => {
    const clientId = '78wfcpplim9cxr'; // Replace with your LinkedIn Client ID
    const redirectUri = encodeURIComponent('http://localhost:8090/api/auth/linkedin/callback');
    const scope = encodeURIComponent('openid profile email');
    const state = Math.random().toString(36).substring(2); // Random state for security
    const authorizeUrl = `https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}&state=${state}&scope=${scope}`;

    window.location.href = authorizeUrl; // Redirect to LinkedIn for authentication
  };

  return (
    <div className="max-w-md w-full space-y-6">
      <div className="text-center">
        <h2 className="text-3xl font-bold text-gray-900">Connexion</h2>
        <p className="mt-2 text-gray-600">
          Accédez à votre compte pour gérer vos candidatures et votre profil
        </p>
      </div>

      <form onSubmit={handleSubmit} className="mt-8 space-y-5">
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
          id="motDePasse" // Changed from password
          name="motDePasse" // Changed from password
          value={formData.motDePasse} // Changed from password
          onChange={handleChange}
          error={errors.motDePasse} // Changed from password
          autoComplete="current-password"
        />

        <div className="flex items-center justify-between">
          <div className="flex items-center">
            <input
              id="remember-me"
              name="remember-me"
              type="checkbox"
              className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded"
            />
            <label htmlFor="remember-me" className="ml-2 block text-sm text-gray-700">
              Se souvenir de moi
            </label>
          </div>

          <div className="text-sm">
            <Link to="/forgot-password" className="font-medium text-primary-600 hover:text-primary-500">
              Mot de passe oublié ?
            </Link>
          </div>
        </div>

        <Button type="submit" fullWidth>
          Se connecter
        </Button>
      </form>

      {/* Bouton LinkedIn */}
      <div className="mt-6">
        <button
          type="button"
          onClick={handleLinkedinButton}
          className="w-full flex items-center justify-center gap-2 bg-blue-700 hover:bg-blue-800 text-white py-2 px-4 rounded">
          <FaLinkedin />
          Se connecter avec LinkedIn
        </button>
      </div>

      <div className="text-center mt-4">
        <p className="text-sm text-gray-600">
          Vous n'avez pas de compte ?{' '}
          <Link to="/register" className="font-medium text-primary-600 hover:text-primary-500">
            Inscrivez-vous
          </Link>
        </p>
      </div>
    </div>
  );
};

export default LoginForm;
