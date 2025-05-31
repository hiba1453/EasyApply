import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Briefcase } from 'lucide-react';
import RegisterForm from '../../components/auth/RegisterForm';

const Register: React.FC = () => {
  const navigate = useNavigate();

  const handleRegister = (data: {
    name: string;
    email: string;
    password: string;
   
  }) => {
    console.log('Register data:', data);
    // For demo purposes, we'll navigate to the appropriate dashboard based on user type
 
      navigate('/dashboard/candidate/jobs');
  }

  return (
    <div className="min-h-screen flex">
      {/* Left side - Image */}
      <div className="hidden lg:block lg:w-1/2 bg-cover bg-center" style={{ 
        backgroundImage: "url('https://images.pexels.com/photos/3184465/pexels-photo-3184465.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2')"
      }}>
        <div className="h-full w-full bg-gradient-to-b from-primary-900/60 to-primary-800/60 flex items-center justify-center p-16">
          <div className="text-white max-w-lg">
            <h2 className="text-3xl font-bold mb-4">Rejoignez la communauté EasyApply</h2>
            <p className="text-xl text-primary-100 mb-6">
              Créez votre compte pour accéder à des milliers d'offres d'emploi et développer votre carrière.
            </p>
            <div className="grid grid-cols-2 gap-4 text-sm">
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Accès à des offres exclusives</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Alertes personnalisées</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Réseau professionnel</span>
              </div>
              <div className="flex items-start space-x-2">
                <svg className="w-5 h-5 text-primary-300 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7"></path>
                </svg>
                <span>Conseils de carrière</span>
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
          
          <RegisterForm onSubmit={handleRegister} />
        </div>
      </div>
    </div>
  );
};

export default Register;