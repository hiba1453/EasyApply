import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Briefcase } from 'lucide-react';
import LoginForm from '../../components/auth/LoginForm';

const Login: React.FC = () => {
  const navigate = useNavigate();

  const handleSubmit = async (data: { email: string; motDePasse: string; token: string; user: any }) => {
    try {
      const response = await fetch('http://localhost:8090/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: data.email,
          motDePasse: data.motDePasse,
        }),
        credentials: 'include',
      });

      const result = await response.json();

      if (response.ok) {
        // Store token, role and userId
        localStorage.setItem('token', result.token);
        localStorage.setItem('role', result.role);
        localStorage.setItem('userId', result.userId.toString());
        
        // Redirect based on role
        switch(result.role) {
          case 'ADMIN':
            navigate('/dashboard/admin');
            break;
          case 'CANDIDAT':
            navigate('/dashboard/candidate/jobs');
            break;
          case 'ENTREPRISE':
            navigate('/dashboard/company');
            break;
          default:
            navigate('/');
        }
      } else {
        // Error: show error message
        alert(result.error || "Erreur lors de la connexion");
      }
    } catch (error) {
      console.error('Login error:', error);
      alert("Erreur réseau");
    }
  };

  return (
    <div className="min-h-screen flex">
      {/* Left side - Form */}
      <div className="w-full lg:w-1/2 flex items-center justify-center p-8">
        <div className="w-full max-w-md">
          <div className="flex justify-center mb-8">
            <Link to="/" className="flex items-center space-x-2">
              <Briefcase className="w-10 h-10 text-primary-600" />
              <span className="text-2xl font-bold text-primary-800">EasyApply</span>
            </Link>
          </div>
          
          <LoginForm onSubmit={handleSubmit} />
        </div>
      </div>
      
      {/* Right side - Image */}
      <div className="hidden lg:block lg:w-1/2 bg-cover bg-center" style={{ 
        backgroundImage: "url('https://images.pexels.com/photos/3182759/pexels-photo-3182759.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2')"
      }}>
        <div className="h-full w-full bg-gradient-to-b from-primary-900/60 to-primary-800/60 flex items-center justify-center p-16">
          <div className="text-white max-w-lg">
            <h2 className="text-3xl font-bold mb-4">Votre carrière commence ici</h2>
            <p className="text-xl text-primary-100 mb-6">
              Connectez-vous pour accéder à des milliers d'offres d'emploi adaptées à votre profil et à vos ambitions.
            </p>
            <div className="grid grid-cols-2 gap-4 text-sm">
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Recommandations d'emploi personnalisées</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>CV optimisé pour les recruteurs</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Candidature en 1 clic</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Suivi des candidatures en temps réel</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;