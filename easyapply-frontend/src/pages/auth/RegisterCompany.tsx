import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Briefcase } from 'lucide-react';
import RegisterCompanyForm from '../../components/auth/RegisterCompanyForm';

const RegisterCompany = () => {
  const navigate = useNavigate();
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [entrepriseId, setEntrepriseId] = useState<number | null>(null);

  const handleRegister = async (data: any) => {
    try {
      const response = await fetch('http://localhost:8090/register/company', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      });

      const result = await response.json();
      if (response.ok) {
        setSuccessMsg(result.message);
        setEntrepriseId(result.entrepriseId);
        // Si tu veux rediriger après 3 secondes :
        // setTimeout(() => navigate('/login', { state: { message: result.message } }), 3000);
      } else {
        setSuccessMsg(result.error || "Erreur lors de l'inscription.");
        setEntrepriseId(null);
      }
    } catch (error) {
      setSuccessMsg("Erreur réseau ou serveur.");
      setEntrepriseId(null);
      console.error('Erreur:', error);
    }
  };

  return (
    <div className="min-h-screen flex">
      {/* Left side - Image */}
      <div className="hidden lg:block lg:w-1/2 bg-cover bg-center" style={{ 
        backgroundImage: "url('https://images.pexels.com/photos/3183197/pexels-photo-3183197.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2')"
      }}>
        <div className="h-full w-full bg-gradient-to-b from-primary-900/60 to-primary-800/60 flex items-center justify-center p-16">
          <div className="text-white max-w-lg">
            <h2 className="text-3xl font-bold mb-4">Recrutez les meilleurs talents</h2>
            <p className="text-xl text-primary-100 mb-6">
              Créez votre compte entreprise pour publier vos offres d'emploi et accéder à notre base de candidats qualifiés.
            </p>
            <div className="grid grid-cols-2 gap-4 text-sm">
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Publication d'offres illimitée</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Gestion des candidatures</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Analytics et statistiques</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Support dédié</span>
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
          {/* Message de succès */}
          {successMsg && (
            <div className="mb-4 p-4 bg-green-100 text-green-800 rounded">
              {successMsg}
              {entrepriseId && (
                <div>ID de l'entreprise : <b>{entrepriseId}</b></div>
              )}
            </div>
          )}
          <RegisterCompanyForm onSubmit={handleRegister} />
        </div>
      </div>
    </div>
  );
};

export default RegisterCompany;