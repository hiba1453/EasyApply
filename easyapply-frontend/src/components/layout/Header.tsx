import React, { useState, useEffect } from 'react';
import { Menu, X, Search, Briefcase } from 'lucide-react';
import { Link, useLocation } from 'react-router-dom';

const Header: React.FC = () => {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const location = useLocation();
  const isAuthenticated = location.pathname.includes('/dashboard');

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 10);
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  return (
    <header
      className={`fixed top-0 left-0 right-0 z-50 transition-all duration-300 ${
        isScrolled ? 'bg-white shadow-md py-2' : 'bg-transparent py-4'
      }`}
    >
      <div className="container mx-auto px-4 md:px-6">
        <div className="flex items-center justify-between">
          <Link to="/" className="flex items-center space-x-2">
            <Briefcase className="w-8 h-8 text-primary-600" />
            <span className="text-xl font-bold text-primary-800">EasyApply</span>
          </Link>

          {!isAuthenticated && (
            <>
              <nav className="hidden md:flex items-center space-x-8">
                <Link to="/" className="text-gray-700 hover:text-primary-600 font-medium">
                  Accueil
                </Link>
                <Link to="/dashboard/candidate/jobs" className="text-gray-700 hover:text-primary-600 font-medium">
                  Offres
                </Link>
                <Link to="/companies" className="text-gray-700 hover:text-primary-600 font-medium">
                  Entreprises
                </Link>
                <Link to="/about" className="text-gray-700 hover:text-primary-600 font-medium">
                  À propos
                </Link>
                <Link to="/dashboard/candidate/jobs/" className="text-gray-700 hover:text-primary-600 font-medium">
                  Dashboard
                </Link>
              </nav>

              <div className="hidden md:flex items-center space-x-4">
                <Link to="/login" className="btn-secondary btn-sm">
                  Se connecter
                </Link>
                <Link to="/register" className="btn-primary btn-sm">
                  S'inscrire
                </Link>
              </div>

              <button
                className="md:hidden text-gray-700 hover:text-primary-600"
                onClick={toggleMenu}
                aria-label="Menu"
              >
                {isMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
              </button>
            </>
          )}

          {isAuthenticated && (
            <div className="flex items-center space-x-4">
              <div className="relative hidden md:block">
                <input
                  type="text"
                  placeholder="Rechercher..."
                  className="pl-10 pr-4 py-2 rounded-full border border-gray-300 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 w-64"
                />
                <Search className="absolute left-3 top-2.5 w-5 h-5 text-gray-400" />
              </div>
              <div className="flex items-center space-x-3">
                <div className="hidden md:block">
                  <span className="text-sm font-medium text-gray-700">Thomas Dubois</span>
                </div>
                <div className="w-8 h-8 rounded-full bg-primary-100 flex items-center justify-center text-primary-700 font-medium">
                  TD
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Mobile menu */}
        {isMenuOpen && !isAuthenticated && (
          <div className="md:hidden mt-4 pb-4 animate-fade-in">
            <nav className="flex flex-col space-y-4">
              <Link 
                to="/" 
                className="text-gray-700 hover:text-primary-600 font-medium py-2"
                onClick={() => setIsMenuOpen(false)}
              >
                Accueil
              </Link>
              <Link 
                to="/dashboard/candidate/jobs" 
                className="text-gray-700 hover:text-primary-600 font-medium py-2"
                onClick={() => setIsMenuOpen(false)}
              >
                Offres
              </Link>
              <Link 
                to="/companies" 
                className="text-gray-700 hover:text-primary-600 font-medium py-2"
                onClick={() => setIsMenuOpen(false)}
              >
                Entreprises
              </Link>
              <Link 
                to="/about" 
                className="text-gray-700 hover:text-primary-600 font-medium py-2"
                onClick={() => setIsMenuOpen(false)}
              >
                À propos
              </Link>
              <div className="flex flex-col space-y-3 pt-2">
                <Link 
                  to="/login" 
                  className="btn-secondary w-full text-center"
                  onClick={() => setIsMenuOpen(false)}
                >
                  Se connecter
                </Link>
                <Link 
                  to="/register" 
                  className="btn-primary w-full text-center"
                  onClick={() => setIsMenuOpen(false)}
                >
                  S'inscrire
                </Link>
              </div>
            </nav>
          </div>
        )}
      </div>
    </header>
  );
};

export default Header;