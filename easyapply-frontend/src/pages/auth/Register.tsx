import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Input from '../../components/ui/Input';
import PasswordInput from '../../components/ui/PasswordInput';
import Button from '../../components/ui/Button';
import { Briefcase } from 'lucide-react';
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
  const navigate = useNavigate(); // For redirecting after success

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
    setServerError(null); // Clear server errors on input change
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
      newErrors.telephone = "Le numéro de téléphone est requis";
    } else if (!/^\+?[1-9]\d{9,14}$/.test(formData.telephone)) {
      newErrors.telephone = "Numéro de téléphone invalide (ex: +33612345678)";
    }

    if (!formData.dateNaissance) {
      newErrors.dateNaissance = "La date de naissance est requise";
    } else {
      const birthDate = new Date(formData.dateNaissance);
      const today = new Date();
      const age = today.getFullYear() - birthDate.getFullYear();
      if (age < 18) {
        newErrors.dateNaissance = "Vous devez avoir au moins 18 ans";
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (validateForm()) {
      const submitData = { ...formData }; // Include all fields
       // Remove confirmed password from submission

      try {
        const response = await fetch('http://localhost:8090/register', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(submitData),
          credentials: 'include', // If backend requires cookies
        });

        const result = await response.json();

        if (!response.ok) {
          console.error("Erreur serveur :", result);
          setServerError(result.error || "Erreur lors de l'inscription. Vérifiez les champs.");
        } else {
          console.log("Inscription réussie :", result);
          if (onSubmit) onSubmit(result);
          navigate('/login'); // Redirect to login page on success
        }
      } catch (error) {
        console.error("Erreur réseau :", error);
        setServerError("Erreur réseau. Impossible de contacter le serveur.");
      }
    }
  };

  return (

    <div className="min-h-screen flex">
      {/* Left side - Image */}
      <div className="hidden lg:block lg:w-1/2 bg-cover bg-center" style={{ 
        backgroundImage: "url('https://images.pexels.com/photos/3184465/pexels-photo-3184465.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2')"
      }}>
        <div className="h-full w-full bg-gradient-to-b from-primary-900/60 to-primary-800/60 flex items-center justify-center p-16">
          <div className="text-white max-w-lg">
            <h2 className="text-3xl font-bold mb-4">Trouvez l'emploi de vos rêves</h2>
            <p className="text-xl text-primary-100 mb-6">
              Créez votre profil pour accéder aux meilleures opportunités et laissez les recruteurs vous trouver.
            </p>
            <div className="grid grid-cols-2 gap-4 text-sm">
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>CV personnalisable</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Candidature simplifiée</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Suivi des candidatures</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Alertes personnalisées</span>
              </div>
            </div>
          </div>
        </div>
      </div>
 
      
      {/* Right side - Form */}
      <div className="w-full lg:w-1/2 flex items-center justify-center p-8">
        <div className="w-full max-w-md">
          <div className="flex justify-center mb-8">
             <Link to="/" className="flex items-center space-x-2">
              <Briefcase className="w-10 h-10 text-primary-600" />
              <span className="text-2xl font-bold text-primary-800">EasyApply</span>
               </Link>
               </div>
    <div className="max-w-md w-full space-y-6">
      <div className="text-center">
        <h2 className="text-3xl font-bold text-gray-900">Créer un compte Candidat</h2>
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
          placeholder="Dupont"
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
          placeholder="+33612345678"
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
      <div className="text-center mt-4">
        <p className="text-sm text-gray-600">
          Vous etes une entreprise ?{' '}
          <Link to="/register/company" className="font-medium text-primary-600 hover:text-primary-500">
            S'inscrire comme entreprise
          </Link>
        </p>
      </div>
      
    </div>
      </div>
      </div>
    </div>
    
  );
};

export default RegisterForm;